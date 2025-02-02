#!/bin/bash

args=`sed ':a;N;$!ba;s/\n/ /g' exec_arguments`
GREEN='\033[0;34m'
NORMAL='\033[0m'
echo -e "\n\n${GREEN}Running GAS  with arguments: $args${NORMAL}\n\n"

LIBPATH="lib/openjfx-11.0.2_linux-x64_bin-sdk"
java -Djava.library.path=$LIBPATH/javafx-sdk-11.0.2/lib -Dfile.encoding=UTF-8 -classpath target/classes:$LIBPATH/javafx-sdk-11.0.2/lib/src.zip:$LIBPATH/javafx-sdk-11.0.2/lib/javafx-swt.jar:$LIBPATH/javafx-sdk-11.0.2/lib/javafx.web.jar:$LIBPATH/javafx-sdk-11.0.2/lib/javafx.base.jar:$LIBPATH/javafx-sdk-11.0.2/lib/javafx.fxml.jar:$LIBPATH/javafx-sdk-11.0.2/lib/javafx.media.jar:$LIBPATH/javafx-sdk-11.0.2/lib/javafx.swing.jar:$LIBPATH/javafx-sdk-11.0.2/lib/javafx.controls.jar:$LIBPATH/javafx-sdk-11.0.2/lib/javafx.graphics.jar:$HOME/.m2/repository/commons-cli/commons-cli/1.4/commons-cli-1.4.jar:../commons/target/classes:$HOME/.m2/repository/org/openjdk/jmh/jmh-core/1.19/jmh-core-1.19.jar:$HOME/.m2/repository/net/sf/jopt-simple/jopt-simple/4.6/jopt-simple-4.6.jar:$HOME/.m2/repository/org/apache/commons/commons-math3/3.2/commons-math3-3.2.jar:$HOME/.m2/repository/org/openjdk/jmh/jmh-generator-annprocess/1.19/jmh-generator-annprocess-1.19.jar:$HOME/.m2/repository/org/slf4j/slf4j-api/1.7.12/slf4j-api-1.7.12.jar:$HOME/.m2/repository/org/slf4j/slf4j-log4j12/1.7.12/slf4j-log4j12-1.7.12.jar:$HOME/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:$HOME/.m2/repository/org/slf4j/jcl-over-slf4j/1.7.12/jcl-over-slf4j-1.7.12.jar ar.edu.itba.ss.g9.tp3.Main -N 100 -f input.txt -o output.xyz

