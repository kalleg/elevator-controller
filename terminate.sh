#!/bin/bash

#kill -SIGINT `cat controller.pid`
kill `cat gui.pid`
sleep 1
ps -f | grep -e "elevator" -e "controller"
 