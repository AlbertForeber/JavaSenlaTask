# --- СТАДИЯ СБОРКИ
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

# POM-файлы для установки зависимостей
COPY pom.xml .
COPY app/pom.xml app/
COPY annotation/pom.xml annotation/
COPY annotation_processor/pom.xml annotation_processor/

# Загружаем зависимости (-B - batch-mode неинтерактивный режим, без цветного вывода)
RUN mvn dependency:go-offline -B

# Копируем исходники
COPY checkstyle checkstyle
COPY app/src app/src
COPY annotation/src annotation/src
COPY annotation_processor/src annotation_processor/src

# Собираем проект
RUN mvn clean package -B

# --- СТАДИЯ ЗАПУСКА
FROM tomcat:10.1.24-jdk21

# Удаляем дефолтные приложения
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем собранный WAR
COPY --from=builder /build/app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Настройка JVM
ENV CATALINA_OPTS="-Xms512m -Xmx1024m \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=200 \
    -Djava.security.egd=file:/dev/./urandom"

# Порт для документации
EXPOSE 8080

# CMD ["catalina.sh", "run"]