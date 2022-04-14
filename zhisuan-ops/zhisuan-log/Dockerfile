FROM zhisuanx/alpine-java:openjdk8-openj9_cn_slim

MAINTAINER zhisuanjava@qq.com

RUN mkdir -p /zhisuan/log

WORKDIR /zhisuan/log

EXPOSE 8103

ADD ./target/zhisuan-log.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
