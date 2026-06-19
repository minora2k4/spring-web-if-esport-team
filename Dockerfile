# ===================== Stage 1: build =====================
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Maven Wrapper dùng distributionType=only-script nên cần curl để tải Maven.
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Cache lớp dependency: copy file build trước, tải dependency, rồi mới copy source.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -ntp dependency:go-offline

COPY src/ src/
RUN ./mvnw -B -ntp clean package -DskipTests

# ===================== Stage 2: runtime =====================
FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

# Chạy bằng user không phải root cho an toàn.
RUN useradd -r -u 1001 spring
USER spring

# Thư mục lưu ảnh upload (mount volume vào đây trong docker-compose).
VOLUME ["/app/uploads"]

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
