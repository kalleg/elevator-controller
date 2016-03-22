/*
 * Controller for elevator api
 *
 * Authors: Rasmus Linusson <raslin@kth.se>
 *          Karl Gafvert <kalleg@kth.se>
 *
 * Last modified: 17/3-2016
 */

#ifndef _REENTRANT
#define _REENTRANT
#endif

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>

#include "hardwareAPI.h"

/* Worker funcktions */
void *dispatcher(void *arg);
void *elevator(void *arg);


/* Structure for passing events between threads */
struct event {
    EventType type;
    EventDesc desc;
};

/* Helper functions */
void enqueue_event(int elevator, struct event *event);

/* Flag for verbosity */
short verbose = 0;

/*
 * Linked buffer definition
 * Used to buffer commands to be processed independently by elvator
 */
struct event_buffer {
    struct event_buffer *next;
    struct event event;
} event_buffer;

/*
 * Definition of thread safe wrapper for elevator control functions.
 */
void handle_door(int cabin, DoorAction action);
void handle_motor(int cabin, MotorAction action);
void handle_scale(int cabin, int floor);

/*
 * Stop queue
 * Each elevator owns a stop_queue which contains the stops of the elevator.
 *
 * TODO: Move to a separate file
 */
typedef struct node_stop_queue {
    int floor;
    struct node_stop_queue* next;
} node_stop_queue;

typedef struct {
    int size;
    node_stop_queue* first;
} stop_queue;

stop_queue* new_stop_queue();
int destroy_stop_queue(stop_queue*);

int push_stop_queue(int floor,
                    int current_floor,
                    int current_direction,
                    stop_queue* queue);
int pop_stop_queue(stop_queue*);
int peek_stop_queue(stop_queue*);

int size_stop_queue(stop_queue*);


/* Thread inter communications */

/*
 * Calls to api functions are critical sections, need mutex to assure correct
 * execution.
 *
 * Each elevator thread will receive a unique conditional variable to wait upon
 * new commands and information to act upon
 *
 * These commands will be placed in shared address space while mutualy exluded
 * using a elevator unique mutex
 */
pthread_mutex_t api_send_mutex;

pthread_mutex_t *elevator_event_buffer_mutex;
pthread_cond_t *elevator_signal;

/* Elevator-independent buffer of events to be processed */
struct event_buffer **elevator_event_buffer;

/* Parse the command line arguments for operational flags */
void parse_flags(int argc, char **argv,
        char **hostname, short *port, short *num_elevators, short *num_floors)
{
    int i;

    for (i = 1; i < argc; i++) {
        /* Check for value based flags */
        if (i < argc-1) {
            if (!strcmp(argv[i], "-h") || !strcmp(argv[i], "--host")) {
                *hostname = argv[i+1];
                i++;                    /* Skip next position as it was a value */
            }
            else if (!strcmp(argv[i], "-p") || !strcmp(argv[i], "--port")) {
                *port = atoi(argv[i+1]);
                i++;                    /* Skip next position as it was a value */
            }
            else if (!strcmp(argv[i], "-f") || !strcmp(argv[i], "--floors")) {
                *num_floors = atoi(argv[i+1]);
                i++;                    /* Skip next position as it was a value */
            }
            else if (!strcmp(argv[i], "-e") || !strcmp(argv[i], "--elevators")) {
                *num_elevators = atoi(argv[i+1]);
                i++;                    /* Skip next position as it was a value */
            }
            else if (!strcmp(argv[i], "-v") || !strcmp(argv[i], "--verbose")) {
                verbose = 1;
            }
        }
        else { /* not value base as it's last */
            if (!strcmp(argv[i], "-v") || !strcmp(argv[i], "--verbose")) {
                verbose = 1;
            }
            else {
                fprintf(stderr, "Unrecognized flag: %s - Exiting...\n", argv[i]);
                exit(1);
            }
        }
    }
}

