#!/bin/sh

#
# 执行./service.sh <service_name>,
# 即可注册为服务service_name
#
SERVICE_NAME=$1
BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`
cd $BASEDIR/bin

sed -e "s/\$smarttesting_HOME/${BASEDIR//\//\\/}/" $BASEDIR/bin/smarttesting.sh > \
./initd

sed -e "s/\$SERVICE_NAME/$1/" ./initd > \
/etc/rc.d/init.d/$1

rm ./initd

chmod 777 /etc/rc.d/init.d/$1
chmod +x $BASEDIR/bin/smarttesting
chkconfig --add $SERVICE_NAME

