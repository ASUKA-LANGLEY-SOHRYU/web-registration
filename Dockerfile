FROM openjdk:22
WORKDIR /usr/src/app
COPY /target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./app.jar"]
