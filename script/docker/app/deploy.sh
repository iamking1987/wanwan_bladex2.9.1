#!/bin/bash

#使用说明，用来提示输入参数
usage() {
	echo "Usage: sh 执行脚本.sh [port|mount|base|monitor|modules|prometheus|alertmanager|stop|rm|rmiNoneTag]"
	exit 1
}

#开启所需端口
port(){
	#gateway
	firewall-cmd --add-port=88/tcp --permanent
	#web
	firewall-cmd --add-port=8000/tcp --permanent
	#nacos
	firewall-cmd --add-port=8848/tcp --permanent
	firewall-cmd --add-port=9848/tcp --permanent
	firewall-cmd --add-port=9849/tcp --permanent
	#sentinel
	firewall-cmd --add-port=8858/tcp --permanent
	#grafana
	firewall-cmd --add-port=3000/tcp --permanent
	#mysql
	firewall-cmd --add-port=3306/tcp --permanent
	#redis
	firewall-cmd --add-port=3379/tcp --permanent
	#admin
	firewall-cmd --add-port=7002/tcp --permanent
	#turbine
	firewall-cmd --add-port=7003/tcp --permanent
	#ureport
	firewall-cmd --add-port=8108/tcp --permanent
	#zipkin
	firewall-cmd --add-port=9411/tcp --permanent
	#prometheus
	firewall-cmd --add-port=9090/tcp --permanent
	#swagger
	firewall-cmd --add-port=18000/tcp --permanent
	#firewalld
	service firewalld restart
}

##放置挂载文件
mount(){
	#挂载配置文件
	if test ! -f "/docker/nginx/api/nginx.conf" ;then
		mkdir -p /docker/nginx/api
		cp nginx/api/nginx.conf /docker/nginx/api/nginx.conf
	fi
	if test ! -f "/docker/nginx/web/nginx.conf" ;then
		mkdir -p /docker/nginx/web
		cp nginx/web/nginx.conf /docker/nginx/web/nginx.conf
		cp -r nginx/web/html /docker/nginx/web/html
	fi
	if test ! -f "/docker/nacos/init.d/custom.properties" ;then
		mkdir -p /docker/nacos/init.d
		cp nacos/init.d/custom.properties /docker/nacos/init.d/custom.properties
	fi
	if test ! -f "/docker/prometheus/prometheus.yml" ;then
		mkdir -p /docker/prometheus
		cp prometheus/config/prometheus.yml /docker/prometheus/prometheus.yml
	fi
	if test ! -f "/docker/prometheus/rules/alert_rules.yml" ;then
		mkdir -p /docker/prometheus/rules
		cp prometheus/config/alert_rules.yml /docker/prometheus/rules/alert_rules.yml
	fi
	if test ! -f "/docker/grafana/grafana.ini" ;then
		mkdir -p /docker/grafana
		cp prometheus/config/grafana.ini /docker/grafana/grafana.ini
	fi
	if test ! -f "/docker/alertmanager/alertmanager.yml" ;then
		mkdir -p /docker/alertmanager
		cp prometheus/config/alertmanager.yml /docker/alertmanager/alertmanager.yml
	fi
	if test ! -f "/docker/alertmanager/templates/wechat.tmpl" ;then
		mkdir -p /docker/alertmanager/templates
		cp prometheus/config/wechat.tmpl /docker/alertmanager/templates/wechat.tmpl
	fi
	if test ! -f "/docker/webhook_dingtalk/dingtalk.yml" ;then
		mkdir -p /docker/webhook_dingtalk
		cp prometheus/config/dingtalk.yml /docker/webhook_dingtalk/dingtalk.yml
	fi
	#增加目录权限
	chmod -R 777 /docker/prometheus
	chmod -R 777 /docker/grafana
	chmod -R 777 /docker/alertmanager
}

#启动基础模块
base(){
	docker-compose up -d nacos sentinel seata-server web-nginx zhisuan-nginx zhisuan-redis
}

#启动监控模块
monitor(){
	docker-compose up -d zhisuan-admin zhisuan-turbine
}

#启动程序模块
modules(){
	docker-compose up -d zhisuan-gateway1 zhisuan-gateway2 zhisuan-auth1 zhisuan-auth2 zhisuan-swagger zhisuan-report zhisuan-user zhisuan-desk zhisuan-system zhisuan-log zhisuan-flow zhisuan-resource
}

#启动普罗米修斯模块
prometheus(){
	docker-compose up -d prometheus node-exporter mysqld-exporter cadvisor grafana
}

#启动监听模块
alertmanager(){
	docker-compose up -d alertmanager webhook-dingtalk
}

#关闭所有模块
stop(){
	docker-compose stop
}

#删除所有模块
rm(){
	docker-compose rm
}

#删除Tag为空的镜像
rmiNoneTag(){
	docker images|grep none|awk '{print $3}'|xargs docker rmi -f
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"port")
	port
;;
"mount")
	mount
;;
"base")
	base
;;
"monitor")
	monitor
;;
"modules")
	modules
;;
"prometheus")
	prometheus
;;
"alertmanager")
	alertmanager
;;
"stop")
	stop
;;
"rm")
	rm
;;
"rmiNoneTag")
	rmiNoneTag
;;
*)
	usage
;;
esac
