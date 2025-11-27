# 1. 基础镜像：使用官方的 OpenJDK 17 精简版 (Alpine Linux)
# 这里选择 eclipse-temurin 是因为它更轻量、安全且开源协议友好
FROM eclipse-temurin:17-jre-alpine

# 2. 作者信息
LABEL maintainer="himwei"

# 3. 设置工作目录
WORKDIR /app

# 4. 挂载临时目录 (Spring Boot 内嵌 Tomcat 需要)
VOLUME /tmp

# 5. 复制构建好的 Jar 包到镜像中
# 注意：这里假设 Maven 打包后的名字是 app.jar，后面会在 pom.xml 里配置
COPY target/*.jar app.jar

# 6. 设置时区 (解决 Linux 容器时间差 8 小时的问题)
# 如果是 Alpine 镜像，需要手动安装 tzdata
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 7. 暴露端口 (和 application.yml 保持一致)
EXPOSE 8123

# 8. 启动命令
# -Djava.security.egd... 是为了加快随机数生成，提升启动速度
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
