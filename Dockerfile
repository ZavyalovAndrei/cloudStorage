FROM openjdk:17
ADD target/CloudStorage-1.0-SNAPSHOT.jar cloudStorage.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/cloudStorage.jar"]