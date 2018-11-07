
set EXPORT_DIR=%BASEDIR%
set DUMP_DIR=%EXPORT_DIR%

set CLASSPATH_PREFIX=%BASEDIR%\\home\\*;%BASEDIR%\\home;%BASEDIR%\\home\\libs\\*
set JAVA_OPTS=-Xms256M -Xmx512M -Xss160k -XX:NewRatio=1 -XX:+UseParallelGC -XX:ParallelGCThreads=2 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="%DUMP_DIR%" -XX:+UseAdaptiveSizePolicy -server

