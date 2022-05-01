FROM openjdk:11.0.14-jre-buster
ARG JAR_FILE=build/libs/*.jar

RUN mkdir app
WORKDIR /app

COPY ${JAR_FILE} ./chat-user-app.jar

RUN printenv

ENTRYPOINT ["java", "-jar", "chat-user-app.jar", "--app.datasource.url=${USER_DB_HOST}"]