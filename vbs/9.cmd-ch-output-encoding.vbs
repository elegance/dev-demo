line="ÐÐÐÅÏ¢£º  a   b   c"

wscript.echo(URLEncodeUTF8(line))
wscript.echo(URLEncodeAnsi(line))


Function URLEncodeUTF8(str)
    Dim i,c,s,length
    length = Len(str)
    For i = 1 To length
        s = Mid(str,i,1)
        c = "&H" & Hex(AscW(Mid(str,i,1)))
        If ( c >= AscW("A") And c <= AscW("Z") ) Or _
            ( c >= AscW("a") And c <= AscW("z") ) Or _
            ( c >= AscW("0") And c <= AscW("9") ) Or _
            ( c = AscW("-") Or c = AscW("_") Or c = AscW(".") ) Then
            URLEncodeUTF8 = URLEncodeUTF8 & s
        ElseIf c = AscW(" ") Then
            URLEncodeUTF8 = URLEncodeUTF8 & "+"
        Else
            If c >= &H0001 And c <= &H007F Then
                URLEncodeUTF8 = URLEncodeUTF8 & s
            ElseIf c > &H07FF Then
                URLEncodeUTF8 = URLEncodeUTF8 & "%" & Hex(&HE0 Or (c\(2^12) And &H0F))
                URLEncodeUTF8 = URLEncodeUTF8 & "%" & Hex(&H80 Or (c\(2^6) And &H3F))
                URLEncodeUTF8 = URLEncodeUTF8 & "%" & Hex(&H80 Or (c\(2^0) And &H3F))
            Else
                URLEncodeUTF8 = URLEncodeUTF8 & "%" & Hex(&HC0 Or (c\(2^6) And &H1F))
                URLEncodeUTF8 = URLEncodeUTF8 & "%" & Hex(&H80 Or (c\(2^0) And &H3F))
            End If
        End If
    Next
End Function

Function URLEncodeAnsi(str)
	Dim i,c,s,length
	length = Len(str)
	For i = 1 To length
		s = Mid(str,i,1)
		c = Asc(Mid(str,i,1))
		If ( c >= Asc("A") And c <= Asc("Z") ) Or _
			( c >= Asc("a") And c <= Asc("z") ) Or _
			( c >= Asc("0") And c <= Asc("9") ) Or _
			( c = Asc("-") Or c = Asc("_") Or c = Asc(".") ) Then
			URLEncodeAnsi = URLEncodeAnsi & s
		ElseIf c = Asc(" ") Then
			URLEncodeAnsi = URLEncodeAnsi & "+"
		Else
			URLEncodeAnsi = URLEncodeAnsi & "%" & Left(Hex(c),2)
			URLEncodeAnsi = URLEncodeAnsi & "%" & Right(Hex(c),2)			
		End If
	Next
End Function