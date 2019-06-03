@echo off
for /f "delims=" %%a in ('dir /ad /b "%ProgramFiles%\Java" ^| findstr /i /c:"jdk"') do set JAVA_HOME=%ProgramFiles%\Java\%%a
set PATH=%PATH%;%%JAVA_HOME%%\bin;
set CLASSPATH=.;%%JAVA_HOME%%\lib;%%JAVA_HOME%%\lib\tools.jar
set RegV=HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment
reg add "%RegV%" /v "JAVA_HOME" /d "%JAVA_HOME%" /f
reg add "%RegV%" /v "Path" /t REG_EXPAND_SZ /d "%PATH%" /f
reg add "%RegV%" /v "CLASSPATH" /d "%CLASSPATH%" /f
mshta vbscript:msgbox("Java环境已成功注册！",64,"成功")(window.close)
exit