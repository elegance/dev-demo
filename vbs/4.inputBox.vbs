' http://www.w3school.com.cn/vbscript/func_inputbox.asp
on error resume next 
dim fname
fname=InputBox("Enter your name:")
MsgBox("Your name is " & fname)