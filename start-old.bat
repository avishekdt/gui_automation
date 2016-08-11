@echo off
@setlocal ENABLEDELAYEDEXPANSION
@setlocal enableextensions enabledelayedexpansion

echo %date% %time% ----- Scheduler started ----- >> start.log
REM echo %date% %time% >> start.log
net use * /delete /y
net use  \\15.154.112.188\automation\gui 'Welcome123!' /user:ASIAPACIFIC\hpdvt-user  /p:yes

set mainFile=\\15.154.112.188\automation\gui\build_version_main.txt
set winFile=\\15.154.112.188\automation\gui\build_version_win.txt
set tempFile=\\15.154.112.188\automation\gui\newfile.tmp

set res=0
::========================================================================::
:: read contents of Win file
set vidx=0
for /F "tokens=*" %%A in (%winFile%) do (
    SET /A vidx=!vidx! + 1
    set localVar!vidx!=%%A
)
::========================================================================::
:: read contents of Main file
set vidx=0
for /F "tokens=*" %%A in (%mainFile%) do (
    SET /A vidx=!vidx! + 1
    set shareVar!vidx!=%%A
)
::========================================================================::
:: check for availability of new HPSUM or SPP build
if %shareVar1% neq %localVar1% set res=1
if %shareVar2% neq %localVar2% set res=1
if %shareVar3% neq %localVar3% set res=1
if %shareVar4% neq %localVar4% set res=1

::
if %res%==0 echo %date% %time% - NO new HPSUM build or SPP found >> start.log
if %res%==0 goto END
::========================================================================::
:: If there is a change, trigger automation
if %res%==1 >%tempFile% (
for /f "tokens=* delims= " %%a in (%winFile%) do (
    set /a N+=1
	if !N! == 1 ( echo %shareVar1%) 
    if !N! == 2 ( echo %shareVar2%) 
	if !N! == 3 ( echo %shareVar3%) 
	if !N! == 4 ( echo %shareVar4%)
	if !N! == 5 ( echo|set /p=end)
  )
)
::Sending email
echo %date% %time% - Sending email >> start.log
call mail.bat %shareVar1% %shareVar2% %shareVar3% %shareVar4%

::Creating the updated win file
echo %date% %time% - New SPP or HPSUM is available, so running automation >> start.log
del %winFile%
rename %tempFile% build_version_win.txt 

::Copy of HPSUM and SPP
echo %date% %time% - Starting the copy of HPSUM version: %shareVar1% build: %shareVar2% and SPP version: %shareVar3% and build: %shareVar4% >> start.log
call %CD%/copy_win/copy-extract-hpsum-spp-win.bat %shareVar1% %shareVar2% %shareVar3% %shareVar4%
echo %date% %time% - Copy of HPSUM and SPP complete >> start.log

::Starting the GUI automation
echo %date% %time% - Ready to run GUI-Automation >> start.log
call RUN.bat
echo %date% %time% - GUI-Automation is complete. >> start.log

::Starting CLI automation
echo %date% %time% - Starting CLI-Automation >> start.log
call C:\automation\cli\bin\MiniMAT.bat
echo %date% %time% - CLI-Automation is complete. >> start.log

::End of file
:END
echo %date% %time% - Good bye >> start.log