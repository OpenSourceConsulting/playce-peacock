#!/bin/bash
#ENV_SH=( `locate --regex "Servers\/\w+(Server(|[0-9]+))\/env.sh" | grep -v code | grep -v t6` )
#SERVER_XML=( `locate --regex "Servers\/\w+(Server(|[0-9]+))\/conf\/server.xml" | grep -v code | grep -v t6` )
#CONTEXT_XML=( `locate --regex "Servers\/\w+(Server(|[0-9]+))\/conf\/context.xml" | grep -v code | grep -v t6` )
ENV_SHS=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/env.sh" 2> /dev/null | grep -v code | grep -v t6` )
SERVER_XML=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/conf\/server.xml" 2> /dev/null | grep -v code | grep -v t6` )
CONTEXT_XML=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/conf\/context.xml" 2> /dev/null | grep -v code | grep -v t6` )

for i in "${ENV_SHS[@]}"
do
        CATALINA_HOME=( `cat $i | grep CATALINA_HOME=` )
        if [ $CATALINA_HOME ] ; then
        		ENV_SH=$i
				break
        fi
done

if [ $ENV_SH ] ; then
        VERSION=`cat $ENV_SH | grep CATALINA_HOME= | grep -v echo | awk '{ print substr($2, length($2)-6, 7); }'`
        SERVER_USER=`cat $ENV_SH | grep SERVER_USER= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        SERVER_HOME=`cat $ENV_SH | grep SERVER_HOME= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        SERVER_NAME=`cat $ENV_SH | grep SERVER_NAME= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        echo $SERVER_USER" "$SERVER_NAME > tmp.info
        SERVER_NAME=`cat tmp.info | awk '{ sub(/SERVER_USER/, $1, $2); print $2 }' | sed -E 's/([${}])//g'`
        CATALINA_HOME=`cat $ENV_SH | grep CATALINA_HOME= | grep -v echo | awk '{ print substr($2, 15, length($2)-14); }'`
        CATALINA_BASE=`cat $ENV_SH | grep CATALINA_BASE= | grep -v echo | awk '{ print substr($2, 15, length($2)-14); }'`
        echo $SERVER_HOME" "$SERVER_NAME" "$CATALINA_HOME" "$CATALINA_BASE > tmp.info
        CATALINA_HOME=`cat tmp.info | awk '{ sub(/SERVER_HOME/, $1, $3); print $3 }' | sed -E 's/([${}])//g'`
        CATALINA_BASE=`cat tmp.info | awk '{ sub(/SERVER_HOME/, $1, $4); print }' | awk '{ sub(/SERVER_NAME/, $2, $4); print $4 }' | sed -E 's/([${}])//g'`
        rm -f tmp.info

        JBOSSEWS=`chkconfig --list | grep -o jbossews`

        if [ $JBOSSEWS ] ; then
                START_CMD="service jbossews start"
                STOP_CMD="service jbossews stop"
        else
                START_CMD="runuser -l "${SERVER_USER}" -c 'cd ${CATALINA_BASE} && sh startNode.sh notail'"
                STOP_CMD="runuser -l ${SERVER_USER} -c ‘cd ${CATALINA_BASE} && sh kill.sh'"
        fi
fi

echo "SERVER_USER="$SERVER_USER
echo "SERVER_NAME="$SERVER_NAME
echo "SERVER_HOME="$SERVER_HOME
echo "CATALINA_HOME="$CATALINA_HOME
echo "CATALINA_BASE="$CATALINA_BASE
echo "VERSION="$VERSION
echo "ENV_SH="$ENV_SH
echo "SERVER_XML="$SERVER_XML
echo "CONTEXT_XML="$CONTEXT_XML
echo "START_CMD="$START_CMD
echo "STOP_CMD="$STOP_CMD