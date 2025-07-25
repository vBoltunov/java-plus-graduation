FROM openjdk:21-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY target/stats-server*.jar /app/stats-server.jar
CMD ["sh", "-c", "sleep 25 && java -jar /app/stats-server.jar"]