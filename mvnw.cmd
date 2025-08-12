@ECHO OFF
setlocal

set MAVEN_SKIP_RC=1

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

rem Resolve project base dir (the one containing .mvn)
set MAVEN_PROJECTBASEDIR=%APP_HOME%
if exist "%APP_HOME%\.mvn" goto endDetectBaseDir
set EXEC_DIR=%CD%
cd /d "%APP_HOME%"
:findBaseDir
if exist ".mvn" goto baseDirFound
cd ..
if "%CD%"=="%EXEC_DIR%" goto baseDirNotFound
goto findBaseDir
:baseDirFound
set MAVEN_PROJECTBASEDIR=%CD%
cd /d "%EXEC_DIR%"
goto endDetectBaseDir
:baseDirNotFound
set MAVEN_PROJECTBASEDIR=%APP_HOME%
cd /d "%EXEC_DIR%"
:endDetectBaseDir

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_PROPERTIES="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"

rem Read wrapperUrl from properties
set DOWNLOAD_URL=
for /F "usebackq tokens=1,2 delims==" %%A in (%WRAPPER_PROPERTIES%) do (
  if "%%A"=="wrapperUrl" set DOWNLOAD_URL=%%B
)

if not exist %WRAPPER_JAR% (
  echo Downloading Maven Wrapper jar...
  powershell -NoProfile -Command "Invoke-WebRequest -UseBasicParsing %DOWNLOAD_URL% -OutFile %WRAPPER_JAR%"
)

set JAVA_EXE=java.exe
if defined JAVA_HOME set JAVA_EXE="%JAVA_HOME%\bin\java.exe"

set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

%JAVA_EXE% -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -cp %WRAPPER_JAR% %WRAPPER_LAUNCHER% %*

endlocal
