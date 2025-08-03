FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/gateway-server*.jar /app/gateway-server.jar
CMD ["sh", "-c", "sleep 25 && java -jar /app/gateway-server.jar"]