/*
 * TODO: Update comments
 * TODO: Explain the +1 reasons - waste of memory < (might) readability
 * TODO: Tidy-up the init part, looks a bit messy
 */
int main(int argc, char **argv)
{
    long i;

    /* Default connection info to java gui */
    char *hostname = "127.0.0.1";
    short port = 4711;
    short num_elevators = 1;
    short num_floors = 3;

    pthread_t* threads = NULL;

    /* Parse arguments */
    parse_flags(argc, argv, &hostname, &port, &num_elevators, &num_floors);

    /* Init shared space variables */
    elevator_event_buffer_mutex = malloc((num_elevators+1)*sizeof(pthread_mutex_t));
    elevator_signal = malloc((num_elevators+1)*sizeof(pthread_cond_t));

    elevator_event_buffer = malloc((num_elevators+1)*sizeof(struct event_buffer*));
    for (i = 1; i <= num_elevators; i++) {
        elevator_event_buffer[i] = NULL;

        pthread_mutex_init(&elevator_event_buffer_mutex[i], NULL);
        pthread_cond_init(&elevator_signal[i], NULL);
    }

    /*
     * Spawn threads to handle elevators
     * +1 for indexing resons and matching towards gui indicies
     */
    threads = malloc((num_elevators+1)*sizeof(pthread_t));

    for (i = 1; i <= num_elevators; i++) {
        if (pthread_create(&threads[i], NULL, elevator, (void*)i) != 0) {
            perror("Cannot create elevator thread\n");
            exit(2);
        }
    }

    printf("Init connection to \"hardware\"\n");
    fflush(stdout);

    /* Init connection to java gui */
    initHW(hostname, port);

    printf("Wait for 5s for java gui to initialize\n");
    fflush(stdout);
    sleep(5);

    /* Enter dispatcher function */
    dispatcher(NULL);
    return 0;
}

/*
 * Incomming interface with hardware.
 *
 * Blocking on a tcp connection until message arrives.
 *
 * Message is then dispatched to the concerned elevator by placing message in
 * its buffer and signaling the thread for execution.
 *
 * Decisions as to which elevator should respond to a floor button request will
 * initially be calculated here but may be placed in separate thread to
 * increase throughput if necessary
 */
void *dispatcher(void *arg)
{
    /* Buffers between socket and elevator-specific buffer */
    EventType e;
    EventDesc ed;
    struct event event;

    if (verbose)
        printf("dispatcher up and running\n");

    while (1) {
        event.type = waitForEvent(&event.desc);

        switch(event.type) {
        case FloorButton:
            if (verbose) {
                printf("floor button pressed: floor %d, type %d\n", event.desc.fbp.floor,
                        (int) event.desc.fbp.type);
            }

            /*
             * TODO: Call ranking function, compare socrese between possible
             * elevators, enqueue new destination event for the best elevator
             */
            break;
        case CabinButton:
            if (verbose) {
                printf("cabin button pressed: cabin %d, floor %d\n", event.desc.cbp.cabin,
                        (int) event.desc.cbp.floor);
            }

            /* Simple button press from within the elevator, just forward it */
            enqueue_event(event.desc.cbp.cabin, &event);

            /* Wake elevator to handle event */
            pthread_cond_signal(&elevator_signal[event.desc.cbp.cabin]);
            break;
        case Position:
            if (verbose) {
                printf("cabin position: cabin %d, position %d\n", event.desc.cp.cabin,
                        (int) event.desc.cp.position);
            }

            /* TODO: Set door open/closed status */

            /* Forward elevator position */
            enqueue_event(event.desc.cbp.cabin, &event);

            /* Wake elevator to handle event */
            pthread_cond_signal(&elevator_signal[event.desc.cbp.cabin]);

            break;
        case Speed:
            if (verbose) {
                printf("speed %f\n", event.desc.s.speed);
            }

            /*
             * TODO: Examine if different strategies has to be implemented
             * depending on the elevators speeds. Perhaps breaking out the
             * calculations on which elevator is best fitted for handling
             * a floor button request to a spearate thread is needed for
             * higher speeds??
             */
            break;
        case Error:
                printf("error: \"%s\"\n", event.desc.e.str);
            break;

        default:
            if (verbose)
                printf("Received unknown event (type %d)\n", event.type);
        }
    }
}

