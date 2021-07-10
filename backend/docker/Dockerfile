FROM tomcat:9.0.50-jdk8 as build

ADD backend-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]