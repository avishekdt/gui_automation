@echo OFF

set user_input_hpsum_version=%1
set user_input_hpsum_build=%2
set user_input_spp_version=%3
set user_input_spp_build=%4

set source_spp_path=\\15.154.112.188\HPSUM_Share\spp.base.iso
set destination_spp_path=C:\SPP\
set destination_extract_spp_path=C:\SPP\AUTOMATION
set dest_spp_packages_gen10=%destination_extract_spp_path%\packages
set dest_spp_packages=%destination_extract_spp_path%\hp\swpackages

set source_hpsum_path=\\15.154.112.188\HPSUM_Share\hpsum.application
set destination_hpsum_path=C:\SPP\
set destination_extract_hpsum_path=C:\SPP\

set programpath=%~dp0
echo %programpath%

net use * /delete /y
net use  \\15.154.112.188\HPSUM_Share 'Welcome123!' /user:ASIAPACIFIC\hpdvt-user  /p:yes

echo "Killing HPSUM instance and clearing logs"
::call KillHPSUM_clearlogs.bat

echo "Killing all browser instances"
::call test.bat

echo "Removing %destination_extract_spp_path%"
rmdir /s /q %destination_extract_spp_path%

echo "Removing %destination_extract_hpsum_path%"
rmdir /s /q %destination_extract_hpsum_path%

echo "Removing %destination_extract_hpsum_path%\sum"
rmdir /s /q %destination_extract_hpsum_path%\sum

echo "Copying SPP iso build: %user_input_spp_build%"
C:\Windows\System32\Robocopy.exe  %source_spp_path%\%user_input_spp_version%\%user_input_spp_build% %destination_spp_path% *%user_input_spp_build%.iso /E /COPY:DAT

echo "Extracting SPP to %destination_extract_spp_path%"
%programpath%7z.exe x -y -o%destination_extract_spp_path% %destination_spp_path%\*%user_input_spp_build%.iso

echo "Copying HP SUM zip build:%user_input_hpsum_build%"
C:\Windows\System32\Robocopy.exe  %source_hpsum_path%\%user_input_hpsum_version%\%user_input_hpsum_build% %destination_hpsum_path% *.zip /E /COPY:DAT

echo "Extracting HP SUM to %destination_extract_hpsum_path%"
%programpath%7z.exe x -o%destination_extract_hpsum_path% %destination_hpsum_path%\*.zip

echo "Copying HP SUM folder contents to SPP"
echo %user_input_spp_version%|findstr "Gen10*"
if %ERRORLEVEL% EQU 0 (
	set dest_spp_packages = %dest_spp_packages_gen10%
)

C:\Windows\System32\xcopy %destination_extract_hpsum_path%\sum\*  %dest_spp_packages% /y /s /i

echo Script copy-extract-hpsum-spp-win complete >> start.log

::Starting the automation
REM echo Ready to run GUI-Automation >> start.log
REM cd C:\automation\gui_automation
REM RUN.bat
REM echo Execution is complete. Good bye. >> start.log