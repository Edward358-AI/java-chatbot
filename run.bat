@echo off

ver | findstr /i "Microsoft" > nul
if errorlevel 1 (
    echo This script is intended to run on Windows only.
    exit /b 1
)

cd /d "%~dp0"

if not exist ".\bin" (
    mkdir .\bin
)

rd /s /q .\bin

mkdir .\bin

for /r .\src %%f in (*.java) do javac -d .\bin %%f

java --enable-preview -cp .\bin App
