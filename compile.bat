@echo off

set PATH_COMPILE=C:\Personal\Project\IB\jets-digimod
echo Compile %1 using path %PATH_COMPILE%
if not "%2"=="" echo Additional Param: client=%2

if "%1" == "web" goto compile_web



goto end

:compile_web
cd %PATH_COMPILE%\digimod-data
call mvn clean install
cd %PATH_COMPILE%\digimod-web
if "%2"=="" (call mvn clean package) else (call mvn clean package -P %2)
goto end

:end
cd %PATH_COMPILE%
rem set PATH_COMPILE=
