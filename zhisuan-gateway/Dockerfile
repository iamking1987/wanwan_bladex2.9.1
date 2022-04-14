FROM zhisuanx/alpine-java:openjdk8-openj9_cn_slim

MAINTAINER zhisuanjava@qq.com

RUN mkdir -p /zhisuan/gateway

WORKDIR /zhisuan/gateway

EXPOSE 80

ADD ./target/zhisuan-gateway.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
