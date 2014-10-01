#!/bin/bash
HTTPD=( `find /etc/init.d/ -name "*_httpd"` )

if [ $HTTPD ] ; then
        APACHE_HOME=( `cat $HTTPD | grep apachectl= | awk '{ print substr($1, 11, length($1)-25); }'` )
        HTTPD_CONF=( `cat $HTTPD | egrep -o 'httpd.{0,5}(.*\/)([^\/]*)\/conf\/httpd.conf' | awk '{ n = split($0, arr, " "); } END { print $n }'` )
        SERVICE_NAME=`echo $HTTPD | awk '{ print substr($1, 13, length($1)-12); }'`
fi

if [ $HTTPD_CONF ] ; then
        SERVER_HOME=`echo $HTTPD_CONF | awk '{ print substr($1, 0, length($1)-16); }'`
fi

if [ $SERVER_HOME ] ; then
        HTTPD_INFO_CONF=( `find $SERVER_HOME -name httpd-info.conf` )
        SSL_CONF=( `find $SERVER_HOME -name *ssl.conf` )
        URIWORKERMAP=( `find $SERVER_HOME -name uriworkermap.properties` )
        WORKERS=( `find $SERVER_HOME -name workers.properties` )
fi

START_CMD="service $SERVICE_NAME start"
STOP_CMD="service $SERVICE_NAME stop"

echo "HTTPD="$HTTPD
echo "APACHE_HOME="$APACHE_HOME
echo "SERVER_HOME="$SERVER_HOME
echo "HTTPD_CONF="$HTTPD_CONF
echo "HTTPD_INFO_CONF="$HTTPD_INFO_CONF
echo "SSL_CONF="$SSL_CONF
echo "URIWORKERMAP="$URIWORKERMAP
echo "WORKERS="$WORKERS
echo "START_CMD="$START_CMD
echo "STOP_CMD="$STOP_CMD