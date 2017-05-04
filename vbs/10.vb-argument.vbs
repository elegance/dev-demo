' reference for: http://ss64.com/vb/syntax-args.html

' 按索引取参数
' run : cscript 10.vb-argument.vbs test

Set objArgs = WScript.Arguments
WScript.echo("param Count:" & objArgs.Count)

if objArgs.Count > 0 then
    param0 = objArgs.Item(0)
    WScript.echo("parma0:" & param0)
end if

' 按命名参数取值
' run: cscript 10.vb-argument.vbs /c:test.ini
Set colArgs = WScript.Arguments.Named
if Not IsEmpty(confFilePath) then
    WScript.echo("confFilePath:" & confFilePath)
end if

If colArgs.Exists("month") Then  
   strMonth = colArgs.Item("month") 
Else  
   strMonth = "January" 
End If
WScript.echo(strMonth)