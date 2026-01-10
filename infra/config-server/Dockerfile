FROM eclipse-temurin:21-jdk-jammy
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/config-server*.jar /app/config-server.jar
CMD ["java", "-jar", "/app/config-server.jar"]