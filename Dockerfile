FROM maven:3.8.1-openjdk-17 as maven
WORKDIR /usr/src/app
COPY src src
COPY pom.xml .
RUN mvn package -DskipTests

FROM openjdk:17.0.2-slim-buster
WORKDIR /usr/src/app
COPY --from=maven /usr/src/app/target/assembly-voting-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar",  "app.jar", "--spring.profiles.active=prod", "-Xms2048M", "-Xmx3072M"]