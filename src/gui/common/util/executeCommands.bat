@echo off

if "%1" == "" (
    rem if %1 is blank there were no arguments. Show how to use this batch
    echo Usage: %0 filename ...
    exit /b
)

set full_command=

:again
rem if %1 is blank, we are finished
if not "%1" == "" (    
    set full_command=%full_command% %1%
    
    rem - shift the arguments and examine %1 again
    shift
    goto again
)

rem echo %full_command%
%full_command%

set full_command=
