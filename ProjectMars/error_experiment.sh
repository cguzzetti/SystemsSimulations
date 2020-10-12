#!/bin/bash

deltas=( "0.0000001" "0.000001" "0.00001" "0.0001" "0.001" )
for i in "${deltas[@]}"; do
    ./run.sh "OVITO" "$i" "0.002" false
done
