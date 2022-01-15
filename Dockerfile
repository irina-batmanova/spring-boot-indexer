#FROM maven:3.6.0-jdk-11-slim AS build
#COPY src .
#COPY pom.xml .
#RUN mvn install
#ADD storage_data ./storage_data
#
##COPY --from=build /home/app/target/demo-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","target/demo-0.0.1-SNAPSHOT.jar"]
##ENTRYPOINT ['/bin/bash']

FROM maven:latest as builder

COPY pom.xml /usr/local/pom.xml
COPY src/ /usr/local/src
WORKDIR /usr/local/
ADD storage_data ./storage_data
RUN mvn clean install
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/indexer-0.0.1-SNAPSHOT.jar"]
