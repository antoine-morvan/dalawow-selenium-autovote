#!/bin/bash

if [ $# == 0 ] || [ "$1" == "--help" ]; then
  java -jar /var/autovote/target/selenium.test-0.0.1-SNAPSHOT-jar-with-dependencies.jar -h
  exit 0
fi

function find_free_display_number {
  USEDXDISPLAYS=`find /tmp -maxdepth 1 -type f -name ".X*-lock" | rev | cut -d"/" -f 1 | colrm 1 5 | rev | colrm 1 2`
  for i in {99..1}
  do
    FREE=YES
    for usedd in $USEDXDISPLAYS; do
      if [ "$usedd" == "$i" ]; then
        FREE=NO
        break
      fi
    done
    if [ "$FREE" == "YES" ]; then
      echo $i
      return
    fi
  done
}
if [ -x /usr/bin/Xvfb ]; then
  XDN=$(find_free_display_number)
  export DISPLAY=:${XDN}.0
  /usr/bin/Xvfb :${XDN} -ac -screen 0 1280x1024x16&
  XVFBPID=$!
fi

while true; do
	java -jar /var/autovote/target/selenium.test-0.0.1-SNAPSHOT-jar-with-dependencies.jar $@
	sleep $((3600 * 12 + 60 * 3))
done

if [ -x /usr/bin/Xvfb ]; then
  kill -2 $XVFBPID
fi
