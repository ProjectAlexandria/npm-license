FROM openjdk:11 as build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw && ./mvnw clean install

FROM node:17

ENV JAVA_OPTS='-Xmx128m' \
    HEALTHCHECK_URL=http://localhost:8080/actuator/health

COPY --from=build /target/npm-license-*.jar /npm-license.jar

RUN apt-get update && apt-get install -y openjdk-11-jre && rm -rf /var/lib/apt/lists/*

CMD java -jar /npm-license.jar

# Volume with shared data
VOLUME /alexandriadata

HEALTHCHECK --interval=10s --timeout=3s CMD wget --no-verbose --tries=1 --spider $HEALTHCHECK_URL || exit 1
