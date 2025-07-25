FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/discovery-server*.jar /app/discovery-server.jar
CMD ["java", "-jar", "/app/discovery-server.jar"]