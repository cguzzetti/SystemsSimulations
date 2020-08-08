
compile() {
    # https://hub.docker.com/_/openjdk
    docker build -t ss$2 ../$1 
}


run() {
    docker run -t --rm ss$1 
}


main() {

    if [ $# -ne 2 ] && [ $# -ne 3 ]; then
        echo "The script needs 1 positional argument"
        echo "Usage 'docker-helper.sh (compile|run) tp[tp num] [ImageName]'"
        exit 1
    fi
    command="$1"
    shift
    identifier="$1"
    tp_title="$2"

    case "$command" in
    "compile")
        echo "Compiling Docker image for $tp_title"
        compile $tp_title $identifier
        ;;
    "run")
        echo "Running Docker container with name ss$identifier"
        run $identifier
        ;;
    *)
        echo "Couldn't understand command $command $@"
        exit 1
        ;;
    esac
}

main "$@"
