FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD
 
# copy the pom and src code to the container
COPY pom.xml /build/
COPY src /build/src/

# package our application code
WORKDIR /build/
RUN mvn package -q
 
# the second stage of our build will use open jdk 8 on alpine 3.9
FROM openjdk:8-jre-alpine3.9
 
# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /build/target/*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]