FROM zhisuanx/alpine-java:8_server-jre_cn_unlimited

MAINTAINER zhisuanjava@qq.com

RUN mkdir -p /zhisuan/flow

WORKDIR /zhisuan/flow

EXPOSE 8008

ADD ./target/zhisuan-flow.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
