' run: start test-app-out-to-vb.vbs "tail -10 log.txt"

Set objShell = CreateObject("WScript.Shell")
Set objExecObject = objShell.Exec ("%comspec% /c " & wscript.arguments(0))

Do While Not objExecObject.StdOut.AtEndOfStream
	strText = objExecObject.StdOut.ReadAll()
loop

msgbox strText