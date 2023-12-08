# cqwm-java
- 运行环境
  - JDK 1.8
  - Maven
  - Mysql
  - Redis
  - Nginx

# 目录说明

```bash
- sky_take_out
    - mp-weixin		——微信小程序
    - nginx-1.20.2  ——网页端前端 直接启动nginx
    - sky-common    ——后端公共模块 工具类 异常 配置类 枚举
    - sky-pojo      ——后端实体类 通信类 交互类
    - sky-server    ——后端服务
    - sql           ——sql文件，数据库设计文档
    - 接口文档                
    - 音频           ——小程序下单 催单音频
```



# 快速启动

- 数据中导入sql文件
- 启动Redis

- 更改``xxx-dev.ym``配置文件数据库连接配置

```YML
sky:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: xxx
    port: xxx
    database: xxx
    username: xxx
    password: xxx
```



- 无阿里云OSS服务器，``xxx-dev.ym``配置文件中更改文件上传改为本地文件上传

```YML
sky:
  file:
    upload-dir: E://MyProject//cqwm//nginx-1.20.2//html//sky//static
```



- 微信小程序登录以及支付可以修改（本工程内是模拟点击支付后，直接修改订单状态为已支付）

```YML
sky:
  wechat:
    appid: xxx
    secret: xxx
    mchid : xxx
    mchserialNo: xxx
    privateKeyFilePath: xxx
    apiV3Key: xxx
    weChatPayCertFilePath: xx
    notifyUrl: xxx
    refundNotifyUrl: xxx
```



- Nignx映射端口 80 启动后直接访问 `localhost`