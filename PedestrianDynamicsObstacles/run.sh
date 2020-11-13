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
  echo "Usage: $0 <num_of_obstacles> deltaT deltaT2 [OBSERVABLE_VARIABLE]"
  exit 1
fi

dmin=( "0.2" "0.3" "0.4" "0.5" "0.6" "0.7" "0.8" "0.9")
dmid=( "1.0" "1.1" "1.2" "1.3" "1.4" "1.5" "1.6" "1.7" "1.8" "1.9")
rad=( "0.2" "0.25" "0.3" "0.35" "0.4" "0.45" "0.5" "0.55" "0.6")

if [ "$4" == "DMIN" ]; then
  echo "lel"
  observable=(${dmin[@]})
  iterations=${#observable[@]}
elif [ "$4" == "DMID" ]; then
  observable=(${dmid[@]})
  iterations=${#observable[@]}
elif [ "$4" == "RAD" ]; then
  observable=(${rad[@]})
  iterations=${#observable[@]}
else
  observable=-1
  iterations=1
fi

echo "${observable[@]}"
echo "$iterations"

echo -e "\n\n${GREEN}Running Pedestrian Dynamics with arguments: $* ${NORMAL}\n\n"

mvn clean package
FILE_PATH="src/visualization"
check_visualization_dir "$FILE_PATH"

start=$(date +%s)
echo TIME START: $start 

if [ "$iterations" -eq 1 ]; then
    FILE_NAME="pedestrian_output.xyz"
    java -jar target/PedestrianDynamicsObstacles-1.0-SNAPSHOT.jar "$1" "$2" "$3" > "$FILE_PATH/$FILE_NAME"
	ovito "$FILE_PATH/$FILE_NAME"
else
    if [ ! -d "$FILE_PATH/bulk" ]; then
        mkdir "$FILE_PATH/bulk"
    fi
    repetitions=10
    for i in $(seq "$iterations"); do
       for j in $(seq "$repetitions"); do
          FILE_NAME="pedestrian_output_${observable[i-1]}_$j.xyz"
          java -jar target/PedestrianDynamicsObstacles-1.0-SNAPSHOT.jar "$1" "$2" "$3" "$4" "${observable[i-1]}" > "$FILE_PATH/bulk/$FILE_NAME"
          echo "$FILE_NAME"
       done
    done
fi

end=$(date +%s)
echo "TIME END $end"

echo "DELTA: $((end - start))"
