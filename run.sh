#!/bin/zsh

os_type=$(uname)

if [[ "$os_type" == "Darwin" ]]; then
    true
else
    echo "This script does not support an operating system of type: $os_type"
    exit 1
fi

cd "$(dirname "$0")"

if [ ! -d "./bin" ]; then
    mkdir -p ./bin
fi

rm -rf ./bin/*

javac -d ./bin ./src/**/*.java

java --enable-preview -cp ./bin App
