FROM eclipse-temurin:17-jre
WORKDIR /app
COPY auth-impl/target/auth-impl-1.0-SNAPSHOT.jar auth.jar
ENTRYPOINT ["java", "-jar", "auth.jar"]