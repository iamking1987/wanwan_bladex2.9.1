FROM zhisuanx/alpine-java:openjdk8-openj9_cn_slim

MAINTAINER zhisuanjava@qq.com

RUN mkdir -p /zhisuan/turbine

WORKDIR /zhisuan/turbine

EXPOSE 7003

ADD ./target/zhisuan-turbine.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
