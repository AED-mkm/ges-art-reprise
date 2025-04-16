FROM openjdk:17-jdk-alpine

EXPOSE 8087

#ADD ./target/*.jar ges-art.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} bureau_digital_universe.jar

#ENV JAVA_OPTS=""

#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
ENTRYPOINT ["java","-jar","/ges-art.jar"]