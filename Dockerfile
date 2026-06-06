# Step 1: Build the application using Maven
FROM maven:3.9.11-eclipse-temurin-25 AS build
ENV SPRING_PROFILES_ACTIVE=prod
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Step 2: Create a lightweight runtime image (Optimizado para Producción)
FROM eclipse-temurin:25-jre-jammy AS runtime
WORKDIR /app

# Copiar el Fat JAR generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Step 3: Configure and run the application
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]