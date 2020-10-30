#!/bin/bash
set -e

GREEN='\033[0;34m'
NORMAL='\033[0m'

function check_visualization_dir() {
    if [ ! -d "$1" ]; then
      mkdir "$1"
    fi
}

echo -e "\n\n${GREEN}Running Project Mars with arguments: $* ${NORMAL}\n\n"

mvn clean package
FILE_PATH="src/visualization"
check_visualization_dir "$FILE_PATH"
FILE_NAME="pedestrian_output.xyz"
java -jar target/PedestrianDynamicsObstacles-1.0-SNAPSHOT.jar "$@" > "$FILE_PATH/$FILE_NAME"