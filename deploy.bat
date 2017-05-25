@echo off
mvn package && scp target\tornadofx-web-1.0.0.war w173288@tornadofx.io:tomcat/webapps/ROOT.war