Set FSO=CreateObject("Scripting.FileSystemObject")

parentPath = FSO.GetParentFolderName("C:\1.log")

wscript.echo(parentPath)

parentPath2 = FSO.GetParentFolderName(".\app\conf.ini")
wscript.echo(parentPath2)
