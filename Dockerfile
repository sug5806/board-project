FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG ENVIRONMENT

ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

ENTRYPOINT ["java","-jar","/app.jar"]