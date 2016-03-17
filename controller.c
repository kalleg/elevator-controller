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

/* Flag for verbosity */
short verbose = 0;

pthread_mutex_t api_send_mutex;

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
                fprintf(stderr, "Unregocnized flag: %s - Exiting...\n", argv[i]);
                exit(1);
            }
        }
    }
}

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
    EventType e;
    EventDesc ed;

    if (verbose)
        printf("dispatcher up and running\n");

    while (1) {
        e = waitForEvent(&ed);

        switch(e) {
        case FloorButton:
            if (verbose) {
                printf("floor button pressed: floor %d, type %d\n", ed.fbp.floor,
                        (int) ed.fbp.type);
            }
            break;
        case CabinButton:
            if (verbose) {
                printf("cabin button pressed: cabin %d, floor %d\n", ed.cbp.cabin,
                        (int) ed.cbp.floor);
            }
            break;
        case Position:
            if (verbose) {
                printf("cabin position: cabin %d, position %d\n", ed.cp.cabin,
                        (int) ed.cp.position);
            }
            break;
        case Speed:
            if (verbose) {
                printf("speed %f\n", ed.s.speed);
            }
            break;
        case Error:
            if (verbose) {
                printf("error: \"%s\"\n", ed.e.str);
            }
            break;
        }
    }
}

void *elevator(void *arg)
{
    int id = (int)(long)arg;

    if (verbose)
        printf("elevator %d up and running\n", id);

    while (1) {
        /* Kalles stuff to do */
    }
}
