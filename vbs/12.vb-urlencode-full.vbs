' http://stackoverflow.com/questions/17880395/decoding-url-encoded-utf-8-strings-in-vbscript
' Dim JSEngine
' Set JSEngine = CreateObject("System.Web.UI.ScriptControl")
' JSEngine.Language = "JScript"

' set list = CreateObject("System.Collections.ArrayList")
' list.add("abc")
' wscript.echo(list)

' set HttpUtility = CreateObject("System.web.HttpUtility")

' wscript.echo(HttpUtility.UrlEncode("abc&123 321"))

wscript.echo(Hex(ascw("+")))
wscript.echo(Hex(ascw(" ")))
wscript.echo(Hex(ascw("/")))
wscript.echo(Hex(ascw("?")))
wscript.echo(Hex(ascw("%")))
wscript.echo(Hex(ascw("#")))
wscript.echo(Hex(ascw("&")))
wscript.echo(Hex(ascw("=")))
wscript.echo(Hex(ascw(":")))
wscript.echo(Hex(ascw("\")))

' testStr = "2017-01-09 13:38:50.154    ¹ÉÆ±´úÂë:002346 val=2.12% a=& ask=? dir=test/abc hash=#123 time=+34"
' testStr = " ¡¡   "
testStr = "+ /?%#&=:\¡¡ÎÄaA1"

wscript.echo(urlencode(testStr))

Function urlencode(str)
    Dim i,c,s,length
    length = Len(str)
    For i = 1 To length
        s = Mid(str,i,1)
        c = "&H" & Hex(AscW(Mid(str,i,1)))
        If ( c >= AscW("A") And c <= AscW("Z") ) Or _
            ( c >= AscW("a") And c <= AscW("z") ) Or _
            ( c >= AscW("0") And c <= AscW("9") ) Or _
            ( c = AscW("-") Or c = AscW("_") Or c = AscW(".") ) Then
            wscript.echo("here:" & s)
            urlencode = urlencode & s
        ElseIf c = AscW(" ") Then
            wscript.echo("here2:" & s)
            urlencode = urlencode & "+"
        Else
            If c >= &H0001 And c <= &H007F Then
                wscript.echo("here3:" & s)
                If instr("+ /?%#&=:\", s) > 0 Then
                    urlencode = urlencode & replace(c, "&H", "%")
                Else
                    urlencode = urlencode & s
                End If
            ElseIf c > &H07FF Then
                wscript.echo("here4:" & s)
                urlencode = urlencode & "%" & Hex(&HE0 Or (c\(2^12) And &H0F))
                urlencode = urlencode & "%" & Hex(&H80 Or (c\(2^6) And &H3F))
                urlencode = urlencode & "%" & Hex(&H80 Or (c\(2^0) And &H3F))
            Else
                wscript.echo("here5:" & s)
                urlencode = urlencode & "%" & Hex(&HC0 Or (c\(2^6) And &H1F))
                urlencode = urlencode & "%" & Hex(&H80 Or (c\(2^0) And &H3F))
            End If
        End If
    Next
End Function
