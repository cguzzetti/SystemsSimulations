#!/bin/bash

set -e

GREEN='\033[0;34m'
NORMAL='\033[0m'

function prompt_solution_visualization() {
    read -p "Do you want to run the visualization?[yN]" yn
    case $yn in
      [Yy]* )
        python "$1" "$2" "$3";
        ;;
      [Nn]* )
        echo "Exiting..."
        exit
        ;;
      * )
        echo "Unrecognized option $yn, exiting..."
        ;;
    esac

}

if [ $# -lt 2 ]; then
  echo "Error, usage: ./run.sh (OVITO|ERROR|SOLUTION|MARS_PLANETS|FIND_LAUNCH|SHIP_VELOCITY) deltaT [deltaT2]"
  exit 1
fi
MODE="$1";

if [ "$MODE" != "OVITO" ] && [ "$MODE" != "ERROR" ] && [ "$MODE" != "SOLUTION" ] && [ "$MODE" != "MARS_PLANETS" ] && [ "$MODE" != "FIND_LAUNCH" ] && [ "$MODE" != "SHIP_VELOCITY" ]; then
  echo "$MODE is not a valid mode. Options are: OVITO, ERROR, SOLUTION, MARS_PLANETS, FIND_LAUNCH and SHIP_VELOCITY"
  exit 1
fi

echo -e "\n\n${GREEN}Running Project Mars with arguments: $* ${NORMAL}\n\n"

mvn clean package
INTERACTIVE=${4:-true}
FILE_PATH="src/main/java/ar/edu/itba/ss/g9/tp4/visualization"
FILE_NAME="mars_$1_$2.xyz"
java -jar target/ProjectMars-1.0-SNAPSHOT.jar "$@" > "$FILE_PATH/$FILE_NAME"

if [ "$MODE" != "MARS_PLANETS" ] && [ "$MODE" != "FIND_LAUNCH" ] && [ "$MODE" != "SHIP_VELOCITY" ] && [ $INTERACTIVE == true ]; then
  cd $FILE_PATH
  prompt_solution_visualization "visualizer.py" "$MODE" "$2"
fi
