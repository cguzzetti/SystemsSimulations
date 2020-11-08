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
  echo "Usage: $0 <num_of_obstacles> deltaT deltaT2 [number_of_iterations]"
  exit 1
fi

iterations=${4:-1}
echo -e "\n\n${GREEN}Running Project Mars with arguments: $* ${NORMAL}\n\n"

mvn clean package
FILE_PATH="src/visualization"
check_visualization_dir "$FILE_PATH"
if [ "$iterations" -eq 1 ]; then
    FILE_NAME="pedestrian_output.xyz"
    java -jar target/PedestrianDynamicsObstacles-1.0-SNAPSHOT.jar "$@" > "$FILE_PATH/$FILE_NAME"
else
    if [ ! -d "$FILE_PATH/bulk" ]; then
        mkdir "$FILE_PATH/bulk"
    fi
    for i in $(seq "$iterations"); do
      FILE_NAME="pedestrian_output_$i.xyz"
      java -jar target/PedestrianDynamicsObstacles-1.0-SNAPSHOT.jar "$@" > "$FILE_PATH/bulk/$FILE_NAME"
      echo "$FILE_NAME"
    done
fi


