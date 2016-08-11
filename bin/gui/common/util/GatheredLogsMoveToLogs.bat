:: GatheredLogsMoveToLogs.bat
:: Does following:
:: - Moves getheredlogs to automation logs dir.

set os=%1%
set source=%2%
set destination=%3%

If "%os%"=="linux" goto OSLINUX
move %source% %destination%

:OSLINUX
If "%os%"=="windows" goto END
mv %source% %destination%

:END
echo "Copied the HPSUM Gatheredlogs to automation logs dir."
