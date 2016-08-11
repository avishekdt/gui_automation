Local $i = 0
Opt("WinTitleMatchMode", 3)
While $i <= 720
   WinActivate("[TITLE:Windows Security]", "")
   ;WinClose("[TITLE:Windows Security]", "")
   Send("{ALTDOWN}i{ALTUP}")   
   $i = $i + 1
   Sleep("5000")
WEnd



