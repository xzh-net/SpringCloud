# Eureka注册中心


```bash
mvn clean compile
mvn clean package
```

```
<spring-boot-dependencies.version>2.1.7.RELEASE</spring-boot-dependencies.version>
<spring-cloud.version>Greenwich.SR5</spring-cloud.version>
```

```
spring-cloud-eureka
├── eureka-server -- 注册中心
├── config-server -- 配置中心
├── config-client -- 配置中心客户端
├── user-service  --  用户服务
├── ribbon-service -- ribbon整合
├── hystrix-service -- hystrix整合
├── feign-service  -- feign整合
├── hystrix-dashboard -- hystrix控制台
├── turbine-service -- hystrix聚合监控
├── admin-server -- 微服务监控
└── zuul-server -- 网关服务
```



## 1.  注册中心（eureka-server）



## 2.  配置中心（config-server）

验证地址：http://127.0.0.1:8001/config-client-dev.yml



## 3.  配置中心客户端（config-eureka）

- 访问地址：http://127.0.0.1:8101/index

- 通过https://gitee.com/xzh-net/config-repostory/blob/master/config-client-dev.yml修改配置内容

- 手动刷新，使用postman调用config-server服务中http://localhost:8001/actuator/bus-refresh，指定method为POST，参数contentType为application/json

- 本地配置文件转移到配置中心中，通过gitee配置config-client-pro.yml文件
```
server:
   port: 8101
spring:
   application:
      name: config-client
   rabbitmq: 
    host: 172.17.17.137
    port: 5672
    username: guest
    password: guest
eureka:
   instance:
      prefer-ip-address: true
      instance-id: ${spring.application.name}:${spring.cloud.client.ip-address}:${spring.application.instance_id:${server.port}}
      lease-renewal-interval-in-seconds: 5 #每隔几秒告诉eureka服务器我还存活，用于心跳检测 默认30 
      lease-expiration-duration-in-seconds: 10 #如果心跳检测一直没有发送，10秒后会从eureka服务器中将此服务剔除 默认90
   client:
      healthcheck:
         enabled: false
      serviceUrl:
         defaultZone: http://admin:admin@127.0.0.1:9001/eureka/
management:
   endpoints:
      web:
         exposure:
            include: '*'
   endpoint:
      health:
         show-details: always
name: this is pro
```

本地配置文件bootstrap.yml
```
spring:
  profiles:
    active: dev
  cloud:
    config: #Config客户端配置
      name: config-client #应用名称,需要对应git中配置文件名称的前半部分
      profile: pro #启用配置后缀名称
      label: master #分支名称
      discovery:
        enabled: true #开启服务发现
        service-id: config-server
```

> bus总线并非是修改了配置后所有客户端自动刷新，而是当多个应用引用一套配置得时候，通过bus-refresh只刷新一个客户端即可所有客户端全部更新。



## 4.  用户服务（user-service）

模拟基础用户的CRUD



## 5.  Ribbon整合（ribbon-service）

使用RestTemplate实现客户端的负载均衡



## 6. hystrix整合（hystrix-service）

使用RestTemplate整合hystrix

监控端点:http://localhost:8401/actuator/hystrix.stream



## 7.  Feign整合（feign-service）

使用@FeignClient实现客户端的负载均衡

feign同时整合了hystrix和ribbon,必须同时配置ribbon的超时时间,可能是bug

```
### Ribbon 配置
ribbon:
  # 连接超时
  ConnectTimeout: 2000
  # 响应超时
  ReadTimeout: 5000
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 5000 #默认的连接超时时间1秒,若1秒没有返回数据,自动的触发降级逻辑
      circuitBreaker:
        enabled: true
        requestVolumeThreshold: 5
        errorThresholdPercentage: 10
        sleepWindowInMilliseconds: 10000
```

```
@FeignClient(value = "user-service",fallback = UserFallbackService.class)
public interface UserService {
    @PostMapping("/user/create")
    CommonResult create(@RequestBody User user);
}

```



## 8. hystrix控制台（hystrix-dashboard）

访问地址:http://127.0.0.1:8501/hystrix

输入端点:http://127.0.0.1:8401/actuator/hystrix.stream




## 9. 聚合监控Turbine（turbine-service）

配置所有需要监控的微服务到统一收集点

监控端点:http://127.0.0.1:8601/turbine.stream

访问地址:http://127.0.0.1:8501/hystrix



## 10. 网关服务（zuul-server）

访问地址：http://127.0.0.1:8801/proxy/userService/user/1

路由名称和服务名称完全一致的情况下可以使用默认路由规则

```
zuul:
  routes: #给服务配置路由
    user-service:
      path: /user-service/**
    feign-service:
      path: /feign-service/**

```

如果不想使用默认的路由规则，可以添加以下配置来忽略默认路由配置

```
zuul:
  ignored-services: user-service,feign-service #关闭默认路由配置
```

查看单路由信息

http://127.0.0.1:8801/actuator/route 

http://localhost:8801/actuator/routes/details




## 11. 微服务应用监控（admin-server）

访问地址：http://127.0.0.1:8901

- admin-starter必须和boot版本一致，但不必和服务端保持一致

