FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle ./
COPY settings.gradle ./
COPY src ./src
RUN chmod +x gradlew
RUN ./gradlew clean build -x test
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
