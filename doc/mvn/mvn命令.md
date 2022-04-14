mvn install:install-file -Dfile=zhisuan-core-1.0.jar -DgroupId=org.springzhisuan -DartifactId=zhisuan-core -Dversion=1.0 -Dpackaging=jar

mvn deploy:deploy-file -DgroupId=com.zhuozhengsoft -DartifactId=pageoffice -Dversion=4.5.0.2  -Dpackaging=jar  -Dfile=pageoffice4.5.0.2.jar -Durl=http://nexus.zhisuanx.vip/repository/maven-releases/ -DrepositoryId=release
