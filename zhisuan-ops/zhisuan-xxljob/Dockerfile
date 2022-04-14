FROM zhisuanx/alpine-java:openjdk8-openj9_cn_slim

MAINTAINER zhisuanjava@qq.com

RUN mkdir -p /zhisuan/xxljob

WORKDIR /zhisuan/xxljob

EXPOSE 7008

ADD ./target/zhisuan-xxljob.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
