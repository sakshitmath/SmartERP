FROM amazoncorretto:21-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "target/smarterp-backend-0.0.1-SNAPSHOT.jar"]