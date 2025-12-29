FROM gradle:8.10.2-jdk21-alpine AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY catalog-service/build.gradle.kts catalog-service/

COPY catalog-service/src catalog-service/src

RUN ./gradlew :catalog-service:build -x test

FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app

COPY --from=builder /app/catalog-service/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]