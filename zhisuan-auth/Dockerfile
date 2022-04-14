#Docker镜像可以选择以下三种，前两种已经内置字体，第三种为原生的openjdk8-openj9镜像
#1.FROM zhisuanx/alpine-java:8_server-jre_cn_unlimited
#2.FROM zhisuanx/alpine-java:openjdk8-openj9_cn_slim
#3.FROM adoptopenjdk/openjdk8-openj9:jdk8u262-b10_openj9-0.21.0-alpine-slim
#第3点的tag可以到该地址查询：https://hub.docker.com/r/adoptopenjdk/openjdk8-openj9/tags?page=1&ordering=last_updated&name=alpine-slim
#版本介绍一览：https://hub.docker.com/r/adoptopenjdk/openjdk8-openj9
FROM zhisuanx/alpine-java:openjdk8-openj9_cn_slim

MAINTAINER zhisuanjava@qq.com

RUN mkdir -p /zhisuan/auth

WORKDIR /zhisuan/auth

EXPOSE 8100

ADD ./target/zhisuan-auth.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
