# Stage 1: build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy everything so Gradle wrapper can see settings.gradle, src, etc.
COPY . .

# Make the wrapper executable and build the fat jar
RUN chmod +x ./gradlew \
 && ./gradlew clean bootJar --no-daemon

# Stage 2: runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the Spring Boot fat jar (whatever its exact name is) into app.jar
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# Launch, injecting the hackernews.url via ENV
ENTRYPOINT ["sh","-c","java -Dhackernews.url=${HACKERNEWS_URL} -jar /app/app.jar"]
