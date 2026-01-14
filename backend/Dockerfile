# ---- 1. Step: Build Backend ----
FROM maven:3.9-amazoncorretto-21 AS backendbuilder
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY . .
RUN mvn clean package -DskipTests

# ---- 2. Step: Run Backend ----
FROM amazoncorretto:21
WORKDIR /app

COPY --from=backendbuilder /app/target/backend.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]