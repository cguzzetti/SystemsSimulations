
args=`sed ':a;N;$!ba;s/\n/ /g' exec_arguments`
# Colors https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
GREEN='\033[0;34m'
NORMAL='\033[0m'
echo -e "\n\n${GREEN} Running OLA with arguments: $args ${NORMAL}\n\n"
java -Dfile.encoding=UTF-8 -classpath target/classes:../commons/target/classes:$HOME/.m2/repository/commons-cli/commons-cli/1.4/commons-cli-1.4.jar:../CellIndexMethod/target/classes ar.edu.itba.ss.g9.tp2.Main $args