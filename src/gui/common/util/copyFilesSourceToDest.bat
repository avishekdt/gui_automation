:: copyFilesSourceToDest.bat
:: Does following:
:: - Copies file from source to destination.

set source=%1%
set destination=%2%

copy %source% %destination%
echo "Source :" %source%
echo "Destination :" %destination%
echo "Copied files from source to destination"
