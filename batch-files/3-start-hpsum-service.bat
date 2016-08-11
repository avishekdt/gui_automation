@ECHO OFF
::::::::::::::::::::::::::::::::::::::::::::::::::::
:: File Name: start-hpsum-service.bat
:: Author: Ajay Singh (ajay.ksingh@hp.com)
::
:: Description:
:: This will start the HPSUM Service
:: This script should be called with an argument specifying SPP Location
:: Example: SPPLocation=C:\SPP\SPPGen8Snap6.2013_0820.22
::
:: Uncomment x86 if the current machine is 32-bit

set SPPLocation=%1%
echo %SPPLocation%
%SPPLocation%\hp\swpackages\x64\hpsum_service_x64.exe
:: %SPPLocation%\hp\swpackages\x86\hpsum_service_x86.exe

