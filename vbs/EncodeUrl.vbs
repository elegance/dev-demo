Function UrlEncode_GBToUtf8(ByVal str)
    Dim B                    ''单个字符
    Dim ub                  ''中文字的Unicode码(2字节)
    Dim High8b, Low8b       ''Unicode码的高低位字节
    Dim UtfB1, UtfB2, UtfB3 ''Utf-8码的三个字节
    Dim i, s
    For i = 1 To Len(str)
        B=Mid(str, i, 1)
        ub = AscW(B)
        If (ub>=48 And ub<=57) Or (ub>=65 And ub<=90) Or (ub>=97 And ub<=122) Or ub=42 Or ub=45 Or ub=46 Or ub=64 Or ub=95 Then 
            ''48 to 57代表0~9;65 to 90代表A~Z;97 to 122代表a~z
            ''42代表*;46代表.;64代表@;45代表-;95代表_ 
            s=s & B
        ElseIf ub=32 Then ''空格转成+
            s=s & "+"
        ElseIf ub<128 Then    ''低于128的Ascii转成1个字节
            s=s & "%" & Right("00" & Hex(ub),2)
        Else
            High8b = (ub And &HFF00) / &H100 ''Unicode码高位
            Low8b = ub And &HFF ''Unicode码低位
            UtfB1 = (High8b And &HF0) / &H10 Or &HE0 ''取Unicode高位字节的二进制的前4位 + 11100000
            UtfB2 = ((High8b And &HF) * &H4 + (Low8b And &HC0) / &H40) Or &H80 ''取Unicode高位字节的后4位及低位字节的前2位 +10000000
            UtfB3 = (Low8b And &H3F) Or &H80 ''取Unicode低位字节的二进制后6位 + 10000000
            s = s & "%" & Hex(UtfB1) & "%" & Hex(UtfB2) & "%" & Hex(UtfB3)
        End If
    Next
    UrlEncode_GBToUtf8 = s
End Function
 
 
''“汉”－AscW("汉")=27721(十进制)    01101100 01001001(二进制)     6C49(十六进制)
''将Gb2312码转成Utf-8码（十六进制表示）的方法为，先用AscW将Gb2312转为Unicode码(2字节),再''将Unicode码的二进制中的位按utf-8(3字节）模板规则填充 x 位：
 
''URL解码，Gb2312页面提交到Utf-8页面
Function UrlDecode_GBToUtf8(ByVal str)
    Dim B,ub    ''中文字的Unicode码(2字节)
    Dim UtfB    ''Utf-8单个字节
    Dim UtfB1, UtfB2, UtfB3 ''Utf-8码的三个字节
    Dim i, n, s
    n=0
    ub=0
    For i = 1 To Len(str)
        B=Mid(str, i, 1)
        Select Case B
            Case "+"
                s=s & " "
            Case "%"
                ub=Mid(str, i + 1, 2)
                UtfB = CInt("&H" & ub)
                If UtfB<128 Then 
                    i=i+2
                    s=s & ChrW(UtfB)
                Else 
                    UtfB1=(UtfB And &H0F) * &H1000    ''取第1个Utf-8字节的二进制后4位
                    UtfB2=(CInt("&H" & Mid(str, i + 4, 2)) And &H3F) * &H40        ''取第2个Utf-8字节的二进制后6位
                    UtfB3=CInt("&H" & Mid(str, i + 7, 2)) And &H3F        ''取第3个Utf-8字节的二进制后6位
                    s=s & ChrW(UtfB1 Or UtfB2 Or UtfB3)
                    i=i+8
                End If 
            Case Else    ''Ascii码
                s=s & B
        End Select 
    Next
    UrlDecode_GBToUtf8 = s
End Function
 
 
Private Function UrlEncode_GBToUtf8_V2(szInput) 
    Dim wch, uch, szRet 
    Dim x 
    Dim nAsc, nAsc2, nAsc3 
    If szInput = "" Then 
        UrlEncode_GBToUtf8_V2= szInput 
        Exit Function 
    End If 
    For x = 1 To Len(szInput) 
        wch = Mid(szInput, x, 1) 
        nAsc = AscW(wch) 
        If nAsc < 0 Then nAsc = nAsc + 65536 
        If wch = "+" then
            szRet = szRet & "%2B"
        ElseIf wch = "%" then
            szRet = szRet & "%25"
        ElseIf (nAsc And &HFF80) = 0 Then 
            szRet = szRet & wch 
        Else 
            If (nAsc And &HF000) = 0 Then 
                uch = "%" & Hex(((nAsc \ 2 ^ 6)) Or &HC0) & Hex(nAsc And &H3F Or &H80) 
                szRet = szRet & uch 
            Else 
                uch = "%" & Hex((nAsc \ 2 ^ 12) Or &HE0) & "%" & _ 
                Hex((nAsc \ 2 ^ 6) And &H3F Or &H80) & "%" & _ 
                Hex(nAsc And &H3F Or &H80) 
                szRet = szRet & uch 
            End If 
        End If 
    Next 
    UrlEncode_GBToUtf8_V2= szRet 
End Function

Public Function URLEncode(strURL)
Dim I
Dim tempStr
For I = 1 To Len(strURL)
    If Asc(Mid(strURL, I, 1)) < 0 Then
       tempStr = "%" & Right(CStr(Hex(Asc(Mid(strURL, I, 1)))), 2)
       tempStr = "%" & Left(CStr(Hex(Asc(Mid(strURL, I, 1)))), Len(CStr(Hex(Asc(Mid(strURL, I, 1))))) - 2) & tempStr
       URLEncode = URLEncode & tempStr
    ElseIf (Asc(Mid(strURL, I, 1)) >= 65 And Asc(Mid(strURL, I, 1)) <= 90) Or (Asc(Mid(strURL, I, 1)) >= 97 And Asc(Mid(strURL, I, 1)) <= 122) Then
       URLEncode = URLEncode & Mid(strURL, I, 1)
    Else
       URLEncode = URLEncode & "%" & Hex(Asc(Mid(strURL, I, 1)))
    End If
Next
End Function
 
 
' dim str
' str = "1?a千一"
' WScript.Echo(UrlEncode_GBToUtf8(str) & "<br />")
' WScript.Echo(UrlEncode_GBToUtf8_V2(str) & "<br />")
WScript.echo(URLEncode("中文%123- 123"))
' WScript.Echo(UrlDecode_GBToUtf8(UrlEncode_GBToUtf8(str)) & "<br />")