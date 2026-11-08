# ==========================================
# Etapa 1: Construcción (Build)
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar primero el pom.xml y descargar dependencias
# (Esto optimiza la caché de Docker y hace las compilaciones futuras más rápidas)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar el proyecto (empaquetar el .jar omitiendo los tests para mayor rapidez)
RUN mvn clean package -DskipTests

# ==========================================
# Etapa 2: Ejecución (Run)
# ==========================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el archivo .jar generado en la etapa de construcción anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto donde corre Spring Boot
EXPOSE 8080

# Comando de inicio de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]