FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /opt/app
COPY FilmDatabase/target/vizsgaremek-VSzilard-1.0-SNAPSHOT.jar /opt/app/FilmDatabase.jar
CMD ["java", "-jar", "/opt/app/FilmDatabase.jar"]