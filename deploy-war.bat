@echo off
REM Build the project and deploy the WAR file to Tomcat

REM Set Tomcat webapps directory (change this to your server's path)
set TOMCAT_WEBAPPS_DIR=C:\path\to\tomcat\webapps

REM Build the project
mvn clean package -f pom.xml

REM Check if build was successful
if exist target\ROOT.war (
    echo Build successful. Deploying WAR file...
    copy /Y target\ROOT.war %TOMCAT_WEBAPPS_DIR%\ROOT.war
    echo Deployment complete.
) else (
    echo Build failed. WAR file not found.
)

pause