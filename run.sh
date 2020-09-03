#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Invalid number of arguments: USAGE: ./run.sh [CIM|OLA]"
    exit 1
fi
project=$1

mvn clean package

if [ $project == "OLA" ]; then
    cd LatticeAutomata
    ./run.sh
    echo Bye bye...
    exit 0
elif [ $project == "CIM" ]; then
    cd CellIndexMethod
    ./run.sh
    echo Adios
    exit 0
fi
echo "Not found"
exit 1

