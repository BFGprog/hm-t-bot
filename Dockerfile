# Используем OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

# Копируем исходники
COPY src ./src

# Сборка приложения
RUN ./mvnw clean package -DskipTests

EXPOSE 9001

# Запуск приложения
ENTRYPOINT ["java","-jar","target/hm-t-bot-0.0.1-SNAPSHOT.jar"]