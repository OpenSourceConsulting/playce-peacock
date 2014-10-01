#!/bin/bash
HTTPD=( `find /etc/init.d/ -name "*_httpd"` )

COUNT=${#HTTPD[@]}

for i in "${HTTPD[@]}"
do
    JUN=(`cat $i | grep JBOSS_USER_NAME= | awk '{ print substr($1, 17, length($1)-16); }'` )
    AH=( `cat $i | grep apachectl= | awk '{ print substr($1, 11, length($1)-25); }'` )
    HC=( `cat $i | egrep -o 'httpd.{0,5}(.*\/)([^\/]*)\/conf\/httpd.conf' | awk '{ n = split($0, arr, " "); } END { print $n }'` )

    if [ $JUN ] ; then
      AH=`echo $JUN" "$AH | awk '{ sub(/JBOSS_USER_NAME/, $1, $2); print $2 }' | sed -E 's/([${}])//g'`
      HC=`echo $JUN" "$HC | awk '{ sub(/JBOSS_USER_NAME/, $1, $2); print $2 }' | sed -E 's/([${}])//g'`
    fi

    SN=`echo $i | awk '{ print substr($1, 13, length($1)-12); }'`

    SH=''
    HIC=''
    SC=''
    URI=''
    WORK=''
    if [ $HC ] ; then
       SH=`echo $HC | awk '{ print substr($1, 0, length($1)-16); }'`
   
       if [ $SH ] ; then
           HIC=( `find $SH -name httpd-info.conf 2> /dev/null` )
           SC=( `find $SH -name *ssl.conf 2> /dev/null` )
           URI=( `find $SH -name uriworkermap.properties 2> /dev/null` )
           WORK=( `find $SH -name workers.properties 2> /dev/null` )
       fi
    fi

    STAC="service $SN start"
    STOC="service $SN stop"

   APACHE_HOME=$APACHE_HOME","$AH
   SERVER_HOME=$SERVER_HOME","$SH
   HTTPD_CONF=$HTTPD_CONF","$HC
   HTTPD_INFO_CONF=$HTTPD_INFO_CONF","$HIC
   SSL_CONF=$SSL_CONF","$SC
   URIWORKERMAP=$URIWORKERMAP","$URI
   WORKERS=$WORKERS","$WORK
   START_CMD=$START_CMD","$STAC
   STOP_CMD=$STOP_CMD","$STOC
done

echo "COUNT"=$COUNT
echo "APACHE_HOME="$APACHE_HOME
echo "SERVER_HOME="$SERVER_HOME
echo "HTTPD_CONF="$HTTPD_CONF
echo "HTTPD_INFO_CONF="$HTTPD_INFO_CONF
echo "SSL_CONF="$SSL_CONF
echo "URIWORKERMAP="$URIWORKERMAP
echo "WORKERS="$WORKERS
echo "START_CMD="$START_CMD
echo "STOP_CMD="$STOP_CMD