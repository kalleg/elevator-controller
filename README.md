###################################
# elevator controller
#
# A tcp-based multi-threaded elevator controller implemented in C using
# the pthreads API
# 
# Author: Kalle Gäfvert <kgafvert@kth.se>
#         Rasmus Linusson <raslin@kth.se>
#
# Project for course ID1217 at KTH
###################################

Description of program:
    The controller communicates with a simulator over a tcp connection
    providing control of the elevator systems motors and visual interface while
    providing service for its users.

How to make:
    To make the project a Makefile is provided which has targets for release
    compilation aswell as debugging. By passing on definitions to make the
    weighting of the elevator selecting algorithm can be defined.

How to run:
    Running the elevator controller may be done manually by executing the
    binary file and providing nessecary flags for the controller to match the
    simulators setup. Or by running the script 'run.sh' which will start both
    the simulator and the controller with matching options.
