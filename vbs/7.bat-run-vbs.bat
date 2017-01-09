title "Web信号采集器-%date:~0,10% %time:~0,8%"
rem 将test.txt的文件转存到 "日期_时间.txt",并置空
type test.txt > %date:~0,4%-%date:~5,2%-%date:~8,2%_%time:~0,2%-%time:~3,2%-%time:~6,2%.txt && cd. > test.txt
echo "Web信号采集中，请勿关闭..."
cscript 7.set-cmd-title.vbs param1