/*
 * Function representing each elevator
 *
 * TODO: Nice way to kill elevators(?)
 */
void *elevator(void *arg)
{
    struct event event;

    int floor = 0,
        next_floor = 0,
        direction = 0;
    stop_queue* queue = new_stop_queue();

    DoorState door_state = DoorClose;
    short floor_visited = 1;

    int id = (int)(long)arg;

    if (verbose)
        printf("elevator %d up and running\n", id);

    while (1) {
        /* Wait until message is received */
        pthread_mutex_lock(&elevator_event_buffer_mutex[id]);
        pthread_cond_wait(&elevator_signal[id], &elevator_event_buffer_mutex[id]);

        /* Handle all new events */
        while (elevator_event_buffer[id] != NULL) {
            event = elevator_event_buffer[id]->event;

            if (verbose)
                printf("elevator %d received type %d\n", id, event.type);

            switch (event.type) {
                case CabinButton:
                    push_stop_queue(event.desc.cbp.floor,
                                        floor, direction, queue);
                    break;
                case Position:
                    floor = (int) event.desc.cp.position;
                    break;
                case Door:
                    door_state = event.desc.ds;
                    break;
                default:
                    if (verbose)
                        printf("Elevator %d received unknown event (type %d)\n",
                                id, event.type);
            }

            /* Dequeue */
            struct event_buffer* tmp = elevator_event_buffer[id];
            elevator_event_buffer[id] = elevator_event_buffer[id]->next;
            if (tmp)
                free(tmp);
        }

        pthread_mutex_unlock(&elevator_event_buffer_mutex[id]);

        /* Elevator logic */
        if (floor_visited) {
            /* Update scale (floor indicator) */
            handle_scale(id, floor);

            next_floor = peek_stop_queue(queue);

            /* Arrived at next floor stop (if moving) and open door */
            if (floor-next_floor == 0) {
                if (direction) handle_motor(id, 0);
                handle_door(id, 1);
                pop_stop_queue(queue);
                floor_visited = 0;
            }

            /* Elevator is not moving, start motor */
            else if (!direction) {
                direction = next_floor-floor;
                direction = direction/abs(direction);
                handle_motor(id, direction);
            }
        }
        else {
            if (door_state == DoorOpen) {
                sleep(5);
                handle_door(id, -1);
            }
            else if (door_state == DoorClose)
                floor_visited = 1;
        }
    }

    /*
    while (size_stop_queue(queue))
        pop_stop_queue(queue);

    destroy_stop_queue(queue);
    */
}

/*
 * Add event to elevators event queue, typical linked list fifo implementation
 *
 * If event is of type position, the queue is not considered fifo, old
 * positional values are worthless and should be discardevent.desc. So if any old
 * positional values are in the buffer, overwrite it. (Not implemented yet)
 *
 * Possibly positions should always be positioned first in the queue as timing
 * requirements can be though. (Simpler, led to it being implemented)
 *
 * TODO: Make double linked list as to get constant time for depositing events
 */
