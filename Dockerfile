FROM openjdk:11
WORKDIR /usr/app
COPY build/libs/*.jar app.jar
CMD ["java", "-jar", "tourguide-main.jar"]
