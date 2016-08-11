@echo OFF

REM :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
REM : Call this batch script with following arguments
REM : remote_mc_type - mention the OS of the remote machine. Valid values: windows, linux
REM : remote_mc_ip   - mention the IP of the remote machine. Example: 15.154.114.19
REM : remote_mc_user - mention the admin user name of the remote machine. Example: Administrator, root
REM : remote_mc_pwd  - mention the admin password of the remote machine. Example: 12iso*help, iso*help
REM : Example:
REM : kill-remote-hpsum.bat windows 15.154.114.19 Administrator 12iso*help
REM :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

set remote_mc_type=%1
set remote_mc_ip=%2
set remote_mc_user=%3
set remote_mc_pwd=%4

echo -------------------- >> kill-remote-hpsum.log

REM : Get Timestamp
FOR /F "tokens=*" %%A IN ('DATE/T') DO FOR %%B IN (%%A) DO SET Today=%%B
FOR /F "tokens=1-3 delims=/-" %%A IN ("%Today%") DO (
    SET DayMonth=%%A
    SET MonthDay=%%B
    SET Year=%%C
)
set MyDate=%Year%%DayMonth%%MonthDay%
set MyTime=%time:~,2%%time:~3,2%%time:~6,2%
set MyTime=%MyTime: =0%
set MyTimeStamp=%MyDate%-%MyTime%
set MyTimeStamp=%MyTimeStamp: =%
echo %MyTimeStamp% >> kill-remote-hpsum.log

REM : Start the process
echo ENTERED VALUES: %remote_mc_type% , %remote_mc_ip% , %remote_mc_user% , %remote_mc_pwd% >> kill-remote-hpsum.log

If "%remote_mc_type%"=="linux" goto OSLINUX
echo WORKING ON WINDOWS MACHINE >> kill-remote-hpsum.log
taskkill.exe /S %remote_mc_ip% /U %remote_mc_user% /P %remote_mc_pwd% /IM hpsum* >> kill-remote-hpsum.log
PsExec \\%remote_mc_ip% -u %remote_mc_user% -p %remote_mc_pwd% cmd /c "rmdir /S /Q "C:\cpqsystem\hp" "C:\Users\Administrator\AppData\Local\Temp\1\HPSUM" "C:\Users\Administrator\AppData\Local\Temp\HPSUM" "C:\Users\Administrator\AppData\Local\Temp\2\HPSUM"" >> kill-remote-hpsum.log

:OSLINUX
If "%remote_mc_type%"=="windows" goto END
echo WORKING ON LINUX MACHINE >> kill-remote-hpsum.log
echo y | plink -v %remote_mc_user%@%remote_mc_ip% -pw %remote_mc_pwd% "pkill -f hpsum" >> kill-remote-hpsum.log
plink -v %remote_mc_user%@%remote_mc_ip% -pw %remote_mc_pwd% "rm -rf /tmp/HPSUM" >> kill-remote-hpsum.log
plink -v %remote_mc_user%@%remote_mc_ip% -pw %remote_mc_pwd% "rm -rf /var/hp/log/" >> kill-remote-hpsum.log

:END
echo Killed the remote HP SUM instance, if any. >> kill-remote-hpsum.log

