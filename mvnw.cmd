@REM Maven Wrapper
@setlocal
@set WRAPPER_JAR="%~dp0\.mvn\wrapper\maven-wrapper.jar"
@set WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

@if not exist %WRAPPER_JAR% (
    powershell -Command "Invoke-WebRequest -Uri %WRAPPER_URL% -OutFile %WRAPPER_JAR%"
)

@set MAVEN_PROJECTBASEDIR=%~dp0
java -jar %WRAPPER_JAR% %*
