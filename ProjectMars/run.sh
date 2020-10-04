#!/bin/bash

args=$(sed ':a;N;$!ba;s/\n/ /g' exec_arguments)
GREEN='\033[0;34m'
NORMAL='\033[0m'
echo -e "\n\n${GREEN}Running Project Mars with arguments: $args${NORMAL}\n\n"

mvn clean package
java -jar target/ProjectMars-1.0-SNAPSHOT.jar "$@" > mars_output.xyz
