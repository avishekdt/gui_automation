::RUN.BAT
@echo OFF

REM ******* PROVIDE THE VALUES ********************************************
set input_copyLogsToRemoteShare=0
set input_remoteShareLocation=\\15.154.112.188\HPSUM_Logs
set input_remoteShareLocationFolder=Automation-Logs\GUI-Automation
set input_remoteShareUser=ASIAPACIFIC\hpdvt-user
set input_remoteSharePwd=Welcome123!
set input_fromEmailAddrConsMail=automation-hpsum@hp.com
set input_toEmailAddrConsMail=ajay.ksingh@hp.com
REM ***********************************************************************

REM TimeStamp folder will be created in the below path and logs will be placed in it.
set remoteShareLogsPath=%input_remoteShareLocation%\%input_remoteShareLocationFolder%
set localLogDir=logs

set LogFileName=Automation-Run.log

for /f "delims=[] tokens=2" %%a in ('ping -4 %computername% -n 1 ^| findstr "["') do (set thisip=%%a)
echo --- Automation Server IP : %thisip%

del GUI-Automation.html

cd xml
del MAT.xml
copy MAT-Windows.xml MAT.xml
cd..

FOR /F "tokens=*" %%A IN ('DATE/T') DO FOR %%B IN (%%A) DO SET Today=%%B
REM ECHO It's %Today% today

FOR /F "tokens=1-3 delims=/-" %%A IN ("%Today%") DO (
    SET DayMonth=%%A
    SET MonthDay=%%B
    SET Year=%%C
)
REM ECHO YYYYMMDD is  %Year%%DayMonth%%MonthDay%
set MyDate=%Year%%DayMonth%%MonthDay%

set MyTime=%time:~,2%%time:~3,2%%time:~6,2%
set MyTime=%MyTime: =0%
set MyTimeStamp=%MyDate%-%MyTime%
set MyTimeStamp=%MyTimeStamp: =%
set MyLogFile=%MyTimeStamp%_%LogFileName%

REM Create a variable to use by ANT - build.xml for copying the logs at the end.
set TIME_STAMP=%MyTimeStamp%

REM Rename the existing logs dir to logs_timestamp -
REM this will make sure that old logs are not copied in the remote share
IF exist %localLogDir% (ren %localLogDir% %localLogDir%_%MyTimeStamp%)

REM Create a new logs dir
IF not exist %localLogDir% (mkdir %localLogDir%)

REM echo Before Creating Folder
echo --- Current timestamp: %MyTimeStamp%
set remoteShareLogsPathWithTS=%remoteShareLogsPath%\%MyTimeStamp%\
set localLogsLocation=.\logs\

REM if remote logs copying is NOT required then skip it.
IF %input_copyLogsToRemoteShare% NEQ 0 (
  net use * /delete /y
  net use  %input_remoteShareLocation% %input_remoteSharePwd% /user:%input_remoteShareUser%  /p:yes

  REM Create the remote logs folder
  mkdir %remoteShareLogsPathWithTS%
  echo --- Created remote logs folder: %remoteShareLogsPathWithTS%  
) ELSE (
  echo --- Value of input_copyLogsToRemoteShare: %input_copyLogsToRemoteShare%
  echo --- Logs will not be copied to Remote Share
)

echo ------- IGNORE THE ABOVE ERRORS IF ANY -------
echo --- Local logs folder: %localLogDir%

REM start the execution
echo --- Check email from %input_fromEmailAddrConsMail% for consolidated test results.
echo --- Consolidated email will be sent to: %input_toEmailAddrConsMail% 
echo --- (Picked from RUN.BAT file)
echo --- Individual test emails will be sent to ids provided in :
echo --- config.properties file
echo --- Log file: %localLogDir%\%MyLogFile%
echo --- Question regarding automation? Contact: ajay.ksingh@hp.com
echo --- STARTING THE EXECUTION. THIS WINDOW WILL CLOSE WHEN EXECUTION COMPLETES.

ant sendMail > %localLogDir%\%MyLogFile%
echo Execution complete.

REM call Robocopy.exe .\logs %input_remoteShareLocation%\Automation-Logs\GUI-Automation\%MyTimeStamp%\ *.* /E
REM This is moved to ANT now.

pause
