FROM openjdk:8
MAINTAINER leonardocalil
COPY target/assessment.jar app.jar
ENTRYPOINT ["java","-jar", "app.jar"]