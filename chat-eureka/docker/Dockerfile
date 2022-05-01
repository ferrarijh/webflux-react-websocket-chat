FROM openjdk:11.0.14-jre-buster
ARG JAR_FILE=build/libs/*.jar

RUN mkdir app
WORKDIR /app

COPY ${JAR_FILE} ./chat-eureka-app.jar

ENTRYPOINT ["java", "-jar", "chat-eureka-app.jar"]