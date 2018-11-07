#!/bin/bash
#description:
#chkconfig: 2345 00 05

source /etc/profile
. /etc/rc.d/init.d/functions

RUN_EXE=$smarttesting_HOME/bin/smarttesting

case "$1" in
start)
      echo "Starting  Server..."
      nohup $RUN_EXE run 2>&1 &
      ;;
stop)
      echo "Stopping  Server..."
      $RUN_EXE stop 
      ;;
restart)
      echo "Restarting  Server..."
      exec $RUN_EXE restart 2>&1 &
      ;;
status)
      status -p $smarttesting_HOME/pid.lock $RUN_EXE
      ;;
    
*)
      echo "Usage: $0 {start|stop|restart|status}"
      exit 1
      ;;
esac
