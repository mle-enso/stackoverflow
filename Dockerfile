FROM openjdk:13-alpine as builder
WORKDIR application
ARG JAR_FILE=target/stackoverflow-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} stackoverflow.jar
RUN java -Djarmode=layertools -jar stackoverflow.jar extract

FROM openjdk:13-alpine
VOLUME /tmp
EXPOSE 8080
USER 1001
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "org.springframework.boot.loader.JarLauncher"]
