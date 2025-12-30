FROM gradle:9.2.1-jdk21-alpine AS builder
WORKDIR /app

COPY . .

RUN sed -i '/org.gradle.java.home/d' gradle.properties

RUN ./gradlew :bidding-service:build -x test

FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app
COPY --from=builder /app/bidding-service/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]