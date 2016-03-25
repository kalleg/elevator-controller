#!/bin/sh

kill `cat gui.pid`
#kill `cat controller.pid`
sleep 1
ps -f | grep -e "elevator" -e "controller"