void enqueue_event(int elevator, struct event *event)
{
    /* Lock the buffer */
    pthread_mutex_lock(&elevator_event_buffer_mutex[elevator]);

    if (event->type == Position) {
        /* Empty buffer */
        if (elevator_event_buffer[elevator] == NULL) {
            elevator_event_buffer[elevator] = malloc(sizeof(struct event_buffer));
            elevator_event_buffer[elevator]->next = NULL;
            elevator_event_buffer[elevator]->event = *event;
        } else {
            /* First element is old positional event */
            if (elevator_event_buffer[elevator]->event.type == Position) {
                elevator_event_buffer[elevator]->event.desc = event->desc;
            } else {
                struct event_buffer *temp = malloc(sizeof(struct event_buffer));
                temp->event = *event;
                temp->next = elevator_event_buffer[elevator];
                elevator_event_buffer[elevator] = temp;
            }
        }
    } else {
        /* Add event last in queue */
        if (elevator_event_buffer[elevator] == NULL) {
            elevator_event_buffer[elevator] = malloc(sizeof(struct event_buffer));
            elevator_event_buffer[elevator]->next = NULL;
            elevator_event_buffer[elevator]->event = *event;
        } else {
            struct event_buffer *current = elevator_event_buffer[elevator];

            while (current->next != NULL)
                current = current->next;

            current->next = malloc(sizeof(struct event_buffer));
            current->next->next = NULL;
            current->next->event = *event;
        }
    }

    pthread_mutex_unlock(&elevator_event_buffer_mutex[elevator]);
}


/*
 * Thread safe wrapper of elevator control functions.
 * The hardware API specifies that none these functions can be executed in
 * parallel. Synchronization is achieved using api_send_mutex.
 *
 * TODO: Make fair?
 */
void handle_door(int cabin, DoorAction action) {
    pthread_mutex_lock(&api_send_mutex);
    handleDoor(cabin, action);
    pthread_mutex_unlock(&api_send_mutex);
}

void handle_motor(int cabin, MotorAction action) {
    pthread_mutex_lock(&api_send_mutex);
    handleMotor(cabin, action);
    pthread_mutex_unlock(&api_send_mutex);
}

void handle_scale(int cabin, int floor) {
    pthread_mutex_lock(&api_send_mutex);
    handleScale(cabin, floor);
    pthread_mutex_unlock(&api_send_mutex);
}

/**
 * Implementation of stop_queue
 * This is essentially a singly linked list which can store an 'infinite'
 * amount of elements. In reality the list will how ever store at most 
 * 14 elements (usually fewer) so the performance gain from implementing
 * this as doubly linked list is virtually none.
 *
 * TODO: Move to a separate file
 */
/* Returns a new initialized stop_queue */
stop_queue* new_stop_queue()
{
    stop_queue* queue;

    if ((queue = (stop_queue*) malloc(sizeof(stop_queue))) == NULL)
        return NULL;

    queue->first = NULL;
    queue->size = 0;

    return queue;
}

/* Destroys an empty stop_queue */
int destroy_stop_queue(stop_queue* queue)
{
    if (size_stop_queue(queue))
        return 1;

    free(queue);

    return 0;
}

/* Push a floor to stop_queue */
int push_stop_queue(int floor,
                    int current_floor,
                    int current_direction,
                    stop_queue* queue)
{
    node_stop_queue *new_node, *curr_node;

    if ((new_node = (node_stop_queue*) malloc(sizeof(node_stop_queue))) == NULL)
        return 1;

    /* Set basic node properties */
    new_node->floor = floor;
    new_node->next = NULL;

    /* Place node first if queue is empty */
    if (!queue->size)
        queue->first = new_node;

    /* 
     * Put node in a sensible position 
     * TODO: put new floor on a sensible position 
     */
    else {
        curr_node = queue->first;
        while (curr_node->next != NULL)
            curr_node = curr_node->next;

        curr_node->next = new_node; 
    }

    ++queue->size;

    return 0;
}

/* Removes and returns the floor of a stop_queue */
int pop_stop_queue(stop_queue* queue)
{
    node_stop_queue* old_first;
    int floor;

    if (!queue->size)
        return -1;

    floor = queue->first->floor;

    old_first = queue->first;
    queue->first = old_first->next;
    free(old_first);

    --queue->size;

    return floor;
}

/* Returns the floor of a stop_queue */
int peek_stop_queue(stop_queue* queue)
{
    if (!queue->size)
        return -1;

    return queue->first->floor;
}

/* Returns the size of a stop_queue */
int size_stop_queue(stop_queue* queue)
{
    return queue->size;
}
