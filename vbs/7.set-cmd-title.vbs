Set objShell = WScript.CreateObject("WScript.Shell")
Set objExec = objShell.Exec("%comspec% /c tail -f c:/logs/tzbapi-2.0.log")

Set oArgs = WScript.Arguments
For Each s In oArgs
    MsgBox(s)
Next

Do
 line = objExec.StdOut.ReadLine()
 WScript.echo(line)
Loop While Not objExec.Stdout.atEndOfStream