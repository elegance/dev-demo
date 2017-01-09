' http://stackoverflow.com/questions/17880395/decoding-url-encoded-utf-8-strings-in-vbscript
Dim JSEngine
Set JSEngine = CreateObject("System.Web.UI.ScriptControl")
    JSEngine.Language = "JScript"

' set list = CreateObject("System.Collections.ArrayList")
' list.add("abc")
' wscript.echo(list)

' set HttpUtility = CreateObject("System.web.HttpUtility")

' wscript.echo(HttpUtility.UrlEncode("abc&123 321"))