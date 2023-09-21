@echo off
echo Compiling Server.java...
javac Server.java

if errorlevel 1 (
    echo Compilation failed.
) else (
    echo Compilation successful.
    echo Running Server...
    java Server
)

pause
