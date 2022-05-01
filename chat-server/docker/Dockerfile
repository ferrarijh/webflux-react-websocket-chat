FROM openjdk:11.0.14-jre-buster
ARG JAR_FILE=build/libs/*.jar

RUN mkdir app
WORKDIR /app

COPY ${JAR_FILE} ./chat-server-app.jar

ENTRYPOINT ["java", "-jar", "chat-server-app.jar", "--redis.host=${REDIS_HOST}"]