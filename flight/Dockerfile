FROM eclipse-temurin:17-jre
WORKDIR /app
COPY flight-impl/target/flight-impl-1.0-SNAPSHOT.jar flight.jar
ENTRYPOINT ["java", "-jar", "flight.jar"]