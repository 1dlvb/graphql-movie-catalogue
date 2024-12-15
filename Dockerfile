FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/graphql-movie-catalogue-0.0.1-SNAPSHOT.jar /app/graphql-movie-catalogue.jar

ENTRYPOINT ["java", "-jar", "/app/graphql-movie-catalogue.jar"]
