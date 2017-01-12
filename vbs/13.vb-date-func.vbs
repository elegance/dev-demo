' reference for: http://www.w3school.com.cn/vbscript/func_formatdatetime.asp
n=now

' wscript.echo(n)
' wscript.echo(FormatDateTime(n, 3))

strDate = year(n) & lpad(month(n)) & lpad(day(n))  & "_" & lpad(hour(n)) & lpad(minute(n)) & lpad(second(n))

wscript.echo(strDate)

Public Function lpad(str)                                
    lpad = right("0" & str, 2)
End Function