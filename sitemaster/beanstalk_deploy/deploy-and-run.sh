#!/bin/sh
export JAVA_OPTIONS="$JAVA_OPTIONS -Djava.security.egd=file:/dev/./urandom"
mongod --smallfiles &
/usr/bin/env bash /opt/jetty/bin/jetty.sh run