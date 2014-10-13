#!/bin/bash
ENV_SHS=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/env.sh" 2> /dev/null | grep -v code | grep -v t6` )
#DS_XML=( `find /home* -regextype egrep -regex ".*/apps\/([^ ]*)-ds.xml" 2> /dev/null` )
#LOGIN_CONFIG_XML=( `find /home* -regextype egrep -regex ".*/Servers\/\w+(Server(|[0-9]+))\/conf\/login-config.xml" 2> /dev/null | grep -v code | grep -v t6` )

COUNT=0
for i in "${ENV_SHS[@]}"
do
    TMP=( `cat $i | grep JBOSS_HOME=` )
    if [ ! $TMP ] ; then
    	continue
    else
    	USER_HOME=''
    	VER=''
    	JD=''
    	JH=''
    	JU=''
    	SH=''
    	SN=''
    	STAC=''
    	STOC=''
    	
        VER=`cat $i | grep JBOSS_HOME= | grep -v echo | awk '{ print substr($2, length($2)-12, 13); }'`
        JD=`cat $i | grep JBOSS_DIR= | grep -v echo | awk '{ print substr($2, 11, length($2)-10); }'`
        JH=`cat $i | grep JBOSS_HOME= | grep -v echo | awk '{ print substr($2, 12, length($2)-11); }'`
        JU=`cat $i | grep JBOSS_USER= | grep -v echo | awk '{ print substr($2, 12, length($2)-11); }'`
        SH=`cat $i | grep SERVER_HOME= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
        SN=`cat $i | grep SERVER_NAME= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`

        if [ $JD ] ; then
            echo $JD" "$JU" "$SN" "$JH" "$SH > tmp.info
            SN=`cat tmp.info | awk '{ sub(/JBOSS_USER/, $2, $3); print $3 }' | sed -E 's/([${}])//g'`
            JH=`cat tmp.info | awk '{ sub(/JBOSS_DIR/, $1, $4); print $4 }' | sed -E 's/([${}])//g'`
            SB=`cat tmp.info | awk '{ sub(/JBOSS_DIR/, $1, $5); print $5 }' | sed -E 's/([${}])//g'`
            SH=$JD
            USER_HOME=$JD
        else
            echo $JU" "$SN > tmp.info
            SN=`cat tmp.info | awk '{ sub(/JBOSS_USER/, $1, $2); print $2 }' | sed -E 's/([${}])//g'`
            SB=`cat $i | grep SERVER_BASE= | grep -v echo | awk '{ print substr($2, 13, length($2)-12); }'`
            USER_HOME=$SH
        fi

        rm -f tmp.info
        
        DX=( `find $USER_HOME -regextype egrep -regex ".*/apps\/([^ ]*)-ds.xml" 2> /dev/null` )
        LCX=( `find $SB -regextype egrep -regex ".*/\w+(Server(|[0-9]+))\/conf\/login-config.xml" 2> /dev/null` )

        JBOSS_INIT=`chkconfig --list | grep -o jboss_init`

        if [ $JBOSS_INIT ] ; then
            STAC="service jboss_init start"
            STOC"service jboss_init stop"
        else
            STAC="runuser -l "${JU}" -c 'cd ${SB}/${SN} && sh startNode.sh notail'"
            STOC="runuser -l ${JU} -c ‘cd ${SB}/${SN} && sh kill.sh'"
        fi
    
	    COUNT=$(($COUNT+1))
	    ENV_SH=$ENV_SH","$i
        DS_XML=$DS_XML","$DX
        LOGIN_CONFIG_XML=$LOGIN_CONFIG_XML","$LCX
        VERSION=$VERSION","$VER
        JBOSS_DIR=$JBOSS_DIR","$JD
        JBOSS_HOME=$JBOSS_HOME","$JH
        JBOSS_USER=$JBOSS_USER","$JU
        SERVER_HOME=$SERVER_HOME","$SH
        SERVER_NAME=$SERVER_NAME","$SN
        SERVER_BASE=$SERVER_BASE","$SB
        START_CMD=$START_CMD","$STAC
        STOP_CMD=$STOP_CMD","$STOC
	    
    fi
done

echo "COUNT="$COUNT
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