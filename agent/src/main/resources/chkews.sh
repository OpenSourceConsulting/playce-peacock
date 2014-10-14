#!/bin/bash
#ENV_SH=( `locate --regex "Servers\/\w+(Server(|[0-9]+))\/env.sh" | grep -v code | grep -v t6` )
#SERVER_XML=( `locate --regex "Servers\/\w+(Server(|[0-9]+))\/conf\/server.xml" | grep -v code | grep -v t6` )
#CONTEXT_XML=( `locate --regex "Servers\/\w+(Server(|[0-9]+))\/conf\/context.xml" | grep -v code | grep -v t6` )
#ENV_SHS=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/env.sh" 2> /dev/null | grep -v code | grep -v t6` )
#SERVER_XML=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/conf\/server.xml" 2> /dev/null | grep -v code | grep -v t6` )
#CONTEXT_XML=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/conf\/context.xml" 2> /dev/null | grep -v code | grep -v t6` )

ENV_SHS=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/env.sh" 2> /dev/null | grep -v code | grep -v t6` )

COUNT=0
for i in "${ENV_SHS[@]}"
do
    TMP=( `cat $i | grep CATALINA_HOME=` )

    if [ ! $TMP ] ; then
        continue
    else
        VER=''
        SU=''
        SH=''
        SN=''
        CH=''
        CB=''
        STAC=''
        STOC=''
        SX=''
        CX=''

        VER=`cat $i | grep CATALINA_HOME= | grep -v echo | awk '{ print substr($2, length($2)-6, 7); }'`
        SU=`cat $i | grep SERVER_USER= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        SH=`cat $i | grep SERVER_HOME= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        SN=`cat $i | grep SERVER_NAME= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        echo $SU" "$SN > tmp.info
        SN=`cat tmp.info | awk '{ sub(/SERVER_USER/, $1, $2); print $2 }' | sed -E 's/([${}])//g'`
        CH=`cat $i | grep CATALINA_HOME= | grep -v echo | awk '{ print substr($2, 15, length($2)-14); }'`
        CB=`cat $i | grep CATALINA_BASE= | grep -v echo | awk '{ print substr($2, 15, length($2)-14); }'`
        echo $SH" "$SN" "$CH" "$CB > tmp.info
        CH=`cat tmp.info | awk '{ sub(/SERVER_HOME/, $1, $3); print $3 }' | sed -E 's/([${}])//g'`
        CB=`cat tmp.info | awk '{ sub(/SERVER_HOME/, $1, $4); print }' | awk '{ sub(/SERVER_NAME/, $2, $4); print $4 }' | sed -E 's/([${}])//g'`
        rm -f tmp.info

        if [ $CB ] ; then
            SX=( `find $CB -regextype egrep -regex ".*/conf\/server.xml" 2> /dev/null` )
            CX=( `find $CB -regextype egrep -regex ".*/conf\/context.xml" 2> /dev/null` )
        fi

        JBOSSEWS=`chkconfig --list | grep -o jbossews`

        if [ $JBOSSEWS ] ; then
            STAC="service jbossews start"
            STOC="service jbossews stop"
        else
            STAC="runuser -l ${SERVER_USER} -c 'cd ${CATALINA_BASE} && sh startNode.sh notail'"
            STOC="runuser -l ${SERVER_USER} -c 'cd ${CATALINA_BASE} && sh kill.sh'"
        fi

        COUNT=$(($COUNT+1))
        ENV_SH=$ENV_SH","$i
        SERVER_XML=$SERVER_XML","$SX
        CONTEXT_XML=$CONTEXT_XML","$CX
        VERSION=$VERSION","$VER
        SERVER_USER=$SERVER_USER","$SU
        SERVER_HOME=$SERVER_HOME","$SH
        SERVER_NAME=$SERVER_NAME","$SN
        CATALINA_HOME=$CATALINA_HOME","$CH
        CATALINA_BASE=$CATALINA_BASE","$CB
        START_CMD=$START_CMD","$STAC
        STOP_CMD=$STOP_CMD","$STOC
    fi
done

echo "COUNT="$COUNT
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