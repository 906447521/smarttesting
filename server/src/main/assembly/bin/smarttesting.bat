
@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

@REM ###########################################################################################
@REM EXPORT_DIR 设置输出文件目录
:repoSetup
if exist %BASEDIR%\bin\setEnv.bat (
call "%BASEDIR%\bin\setEnv.bat"
)
echo "Using smarttesting_HOME :  %BASEDIR%"
echo "Using EXPORT_HOME :  %EXPORT_DIR%"
echo "Using DUMP_HOME :  %DUMP_DIR%"
echo "Using JAVA_HOME:  %JAVA_HOME%"
echo "CLASSPATH: %CLASSPATH_PREFIX%"
if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH=
set EXTRA_JVM_ARGUMENTS=
goto endInit

@REM Reaching here means variables are defined and arguments have been captured
:endInit

if "%CMD_LINE_ARGS%"=="run" goto doRun
if "%CMD_LINE_ARGS%"=="start" goto doStart
if "%CMD_LINE_ARGS%"=="restart" goto doRestart
if "%CMD_LINE_ARGS%"=="stop" goto doStop
if "%CMD_LINE_ARGS%"=="version" goto doVersion

echo -----------------------------------------------------------------------------------------
echo Usage:  ( commands ... )
echo commands:"
echo   run               Start  in the current window
echo   start             Start  in a separate window
echo   restart           Start  in a separate window, If unable to remove or clear stale PID file.
echo   stop              Stop
echo   version           What version of  are you running?
echo -----------------------------------------------------------------------------------------
goto end

if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal

:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%


:doRun
%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath "%CLASSPATH_PREFIX%;%CLASSPATH%" -Dapp.name="smarttesting" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" -Dexportdir="%EXPORT_DIR%" smarttesting.Main start
goto end

:doStart
start "smarttesting" %JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath "%CLASSPATH_PREFIX%;%CLASSPATH%" -Dapp.name="smarttesting" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" -Dexportdir="%EXPORT_DIR%" smarttesting.Main start
goto end

:doRestart
start "smarttesting" %JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath "%CLASSPATH_PREFIX%;%CLASSPATH%" -Dapp.name="smarttesting" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" -Dexportdir="%EXPORT_DIR%" smarttesting.Main restart
goto end

:doStop
%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath "%CLASSPATH_PREFIX%;%CLASSPATH%" -Dapp.name="smarttesting" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" -Dexportdir="%EXPORT_DIR%" smarttesting.Main stop
goto end

:doVersion
%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath "%CLASSPATH_PREFIX%;%CLASSPATH%" -Dapp.name="smarttesting" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" -Dexportdir="%EXPORT_DIR%" smarttesting.Main version
goto end