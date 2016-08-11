@ECHO OFF
::::::::::::::::::::::::::::::::::::::::::::::::::::
:: File Name: auto-kill-start-run.bat
:: Author: Ajay Singh (ajay.ksingh@hp.com)
::
:: Description: 
:: This will start automation.bat
::
:: Takes following arguments (specify ALL and maintain the order):
:: 1. SPPLocation = Specify the location of the SPP
:: 2. HpsumKillLogsDelete = Specify if you want to kill any existing HPSUM service and delete the existing HPSUM logs.[0=No,1=Yes]
:: 3. HpsumServiceStart = Specify if you want to start HPSUM Service - it will start from the SPP location you specified.[0=No,1=Yes]
:: 

set SPPLocation=C:\SPP\GUI-AUTOMATION
set HpsumKillLogsDelete=0
set HpsumServiceStart=0

START 2-automation.bat %SPPLocation% %HpsumKillLogsDelete% %HpsumServiceStart%

::pause
