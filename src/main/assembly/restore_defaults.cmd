@echo off

setlocal enabledelayedexpansion

set KEY_NAME="HKLM\SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment"
set VALUE_NAME=CurrentVersion
::
:: get the current version
::
FOR /F "usebackq skip=2 tokens=3" %%A IN (`REG QUERY %KEY_NAME% /v %VALUE_NAME% 2^>nul`) DO (
    set ValueValue=%%A
)

if defined ValueValue (
    @echo the current Java runtime is  %ValueValue%
) else ( 
	echo "Registry key %KEY_NAME% not found."
	goto end
)
set JAVA_CURRENT="HKLM\SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment\%ValueValue%"
set JAVA_HOME_VAR=JavaHome

::
:: get the javahome
::
FOR /F "usebackq skip=2 tokens=3*" %%A IN (`REG QUERY %JAVA_CURRENT% /v %JAVA_HOME_VAR% 2^>nul`) DO (
    set JAVA_HOME=%%A %%B
)

REM echo the path of the current Java JVM according to the registry is
REM echo %JAVA_HOME%
REM echo.
REM echo now if we try it :
REM "%JAVA_HOME%\bin\java.exe" -version

set CP="%JAVA_HOME%\lib\rt.jar";%CD%/lib

for %%f in (./lib/*.jar) do set CP=!CP!;%CD%/lib/%%f
for %%f in (./lib/laf/*.jar) do set CP=!CP!;%CD%/lib/laf/%%f

set CLASSPATH=%CP%

REM echo %CLASSPATH%

java marcclaessens.galgje.ui.swing.Reset


:end