'----------------------------------------
On Error Resume Next
 
dim iCpuUsePercentage'记录CPU使用率
dim iSecond'记录使用率等于100所持续的时间
dim objFileStream'txt文本读写流，用于记录日志
dim objTextFileWriter'txt写对象
dim objShell'Shell'对象,用于执行指定程序
dim objFileInfo'文件信息对象，用于获取文件大小信息
dim bIsExecuteBat'是否已经运行过bat文件，下次重置0之前只执行一次批处理
iSecond=0'初始值为0
bIsExecuteBat=False
set objFileStream =CreateObject("Scripting.Filesystemobject")'创建文件系统对象
set objTextFileWriter=objFileStream.opentextfile("log.txt",8,True)'在脚本相同位置以追加方式打开日志文件，如果不存在，则创建日志文件
While True 
Set objProc  = GetObject("winmgmts:\\.\root\cimv2:win32_processor='cpu0'")'获取第一个CPU对象
iCpuUsePercentage=objProc.LoadPercentage'获取CPU使用率
objTextFileWriter.WriteLine("【" & Now & "】CPU使用率:" & iCpuUsePercentage & "%,使用率超80%持续时间:" & CStr(iSecond))
'统计累计CPU使用率达到80以上的持续次数,一旦有小于80的，次数归0
if iCpuUsePercentage>=80 then 
  iSecond=iSecond+1
else
  iSecond=0
  bIsExecuteBat=False'重置0之后，批处理可以再次执行
end if
if iSecond>=30 And bIsExecuteBat=False then
  objTextFileWriter.WriteLine("CPU使用率达80%持续时间超过30秒!!")
  '调用指定的批处理程序
  set objShell=CreateObject("WScript.shell")
  objShell.run "c:\email.bat"   
  bIsExecuteBat=True'标记已经执行过bat文件
end if
set objFileInfo=objFileStream.getfile("Log.txt") 
'日志文件大于2M时,重新建立日志文件
if (objFileInfo.size/1024/1024) >=2 then
  '先关闭文件才能操作
  objTextFileWriter.close
  objFileStream.MoveFile "Log.txt",Replace(Replace(Replace(Now,":",""),"-","")," ","") & "BackLog.txt"  
  '重新打开日志文件  
  set objTextFileWriter=objFileStream.opentextfile("log.txt",8,True)
end if
Wend