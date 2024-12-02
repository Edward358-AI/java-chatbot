# pylint: disable-all

import os

def count_lines_in_file(file_path):
    """Counts the number of non-empty lines in a file."""
    try:
        with open(file_path, "r", encoding="utf-8") as file:
            return sum(1 for line in file if line.strip())  # Counts non-empty lines
    except Exception as e:
        print(f"Error reading file {file_path}: {e}")
        return 0

def count_lines_in_java_files(root_dir):
    """Counts the lines in all .java files in the root directory and its subdirectories."""
    total_lines = 0
    java_files = []

    for subdir, _, files in os.walk(root_dir):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(subdir, file)
                lines = count_lines_in_file(file_path)
                total_lines += lines
                java_files.append((file_path, lines))

    return total_lines, java_files

def main():
    root_dir = "."  # You can change this to a specific directory
    total_lines, java_files = count_lines_in_java_files(root_dir)

    print(f"Total lines in all .java files: {total_lines}\n")
    print("Lines in each .java file:")
    for file_path, lines in java_files:
        print(f"{file_path}: {lines} lines")

if __name__ == "__main__":
    main()
