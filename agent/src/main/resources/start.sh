#!/bin/sh
PID=`ps -ef | grep java | grep "com.athena.peacock.agent.Starter" | awk '{print $2}'`

if [ e$PID != "e" ] ; then
    echo "Peacock Client(Agent) is already RUNNING..."
    exit;
fi

nohup java -cp .:lib/* com.athena.peacock.agent.Starter > /dev/null 2>&1 &
sleep 1
tail -f log/athena-peacock-agent.log
