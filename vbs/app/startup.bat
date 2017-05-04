@echo off
title "Web三方信号采集器-%date:~0,10% %time:~0,8%"
set curDir=%~dp0
set binDir=%curDir%bin\
set App=%binDir%App.vbs
set tee=%binDir%tee

:: 如果需要启动多个采集器来采集信号，可以复制一份"startup.bat"文件，修改以下参数 （当然上面的title也可以调整，比如 调整为 "金字塔-web三方信号采集器-..."、"Python-web三方信号采集器...")

:: 配置文件，默认为 当前目录下 的conf.ini
set confFile=%curDir%conf.ini

:: 监控的信号文件，默认为当前录“signal”目录下的“signal.txt”文件
set signalFile=%curDir%signal\signal.txt

:: 输出日志文件默认为当前目录下的output.log
set logFile=%curDir%output.log

cscript %App% /c:%confFile% /s:%signalFile% | %tee% -a %logFile%