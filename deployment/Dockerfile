# Stage 1: build
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app

# Copiar configuración de Gradle
COPY build.gradle settings.gradle gradlew gradlew.bat gradle.properties ./
COPY gradle ./gradle

# Copiar todo el proyecto (manteniendo estructura)
COPY applications /app/applications
COPY domain /app/domain
COPY infrastructure /app/infrastructure
COPY main.gradle /app/

# Compilar el jar desde el módulo app-service
RUN ./gradlew :app-service:clean :app-service:bootJar -x test -x validateStructure

# Stage 2: runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiar el jar construido desde el módulo correcto
COPY --from=builder /app/applications/app-service/build/libs/*.jar app.jar

EXPOSE 8082

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
