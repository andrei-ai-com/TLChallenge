FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /opt/tlchallenge

ARG JAR_FILE=target/tlchallenge-1.0.jar
COPY ${JAR_FILE} tlchallenge.jar

ENTRYPOINT ["java", "-jar", "tlchallenge.jar"]