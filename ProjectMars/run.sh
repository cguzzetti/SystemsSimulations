#!/bin/bash

GREEN='\033[0;34m'
NORMAL='\033[0m'

if [ $# -lt 2 ]; then
  echo "Error, usage: ./run.sh (OVITO|ERROR|SOLUTION) deltaT [deltaT2]"
  exit 1
fi
MODE="$1";

if [ "$MODE" != "OVITO" ] && [ "$MODE" != "ERROR" ] && [ "$MODE" != "SOLUTION" ]; then
  echo "$MODE is not a valid mode. Options are: OVITO, ERROR and SOLUTION"
  exit 1
fi

echo -e "\n\n${GREEN}Running Project Mars with arguments: $* ${NORMAL}\n\n"

mvn clean package

FILE_PATH="src/main/java/ar/edu/itba/ss/g9/tp4/visualization"
FILE_NAME="mars_output.txt"
java -jar target/ProjectMars-1.0-SNAPSHOT.jar "$@" > "$FILE_PATH/$FILE_NAME"
