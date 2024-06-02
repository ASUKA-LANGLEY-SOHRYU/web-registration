FROM eclipse-temurin:22-jdk-alpine as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package -Dmaven.test.skip


FROM eclipse-temurin:22-jdk-alpine
WORKDIR /opt/app
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
RUN mkdir -p ~/.postgresql
RUN wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" --output-document ~/.postgresql/root.crt
RUN chmod 0600 ~/.postgresql/root.crt
WORKDIR /
ENTRYPOINT ["ls", "-la", "~/.postgresql"]
#ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]
