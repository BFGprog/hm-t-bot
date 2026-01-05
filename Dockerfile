
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# mvnw требует bash
RUN apt-get update && apt-get install -y bash && rm -rf /var/lib/apt/lists/*

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw package -DskipTests

# -----------
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]