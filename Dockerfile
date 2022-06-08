FROM maven:3.8-openjdk-11-slim as maven_build
WORKDIR /app

COPY pom.xml ./pom.xml
COPY src ./src
RUN mvn dependency:go-offline -B
RUN mvn package

FROM openjdk:11
WORKDIR /app

COPY --from=maven_build /app/target/*.jar ./sunnycloud.jar

EXPOSE 8080
ENV JAVA_OPTS ""
CMD ["java", "-jar", "./sunnycloud.jar"]





