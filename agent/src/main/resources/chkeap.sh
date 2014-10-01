#!/bin/bash
ENV_SH=( `find / -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/env.sh" 2> /dev/null | grep -v code | grep -v t6` )
DS_XML=( `find / -regextype egrep -regex ".*/apps\/\w+-ds.xml" 2> /dev/null` )
LOGIN_CONFIG_XML=( `find / -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/conf\/login-config.xml" 2> /dev/null | grep -v code` )

#echo "Array size: ${#ENV_SH[@]}"

if [ $ENV_SH ] ; then
        VERSION=`cat $ENV_SH | grep JBOSS_HOME= | grep -v echo | awk '{ print substr($2, length($2)-12, 13); }'`
        JBOSS_DIR=`cat $ENV_SH | grep JBOSS_DIR= | grep -v echo | awk '{ print substr($2, 11, length($2)-10); }'`
        JBOSS_HOME=`cat $ENV_SH | grep JBOSS_HOME= | grep -v echo | awk '{ print substr($2, 12, length($2)-11); }'`
        JBOSS_USER=`cat $ENV_SH | grep JBOSS_USER= | grep -v echo | awk '{ print substr($2, 12, length($2)-11); }'`
        SERVER_HOME=`cat $ENV_SH | grep SERVER_HOME= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        SERVER_NAME=`cat $ENV_SH | grep SERVER_NAME= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`

        if [ $JBOSS_DIR ] ; then
                echo $JBOSS_DIR" "$JBOSS_USER" "$SERVER_NAME" "$JBOSS_HOME" "$SERVER_HOME > tmp.info
                cat tmp.info
                SERVER_NAME=`cat tmp.info | awk '{ sub(/JBOSS_USER/, $2, $3); print $3 }' | sed -E 's/([${}])//g'`
                JBOSS_HOME=`cat tmp.info | awk '{ sub(/JBOSS_DIR/, $1, $4); print $4 }' | sed -E 's/([${}])//g'`
                SERVER_HOME=`cat tmp.info | awk '{ sub(/JBOSS_DIR/, $1, $5); print $5 }' | sed -E 's/([${}])//g'`
                SERVER_BASE=$SERVER_HOME
        else
                echo $JBOSS_USER" "$SERVER_NAME > tmp.info
                SERVER_NAME=`cat tmp.info | awk '{ sub(/JBOSS_USER/, $1, $2); print $2 }' | sed -E 's/([${}])//g'`
                SERVER_BASE=`cat $ENV_SH | grep SERVER_BASE= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        fi

        rm -f tmp.info

        JBOSS_INIT=`chkconfig --list | grep -o jboss_init`

        if [ $JBOSS_INIT ] ; then
                START_CMD="service jboss_init start"
                STOP_CMD="service jboss_init stop"
        else
                START_CMD="runuser -l "${JBOSS_USER}" -c 'cd ${SERVER_BASE}/${SERVER_NAME} && sh startNode.sh notail'"
                STOP_CMD="runuser -l ${JBOSS_USER} -c ‘cd ${SERVER_BASE}/${SERVER_NAME} && sh kill.sh'"
        fi
fi

echo "JBOSS_DIR="$JBOSS_DIR
echo "JBOSS_HOME="$JBOSS_HOME
echo "JBOSS_USER="$JBOSS_USER
echo "SERVER_HOME="$SERVER_HOME
echo "SERVER_NAME="$SERVER_NAME
echo "SERVER_BASE="$SERVER_BASE
echo "VERSION="$VERSION
echo "ENV_SH="$ENV_SH
echo "DS_XML="$DS_XML
echo "LOGIN_CONFIG_XML="$LOGIN_CONFIG_XML
echo "START_CMD="$START_CMD
echo "STOP_CMD="$STOP_CMD