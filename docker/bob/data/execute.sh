#!/bin/bash

# Usage: execute.sh [WildFly mode] [configuration file]
#
# The default mode is 'standalone' and default configuration is based on the
# mode. It can be 'standalone.xml' or 'domain.xml'.

JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}
JBOSS_USER=jboss

function wait_for_server() {
  until `$JBOSS_CLI -c "ls /deployment" &> /dev/null`; do
    sleep 1
  done
}

function shut_down_server() {
  if [ "$JBOSS_MODE" = "standalone" ]; then
    $JBOSS_CLI -c ":shutdown"
  else
    $JBOSS_CLI -c "/host=*:shutdown"
  fi
}

echo "=> Setting up permissions for the log volume"
TARGET_GID=$(stat -c "%g" $JBOSS_HOME/standalone/log)
EXISTS=$(cat /etc/group | grep $TARGET_GID | wc -l)
if [ $EXISTS == "0" ]; then
  groupadd -g $TARGET_GID bob
  usermod -a -G bob jboss
else
  GROUP=$(getent group $TARGET_GID | cut -d: -f1)
  usermod -a -G $GROUP $JBOSS_USER
fi

echo "=> Starting WildFly server"
$JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0 -c $JBOSS_CONFIG &

echo "=> Waiting for the server to boot"
wait_for_server

echo "=> Executing the commands"

$JBOSS_CLI -c << EOF
batch

# Add MySQL module
module add --name=com.mysql --resources=/opt/jboss/wildfly/customization/mysql-connector-java.jar --dependencies=javax.api,javax.transaction.api

# Add MySQL driver
/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-xa-datasource-class-name=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource)

# Add the datasource
data-source add --name=bob --driver-name=mysql --jndi-name=java:jboss/datasources/bob --connection-url=jdbc:mysql://mysql:3306/bob?useUnicode=true&amp;characterEncoding=UTF-8 --user-name=mysql --password=mysql --use-ccm=false --jta=true --max-pool-size=25 --blocking-timeout-wait-millis=5000 --enabled=true

# Set up thread and bean pools
/subsystem=ejb3/thread-pool=default:write-attribute(name="max-threads", value="1000")
/subsystem=ejb3/strict-max-bean-instance-pool=slsb-strict-max-pool:write-attribute(name="max-pool-size", value="1000")

# Increase connection timeout
/subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=tcp-keep-alive,value=true)
/subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=read-timeout,value=1200000)

# Execute the batch
run-batch
EOF

# deploy
cp /opt/jboss/wildfly/customization/bob-rest-impl.war $JBOSS_HOME/$JBOSS_MODE/deployments/bob-rest.war

echo "=> Restarting WildFly"
shut_down_server
$JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0 -c $JBOSS_CONFIG
