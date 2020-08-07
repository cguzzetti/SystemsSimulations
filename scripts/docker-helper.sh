
compile() {
    # https://hub.docker.com/_/openjdk
    docker build -t ss$1 ../$1 
}


run() {
    docker run -t --rm ss$1 
}


main() {

    if [ $# -ne 2 ]; then
        echo "The script needs 1 positional argument"
        echo "Usage 'docker-helper.sh (compile|run) tp[tp num]'"
        exit 1
    fi
    command="$1"
    shift
    tp_title="$1"

    case "$command" in
    "compile")
        echo "Compiling Docker image for $tp_title"
        compile $tp_title
        ;;
    "run")
        echo "Running Docker container with name ss$tp_title"
        run $tp_title
        ;;
    *)
        echo "Couldn't understand command $command $@"
        exit 1
        ;;
    esac
}

main "$@"