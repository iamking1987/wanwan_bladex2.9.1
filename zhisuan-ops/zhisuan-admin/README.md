## SDK下载

#### Java SDK 下载
    下载SDK: https://open-doc.dingtalk.com/microapp/faquestions/vzbp02
    
## 配置项

#### bootstrap.yml

```
# 监控的相关配置
monitor:
  ding-talk:
    enabled: false
    # 用于自定义域名，默认会自动填充为 http://ip:port
    link: http://localhost:${server.port}
    # 钉钉配置的令牌
    access-token: xxx
    # 如果采用密钥形式，需要添加，否则需要去掉该参数
    secret: xxx
```
