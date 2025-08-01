FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/request-service*.jar /app/request-service.jar
CMD ["java", "-jar", "/app/request-service.jar"]