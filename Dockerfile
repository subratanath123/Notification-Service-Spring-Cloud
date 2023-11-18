FROM openjdk:17-jdk-slim
CMD ["gradle", "build"]
COPY /build/libs/notification-server-0.0.1-SNAPSHOT.jar /app/notification-server.jar
EXPOSE 8007
ENTRYPOINT ["java", "-jar", "/app/notification-server.jar"]
