#!/bin/bash

# Files and directories
bin_elevator='./elevator/lib/elevator.jar'
bin_controller='./controller'

# Settings
mk='false'
verbose=''
elevators_arg_gui=''
elevators_arg_controller=''
top_floor_arg=''
floors_arg=''
weigth_distance_arg=''
weigth_stops_arg=''

# Get arguments
while getopts 'mve:f:d:s:' flag; do
  case "${flag}" in
    m) mk='true' ;;
    v) verbose='-v' ;;
    e) elevators_arg_gui="-number ${OPTARG}"; elevators_arg_controller="-e ${OPTARG}" ;;
    f) top_floor_arg="-top $((${OPTARG}-1))"; floors_arg="-f ${OPTARG}" ;;
    d) weigth_distance_arg="WDISTANCE=${OPTARG}" ;;
    s) weigth_stops_arg="WSTOPS=${OPTARG}" ;;
    *) echo 'Exiting script!'; exit 1 ;;
  esac
done

# Make if necessary or requested
if [ ! -f './controller' -o $mk == 'true' ]; then
	echo 'Make'
	echo '--------------------------'
  if [ weigth_distance_arg != '' -o weigth_stops_arg != '' ]; then
    make clean all $weigth_distance_arg $weigth_stops_arg
  else
    make all
  fi
fi

if [ $? != 0 ]; then
	echo 'Exiting script!' 
	exit 1
fi

# Start elevator GUI
echo ''
echo 'Starting elevator GUI'
echo '--------------------------'

java -jar $bin_elevator -tcp $elevators_arg_gui $top_floor_arg &
gui_pid=$!
echo "PID = $gui_pid"

sleep 2

ps -p $gui_pid > /dev/null 2>&1
if [ $? != 0 ]; then
	echo ''
	echo 'Exiting script!' 
	exit 1
fi

echo $gui_pid > gui.pid

# Start
echo 'Starting elevator controller'
echo '--------------------------'

./controller $verbose $elevators_arg_controller $floors_arg

#echo $! > controller.pid
