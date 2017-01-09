@echo off
REM echo %1

REM if {%1}=={}

REM :gotDefault

REM goto end

REM :gotNewConf
REM goto end

REM set confFile=%1

REM if "%confFile%"=="" 
REM (
REM echo 空
REM ) 
REM else 
REM (
REM echo %1
REM )

@echo off
set one=%1
set two=%2

if "%one%"=="" (
echo abc) else (
echo %1)

if "%two%"=="" (echo null) else echo %2