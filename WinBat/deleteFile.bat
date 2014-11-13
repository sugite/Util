@echo off
rem write to log
set filename=deletefile.log
echo -------------------Delete Expired Log Files------------------------- >>%filename%
echo Start >>%filename%
echo (%date% %time%) >>%filename%
echo Delete the expired log files in D:\dataForPrograms\onlinejudge, Please wait... >>%filename%
forfiles.exe /p "D:\dataForPrograms\onlinejudge" /m *main.cpp /d -3 /c "cmd /c del @path"
echo Delete the expired log files in D:\Program Files\apache-tomcat-7.0.32\bin, Please wait... >>%filename%
forfiles.exe /p "D:\Program Files\apache-tomcat-7.0.32\bin" /m *main.exe /d -3 /c "cmd /c del @path"
echo End >>%filename%
echo (%date% %time%) >>%filename%