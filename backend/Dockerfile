# ====== 构建阶段 ======
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
RUN for i in 1 2 3; do mvn dependency:go-offline -B && break || echo "Retry $i..."; done
COPY src ./src
RUN mvn package -DskipTests -q

# ====== 运行阶段 ======
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
RUN mkdir -p /app/uploads
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
