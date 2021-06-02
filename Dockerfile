FROM openjdk:11-slim

WORKDIR /app
COPY ./target/jersey-kubernetes-1.0-SNAPSHOT.jar ./

CMD ["java", "-jar", "jersey-kubernetes-1.0-SNAPSHOT.jar"]