set oWShell = CreateObject("wscript.shell")
wsh.sleep 20
oWShell.AppActivate("Internet Explorer")  
oWShell.Sendkeys "{enter}"

oWShell.AppActivate("Confirm Close")  
oWShell.Sendkeys "{enter}"

