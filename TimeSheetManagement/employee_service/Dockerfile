# # Docker Build Maven Stage
# FROM maven:3-jdk-8-alpine AS build
# # Copy folder in docker
# WORKDIR /opt/app
# COPY ./ /opt/app
# RUN mvn clean install -DskipTests
# # Run spring boot in Docker
# FROM openjdk:8-jdk-alpine
# COPY --from=build /opt/app/target/*.jar user-service.jar
# ENV PORT 8081
# EXPOSE $PORT
# ENTRYPOINT ["java","-jar","/user-service.jar"]


# Docker Build Maven Stage
FROM docker.io/library/openjdk:17
VOLUME /tmp
COPY target/employee_service.jar /employee_service.jar
ENTRYPOINT ["java","-jar","/employee_service.jar"]
