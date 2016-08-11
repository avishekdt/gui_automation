:: email.bat :::::::::::::::::::::::::::::::::::::::::::::::::::::
@echo off
setlocal


:: use these settings to send from a gmail account
:: set port=465 and set SSL=True

:: use these settings for standard email SMTP port and no encryption
:: set port=25 and set SSL=False

:: Change these following items to use the same variables all the time
:: or use the command line to pass all the variables

set Port=25
set SSL=False
set From="automation-hpsum@hpe.com"
set To="automation-hpsum@hpe.com"
set Subject="Windows: MAT Automation Started - HPSUM version:%1 build:%2 and SPP version:%3 build:%4<EOM>"
set Body=""
set SMTPServer="smtp1.hpe.com"
set User="Automation"
set fileattach=""

if "%~7" NEQ "" (
set From="%~1"
set To="%~2"
set Subject="%~3"
set Body="%~4"
set SMTPServer="%~5"
set User="%~6"
set Pass="%~7"
set fileattach="%~8"
)

set "vbsfile=%temp%\email-bat.vbs"
del "%vbsfile%" 2>nul
set cdoSchema=http://schemas.microsoft.com/cdo/configuration
echo >>"%vbsfile%" Set objArgs       = WScript.Arguments
echo >>"%vbsfile%" Set objEmail      = CreateObject("CDO.Message")
echo >>"%vbsfile%" objEmail.From     = %From%
echo >>"%vbsfile%" objEmail.To       = %To%
echo >>"%vbsfile%" objEmail.Subject  = %Subject%
echo >>"%vbsfile%" objEmail.Textbody = %body%
if exist %fileattach% echo >>"%vbsfile%" objEmail.AddAttachment %fileattach%
echo >>"%vbsfile%" with objEmail.Configuration.Fields
echo >>"%vbsfile%"  .Item ("%cdoSchema%/sendusing")        = 2 ' not local, smtp
echo >>"%vbsfile%"  .Item ("%cdoSchema%/smtpserver")       = %SMTPServer%
echo >>"%vbsfile%"  .Item ("%cdoSchema%/smtpserverport")   = %port%
echo >>"%vbsfile%"  .Item ("%cdoSchema%/smtpauthenticate") = 1 ' cdobasic
echo >>"%vbsfile%"  .Item ("%cdoSchema%/sendusername")     = %user%
echo >>"%vbsfile%"  .Item ("%cdoSchema%/smtpusessl")       = %SSL%
echo >>"%vbsfile%"  .Item ("%cdoSchema%/smtpconnectiontimeout") = 30
echo >>"%vbsfile%"  .Update
echo >>"%vbsfile%" end with
echo >>"%vbsfile%" objEmail.Send

cscript.exe /nologo "%vbsfile%"
echo %date% %time% - Email sent >> start.log
del "%vbsfile%" 2>nul
goto :EOF