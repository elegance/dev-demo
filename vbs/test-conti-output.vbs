' reference for : http://codego.net/253563/
Set objShell = WScript.CreateObject("WScript.Shell")
Set objExec = objShell.Exec("%comspec% /c tail -f log.txt")
Do
 line = objExec.StdOut.ReadLine()
 's = s & line & vbcrlf
 WScript.Echo line
Loop While Not objExec.Stdout.atEndOfStream
'WScript.Echo s