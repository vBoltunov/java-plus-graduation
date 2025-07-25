FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/gateway-server*.jar /app/gateway-server.jar
CMD ["sh", "-c", "sleep 25 && java -jar /app/gateway-server.jar"]