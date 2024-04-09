FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn dependency:go-offline

RUN mvn clean package -DskipTests

ENTRYPOINT ["java", "-jar", "/app/target/shaft.jar"]