/*
 * Controller for elevator api
 *
 * Authors: Rasmus Linusson <raslin@kth.se>
 *          Karl Gafvert <kalleg@kth.se>
 *
 * Last modified: 17/3-2016
 */

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

#include "hardwareAPI.h"

int main(int argc, char **argv)
{
    /* Default connection info to java gui */
    char *hostname = "127.0.0.1";
    short port = 4711;

    /* Fetch arguments */
    if (argc > 1)
        hostname = argv[1];
    if (argc > 2)
        port = atoi(argv[2]);

    printf("Init connection to \"hardware\"\n");
    fflush(stdout);

    /* Init connection to java gui */
    initHW(hostname, port);

    printf("Wait for 10s for some reason\n");
    fflush(stdout);
    sleep(10);

    printf("Do a test\n");
    handleDoor(0, DoorOpen);
    sleep(3);
}
