FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

COPY src src

RUN ./mvnw -B clean package


FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

RUN apt-get update \
    && apt-get install --yes --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

RUN groupadd --system spring \
    && useradd --system --gid spring spring

COPY --from=build \
    --chown=spring:spring \
    /workspace/target/*.jar \
    app.jar

USER spring

EXPOSE 8080

ENV JAVA_OPTS=""

HEALTHCHECK \
    --interval=30s \
    --timeout=5s \
    --start-period=20s \
    --retries=3 \
    CMD curl --fail --silent \
        http://localhost:8080/actuator/health \
        > /dev/null || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]