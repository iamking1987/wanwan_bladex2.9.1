FROM zhisuanx/alpine-java:openjdk8-openj9_cn_slim

MAINTAINER zhisuanjava@qq.com

RUN mkdir -p /zhisuan/user

WORKDIR /zhisuan/user

EXPOSE 8102

ADD ./target/zhisuan-user.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
