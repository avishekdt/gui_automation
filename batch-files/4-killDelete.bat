:: killDelete.bat
:: Does following:
:: - Kills any existing HPSUM Service
:: - Delete %TEMP% - HPSUM directory
:: - Delete C:\cpqsystem directory

taskkill /f /IM hpsum_service*
rmdir /S/Q C:\Users\ADMINI~1\AppData\Local\Temp\1\HPSUM
rmdir /S/Q C:\Users\ADMINI~1\AppData\Local\Temp\2\HPSUM
::rmdir /S/Q C:\cpqsystem

