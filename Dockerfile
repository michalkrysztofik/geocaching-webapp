FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /workspace

COPY --chmod=0755 gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle gradle.properties ./

RUN ./gradlew dependencies --no-daemon
COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Run locally:
# docker run -p 8080:8080 \
# -e DB_USER="user" \
# -e DB_PASS="password" \
# --name geocaching-app \
# geocaching-webapp