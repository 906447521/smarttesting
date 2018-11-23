#!/bin/sh
# 自动部署所需要的sh, 启动进程脚本

BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`
PID=`ps -ef | grep 'app.name=smarttesting' |  grep 'basedir='$BASEDIR' '  | grep -v 'grep' | awk '{print $2}'`

if [ -z $PID ]; then
	./smarttesting restart
	echo "Starting ..."
else
	echo "The  had been started, PID is $PID, DIR is $BASEDIR"
fi