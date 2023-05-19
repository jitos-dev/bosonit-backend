FROM openjdk:17.0-jdk-oracle
ADD target/*.jar /usr/share/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/share/app.jar"]
