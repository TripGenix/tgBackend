FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/BookingService-*-exec.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java","-jar","app.jar"]
