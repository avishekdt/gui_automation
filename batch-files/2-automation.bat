@ECHO OFF
::::::::::::::::::::::::::::::::::::::::::::::::::::
:: File Name: automation.bat
:: Author: Ajay Singh (ajay.ksingh@hp.com)
::
:: Description:
:: This file/tool gets called from auto-kill-start-run.bat which passes the parameters for
:: killing and starting the HPSUM service. This calls start-hpsum-service.bat to start the
:: HPSUM service. It then starts the execution using ant command and after the execution 
:: collects the HPSUM logs by calling gatherlogs.bat
:: 
:: MainPath 	= the path to the folder where build.xml is located
:: LogPath		= results logs file will be saved here.
:: MyLogFolder 	= TimeStamp folder inside logs folder.
:: MyLogFile 	= log file - used for debugging and seeing the result
:: MyLogFolderTrackFile = Stores the location/name of MyLogFolder - used  by selenium to place logs
::
:: ***CAUTION***
:: Make sure the date-time format on the system is in US-English format. MM/DD/YYYY
:: If not you might see some issue while creating the log directory
:: The script may even do not work at all.
:: You can change the format from Control Panel - Clock, Language and Region settings
:: 
::::::::::::::::::::::::::::::::::::::::::::::::::::
setlocal
set SPPLocation=%1%
set HpsumKillLogsDelete=%2%
set HpsumServiceStart=%3%

set MainPath=C:\automation_gui\gui_automation
set LogPath=%MainPath%\logs

set LogFileName=Automation-Run.log
set HpsumLocation=%SPPLocation%\hp\swpackages
set GatherLogsFile=%HpsumLocation%\gatherlogs.bat

set MyDate=%date:~-4%%date:~,2%%date:~3,2%
set MyTime=%time:~,2%%time:~3,2%%time:~6,2%
set MyTimeStamp=%MyDate%-%MyTime%

set MyLogFolder=%LogPath%\%MyTimeStamp%
set MyLogFile=%MyLogFolder%\%MyTimeStamp%_%LogFileName%
set MyLogFolderTrackFile=%MainPath%\LogFolderTimeStamp.txt

:: Create a timestamp folder inside logs folder (C:\automation_gui\gui_automation\logs)
IF NOT EXIST %LogPath% MKDIR %LogPath%
IF NOT EXIST %MyLogFolder% MKDIR %MyLogFolder%
echo LOGS FOLDER: %MyLogFolder%

:: Store the name of LogFolder in Tracking file - used  by selenium to place logs
echo %MyLogFolder%>%MyLogFolderTrackFile%

::Note that the quotes are REQUIRED around %MyLogFIle% in case it contains a space
IF NOT EXIST "%MyLogFile%" goto:noLogFile
echo.>>"%MyLogFile%"
echo.===================>>"%MyLogFile%"
:noLogFile
echo "Date and Time of start of script:" >>"%MyLogFile%"
echo.%Date% >>"%MyLogFile%"
echo.%Time:~,8% >>"%MyLogFile%"

echo.>>"%MyLogFile%"
echo.================================================>>%MyLogFile%

:: Don't delete the logs if HpsumLogsDelete=0
If %HpsumKillLogsDelete%==0 goto SkipHpsumKillDeleteLogs
>> %MyLogFile% taskkill /f /IM hpsum_service*  2>&1 | echo taskkill done
>> %MyLogFile% rmdir /S/Q C:\Users\ADMINI~1\AppData\Local\Temp\1\HPSUM 2>&1 | echo temp1 removed done
>> %MyLogFile% rmdir /S/Q C:\Users\ADMINI~1\AppData\Local\Temp\2\HPSUM 2>&1 | echo temp2 removed done

:SkipHpsumKillDeleteLogs
REM Echo "Did not delete the old HPSUM logs and also did not kill the HPSUM service" >> "%MyLogFile%"

echo.>>"%MyLogFile%"
echo.================================================>>%MyLogFile%

If %HpsumServiceStart%==0 goto SkipHpsumServiceStart
echo "Starting HPSUM Service: start-hpsum-service.bat" >>"%MyLogFile%"
cd
START 3-start-hpsum-service.bat %SPPLocation%

:SkipHpsumServiceStart
REM Echo "Did not Start HPSUM service" >> "%MyLogFile%"

echo.>>"%MyLogFile%"
echo.================================================>>%MyLogFile%

:: Start the Test Execution
>> %MyLogFile% cd
>> %MyLogFile% cd %MainPath%
>> %MyLogFile% cd

echo.>>"%MyLogFile%"
echo "Starting Execution: ant clean compile run makexsltreports" >>"%MyLogFile%"
echo.>>"%MyLogFile%"
>> %MyLogFile% ant clean compile run makexsltreports 2>&1 | echo ant clean compile run makexsltreports done

timeout 20

:: Collect Logs after the test execution.
echo.>>"%MyLogFile%"
echo.================================================>>%MyLogFile%
REM START %GatherLogsFile%
echo %GatherLogsFile% executed >> %MyLogFile% 

:: Copy the gathered logs in current logs directory.
timeout 20
echo.>>"%MyLogFile%"
echo.================================================>>%MyLogFile%

echo Current Directory::>> %MyLogFile% 
>> %MyLogFile% cd

echo Change to Log Directory.>> %MyLogFile% 
>> %MyLogFile% cd %MyLogFolder%

echo Current Directory::>> %MyLogFile% 
>> %MyLogFile% cd

echo Zip files present in %HpsumLocation%::>> %MyLogFile% 
>> %MyLogFile% dir /B %HpsumLocation%\*.zip

echo Copy zip files to Current directory::>> %MyLogFile% 
>> %MyLogFile% copy %HpsumLocation%\*.zip .

echo Current Directory contents::>> %MyLogFile% 
>> %MyLogFile% dir /B

echo Deleting Zip files in HPSUM Location.>> %MyLogFile% 
>> %MyLogFile% del %HpsumLocation%\*.zip

echo.>>"%MyLogFile%"
echo.================================================>>%MyLogFile%

echo "Date and Time of completion of script:" >>"%MyLogFile%"
echo.%Date% >>"%MyLogFile%"
echo.%Time:~,8% >>"%MyLogFile%"

echo.>>"%MyLogFile%"
echo.================================================>>%MyLogFile%


:: Script complete.
echo =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
echo SCRIPT COMPLETE - SEE LOGS AT: %MyLogFolder%

