FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /app

# COPY . .
# RUN mvn --batch-mode --settings deploy/settings.xml clean package -Dmaven.test.skip=true

# FROM openjdk:17-jdk-alpine
# COPY --from=builder ../._backend-0.0.1-SNAPSHOT.jar /app.jar

# 复制已经打包好的 JAR 文件到容器中
COPY ../backend-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
