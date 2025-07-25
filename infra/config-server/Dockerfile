FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/config-server*.jar /app/config-server.jar
CMD ["java", "-jar", "/app/config-server.jar"]