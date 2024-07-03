FROM eclipse-temurin:21 as build

WORKDIR /src
COPY . .

RUN ./gradlew :bootJar

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build --chmod=777 /src/build/libs/*.jar ./app.jar
ENTRYPOINT java -jar app.jar
