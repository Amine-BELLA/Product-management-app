FROM maven:3.9.9-amazoncorretto-21 as build

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:21-alpine as runtime

WORKDIR /app

COPY --from=build /app/target/trial-0.0.1-SNAPSHOT.jar /app/kata.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/kata.jar"]
