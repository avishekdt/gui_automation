@echo off
@setlocal ENABLEDELAYEDEXPANSION
@setlocal enableextensions enabledelayedexpansion

net use * /delete /y
net use \\15.154.112.188\automation\gui 'Welcome123!' /user:ASIAPACIFIC\hpdvt-user  /p:yes

echo %date% %time% ----- Scheduler started ----- >> start.log

set winFile=\\15.154.112.188\automation\gui\main_hpsum_win.txt
set snap2File=\\15.154.112.188\automation\gui\Gen9Snap2.txt
set snap3File=\\15.154.112.188\automation\gui\Gen9Snap3.txt
set snap4File=\\15.154.112.188\automation\gui\Gen9Snap4.txt
set snap5File=\\15.154.112.188\automation\gui\Gen9Snap5.txt
set snap6File=\\15.154.112.188\automation\gui\Gen9Snap6.txt
REM Add files here for other SPPs like set snap4File=\\15.154.112.188\automation\gui\Gen9Snap4.txt

for %%A in (%winFile%) do if %%~zA==0 goto EMPTY
::========================================================================::
:: read contents of Win file
set n=0
for /F "tokens=*" %%A in (%winFile%) do (
    SET /A n=!n! + 1
    set hpsum!n!=%%A
)

del %winFile%
break>%winFile%

if %hpsum1%==7.2.0 goto SNAP2
if %hpsum1%==7.3.0 goto SNAP3
if %hpsum1%==7.4.0 goto SNAP4
if %hpsum1%==7.5.0 goto SNAP5
if %hpsum1%==7.5.1 goto SNAP5
if %hpsum1%==7.6.0 goto SNAP6
if %hpsum1%==8.0.0 goto SNAP6
REM Add lines here for next HPSUM versions like and redirect it to right SPPs:
	REM if %hpsum1%==7.2.1
	REM goto SNAP2
	REM if %hpsum1%==7.4.0
	REM goto SNAP4

:SNAP2
	set readSnapFile=%snap2File%
	goto READ

:SNAP3
	set readSnapFile=%snap3File%
	goto READ

:SNAP4
	set readSnapFile=%snap4File%
	goto READ

:SNAP5
	set readSnapFile=%snap5File%
	goto READ

:SNAP6
	set readSnapFile=%snap6File%
	goto READ
REM Add more SPPs here like:
	REM :SNAP4
	REM set readSnapFile=%snap4File%
	REM goto READ

:READ
	REM Reading last 2 lines of respective SPP
	for /f %%i in ('find /v /c "" ^< %readSnapFile%') do set /a lines=%%i
	set /a startLine=%lines% - 2
	more /e +%startLine% %readSnapFile% > temp.txt
	set n=0
	for /F "tokens=*" %%A in (temp.txt) do (
		SET /A n=!n! + 1
		set spp!n!=%%A
	)
	del temp.txt

	::Sending email
	echo %date% %time% - Sending email >> start.log
	call mail.bat %hpsum1% %hpsum2% %spp1% %spp2%

	echo %date% %time% - New SPP or HPSUM is available %hpsum1% %hpsum2% %spp1% %spp2%, so running automation >> start.log

	::Copy of HPSUM and SPP
	echo %date% %time% - Starting the copy of HPSUM version:%hpsum1% build:%hpsum2% and SPP version:%spp1% and build:%spp2% >> start.log
	call %CD%/copy_win/copy-extract-hpsum-spp-win.bat %hpsum1% %hpsum2% %spp1% %spp2%
	echo %date% %time% - Copy of HPSUM and SPP complete >> start.log

	::Starting the GUI automation
	echo %date% %time% - Ready to run GUI-Automation >> start.log
	call RUN.bat
	echo %date% %time% - GUI-Automation is complete. >> start.log

	::Starting CLI automation
	echo %date% %time% - Starting CLI-Automation >> start.log
	call C:\automation\cli\bin\MiniMAT.bat
	echo %date% %time% - CLI-Automation is complete. >> start.log
	goto EOF
	
:EMPTY
	echo %date% %time% - No new HPSUM build found >> start.log
	goto EOF

:EOF
	echo %date% %time% - Good bye >> start.log