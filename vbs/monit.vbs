'----------------------------------------
On Error Resume Next
 
dim iCpuUsePercentage'��¼CPUʹ����
dim iSecond'��¼ʹ���ʵ���100��������ʱ��
dim objFileStream'txt�ı���д�������ڼ�¼��־
dim objTextFileWriter'txtд����
dim objShell'Shell'����,����ִ��ָ������
dim objFileInfo'�ļ���Ϣ�������ڻ�ȡ�ļ���С��Ϣ
dim bIsExecuteBat'�Ƿ��Ѿ����й�bat�ļ����´�����0֮ǰִֻ��һ��������
iSecond=0'��ʼֵΪ0
bIsExecuteBat=False
set objFileStream =CreateObject("Scripting.Filesystemobject")'�����ļ�ϵͳ����
set objTextFileWriter=objFileStream.opentextfile("log.txt",8,True)'�ڽű���ͬλ����׷�ӷ�ʽ����־�ļ�����������ڣ��򴴽���־�ļ�
While True 
Set objProc  = GetObject("winmgmts:\\.\root\cimv2:win32_processor='cpu0'")'��ȡ��һ��CPU����
iCpuUsePercentage=objProc.LoadPercentage'��ȡCPUʹ����
objTextFileWriter.WriteLine("��" & Now & "��CPUʹ����:" & iCpuUsePercentage & "%,ʹ���ʳ�80%����ʱ��:" & CStr(iSecond))
'ͳ���ۼ�CPUʹ���ʴﵽ80���ϵĳ�������,һ����С��80�ģ�������0
if iCpuUsePercentage>=80 then 
  iSecond=iSecond+1
else
  iSecond=0
  bIsExecuteBat=False'����0֮������������ٴ�ִ��
end if
if iSecond>=30 And bIsExecuteBat=False then
  objTextFileWriter.WriteLine("CPUʹ���ʴ�80%����ʱ�䳬��30��!!")
  '����ָ�������������
  set objShell=CreateObject("WScript.shell")
  objShell.run "c:\email.bat"   
  bIsExecuteBat=True'����Ѿ�ִ�й�bat�ļ�
end if
set objFileInfo=objFileStream.getfile("Log.txt") 
'��־�ļ�����2Mʱ,���½�����־�ļ�
if (objFileInfo.size/1024/1024) >=2 then
  '�ȹر��ļ����ܲ���
  objTextFileWriter.close
  objFileStream.MoveFile "Log.txt",Replace(Replace(Replace(Now,":",""),"-","")," ","") & "BackLog.txt"  
  '���´���־�ļ�  
  set objTextFileWriter=objFileStream.opentextfile("log.txt",8,True)
end if
Wend