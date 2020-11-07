#!/bin/bash
set -e

GREEN='\033[0;34m'
RED='\033[0;31m'
NORMAL='\033[0m'



function check_visualization_dir() {
    if [ ! -d "$1" ]; then
      mkdir "$1"
    fi
}

MIN_ARGS=3

if (( "$#" < "$MIN_ARGS" )); then
  echo -e "${RED}Error: $# is not a valid number of arguments. You need at least $MIN_ARGS${NORMAL}"
  echo "Usage: $0 <num_of_obstacles> deltaT deltaT2"
  exit 1
fi

echo -e "\n\n${GREEN}Running Project Mars with arguments: $* ${NORMAL}\n\n"

mvn clean package
FILE_PATH="src/visualization"
check_visualization_dir "$FILE_PATH"
FILE_NAME="pedestrian_output.xyz"
java -jar target/PedestrianDynamicsObstacles-1.0-SNAPSHOT.jar "$@" > "$FILE_PATH/$FILE_NAME"
