#!/bin/zsh

# Check if the OS is macOS or UNIX-based
os_type=$(uname)

if [[ "$os_type" == "Darwin" ]]; then
    true
else
    echo "This script does not support an operating system of type: $os_type"
    exit 1
fi

# Navigate to the script's directory (relative to script location)
cd "$(dirname "$0")"

# Ensure the bin directory exists
if [ ! -d "./bin" ]; then
    mkdir -p ./bin
fi

# Remove old compiled files
rm -rf ./bin/*

# Enable globbing for recursive globbing in zsh (zsh supports globstar natively)
# Use zsh's ** globbing to match all .java files in the src directory recursively
javac -d ./bin ./src/**/*.java

# Run the Java application
java --enable-preview -cp ./bin App
