' 读取配置文件

tmp=GetCfgMain("app/conf.ini")

cfgParts = Split(tmp, vbcr)

cfgStr = ""

for each cfgPart in cfgParts
  wscript.echo(cfgPart)
  if instr(cfgPart, "=") > 0 then
    values = split(cfgPart, "=")
    cfgStr = cfgStr & "&" & values(0) & "=" & values(1)
  end if
next

wscript.echo(cfgStr)

' url=GetCfg(tmp, "url")
' url2=GetCfg(tmp, "url2")
' wscript.echo(url)
' wscript.echo(url2)










Function GetCfg(strCfg,str)
ON ERROR RESUME Next
GetCfgTmp=""
CfgParts=Split(strCfg,vbCr)
For Each CfgPart In CfgParts
  if InStr(CfgPart,"=")>0 Then
    Values=Split(CfgPart,"=")
    If str=Values(0) Then
    GetCfgTmp=Values(1)
    Exit For
    End If
  End If
Next
GetCfg=GetCfgTmp
End Function

Function GetCfgMain(strCfgMainFile)
ON ERROR RESUME Next
str=""
Set FSO=CreateObject("Scripting.FileSystemObject")
If Not FSO.FileExists(strCfgMainFile) Then
  wscript.echo("""" & strCfgMainFile & """ file not found!")
  GetCfgMain=str:Exit Function
End If
Set f=FSO.OpenTextFile(strCfgMainFile,1)
Do While Not f.AtEndOfStream
  strTmp=Trim(f.ReadLine)
  If strTmp<>"" Then
  If Left(strTmp,1)<>"#" And Left(strTmp,2)<>"//" Then
  str=str&vbCr&strTmp
  End If
  End If
Loop
f.Close
GetCfgMain=str
End Function