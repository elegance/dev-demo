@echo off
title "Web�����źŲɼ���-%date:~0,10% %time:~0,8%"
set curDir=%~dp0
set binDir=%curDir%bin\
set App=%binDir%App.vbs
set tee=%binDir%tee

:: �����Ҫ��������ɼ������ɼ��źţ����Ը���һ��"startup.bat"�ļ����޸����²��� ����Ȼ�����titleҲ���Ե��������� ����Ϊ "������-web�����źŲɼ���-..."��"Python-web�����źŲɼ���...")

:: �����ļ���Ĭ��Ϊ ��ǰĿ¼�� ��conf.ini
set confFile=%curDir%conf.ini

:: ��ص��ź��ļ���Ĭ��Ϊ��ǰ¼��signal��Ŀ¼�µġ�signal.txt���ļ�
set signalFile=%curDir%signal\signal.txt

:: �����־�ļ�Ĭ��Ϊ��ǰĿ¼�µ�output.log
set logFile=%curDir%output.log

cscript %App% /c:%confFile% /s:%signalFile% | %tee% -a %logFile%