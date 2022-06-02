# Spring Cloud


```
spring-cloud-dubbo 7000
spring-cloud-netflix 8000
spring-cloud-alibaba 9000
spring-cloud-consul
spring-cloud-knife4j
```




# 目录结构
> 调试

```lua
root
├── common (模块级)
│   ├── annotation
│   ├── aspect
│   ├── component
│   ├── config
│   ├── constant
│   ├── context
│   ├── exception
│   ├── extension
│   ├── feign
│   ├── model
│   ├── auth
│   ├── token
│   ├── properties
│   ├── lock
│   ├── resolver
│   ├── service
│   └── utils
├── controller (项目级)
│   ├── BaseAction.java
│   ├── SysUserController.java
│   ├── UserAccountController.java
│   ├── SysMenuController.java
│   └── SysRoleController.java
├── config (项目级)
│   ├── MyBatisConfig.java
│   ├── Swagger2Config.java
│   └── RedisExpireConfig.java
├── properties
├── dto
├── model
├── dao
├── mapper
├── service
└── utils
```