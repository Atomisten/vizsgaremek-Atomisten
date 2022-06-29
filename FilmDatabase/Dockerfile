FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /opt/app
COPY FilmDatabase/target/Filmdatabase-1.0-SNAPSHOT.jar /opt/app/Filmdatabase.jar
CMD ["java", "-jar", "/opt/app/Filmdatabase.jar"]