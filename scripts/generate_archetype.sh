if [ $# -ne 1 ]; then
    echo "The script needs 1 positional argument"
    echo "Usage 'generate_archetype.sh tp[tp num]'"
    exit 1
fi
cd ..
mvn archetype:generate -DgroupId=ar.edu.itba.grupo4.$1 -DartifactId=$1 -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
