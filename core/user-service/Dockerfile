FROM eclipse-temurin:21-jdk-jammy
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/user-service*.jar /app/user-service.jar
CMD ["java", "-jar", "/app/user-service.jar"]