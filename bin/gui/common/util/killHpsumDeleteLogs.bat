REM
REM File-name: killHpsumDeleteLogs.bat
REM Does following:
REM - Kills any existing HPSUM Service
REM - Delete %TEMP% - HPSUM directory
REM - Delete C:\cpqsystem directory - CODE COMMENTED AS OF NOW
REM

taskkill /f /IM hpsum_service*			
rmdir /S/Q C:\Users\\Administrator\AppData\Local\Temp\1\HPSUM
rmdir /S/Q C:\Users\\Administrator\AppData\Local\Temp\2\HPSUM
rmdir /S/Q C:\Users\\Administrator\AppData\Local\Temp\HPSUM
cd %temp%
rmdir HPSUM

REM rmdir /S/Q C:\cpqsystem

echo "killHpsumDeleteLogs complete."

pause
