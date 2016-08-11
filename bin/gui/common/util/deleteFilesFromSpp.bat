REM deleteFilesFromSpp.bat
REM Does following:
REM - Deletes the files from SPP.

set fileToDelete=%1%
del /Q %fileToDelete%
echo "Deleted the file."
