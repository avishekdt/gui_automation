Set-ExecutionPolicy RemoteSigned
$ProcessIE= Get-Process iexplore 
$ProcessIE | Foreach-object { $_.CloseMainWindow() } -ErrorAction SilentlyContinue
cscript "test2.vbs"



$ProcessFF= Get-Process firefox 
$ProcessFF | Foreach-Object { $_.CloseMainWindow() } -ErrorAction SilentlyContinue
cscript "test2.vbs"



$ProcessGC= Get-Process chrome 
$ProcessGC | Foreach-Object { $_.CloseMainWindow() } -ErrorAction SilentlyContinue
cscript "test2.vbs"

