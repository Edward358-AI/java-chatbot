# pylint: disable-all

import os

def count_lines_in_file(file_path: str) -> int:
    try:
        with open(file_path, "r", encoding = "utf-8") as file:
            return sum(1 for line in file if line.strip())
    except Exception as e:
        print(f"Error reading file {file_path}: {e}")
        return 0

def count_lines_in_java_files(root_dir: str) -> tuple[int, list[tuple[str, int]]]:
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

def main() -> None:
    root_dir = "."
    total_lines, java_files = count_lines_in_java_files(root_dir)

    print(f"Total lines in all .java files: {total_lines}\n")
    print("Lines in each .java file:")
    for file_path, lines in java_files:
        print(f"{file_path}: {lines} lines")

if __name__ == "__main__":
    main()
