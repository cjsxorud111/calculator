FROM openjdk:8-jre
COPY target/calculator-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]