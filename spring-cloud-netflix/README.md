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
├── eureka-server -- 注册中心9001
├── config-server -- 配置中心8001
├── config-client -- 配置中心客户端8101
├── user-service  --  用户服务8201
├── ribbon-service -- ribbon整合8301
├── hystrix-service -- hystrix整合8401
├── feign-service  -- feign整合8701
├── hystrix-dashboard -- hystrix控制台8501
├── turbine-service -- hystrix聚合监控8601
├── admin-server -- 微服务监控8901
└── gateway-server -- gateway网关服务8801
└── zuul-server -- zuul网关服务8801
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


## 10. 微服务应用监控（admin-server）

访问地址：http://127.0.0.1:8901

- admin-starter必须和boot版本一致，但不必和服务端保持一致

## 11. zuul网关服务（zuul-server）

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

## 11. gateway网关服务（gateway-server）

- 路由

  - 手动配置规则，其中RewritePath的效果和使用StripPrefix类似

    ```
    server:
      port: 8801
    spring:
      application:
        name: gateway-server
      cloud: #配置SpringCloudGateway的路由
        gateway:
          routes:
          - id: user-service
            uri: lb://user-service
            predicates:
              - Path=/user-server/**
            filters:
              - PreserveHostHeader
              - RewritePath=/user-server/(?<segment>.*), /$\{segment}
          - id: feign-service
            uri: lb://feign-service
            predicates:
              - Path=/user-service/**
            filters:
              - StripPrefix=1
              - PreserveHostHeader
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
    logging:
      level:
        org.springframework.cloud.gateway: debug
    hystrix:
      command:
        default:
          execution:
            isolation:
              thread:
                timeoutInMilliseconds: 1000
    ```

    

  - 动态路由默认对应规则

    ```
    server:
      port: 8801
    spring:
      application:
        name: gateway-server
      cloud:
        gateway:
          discovery:
            locator:
              enabled: true #开启从注册中心动态创建路由的功能
              lower-case-service-id: true #使用小写服务名，默认是大写
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
    logging:
      level:
        org.springframework.cloud.gateway: debug
    ```

    

- 断言

- 过滤器

- 限流

  - 基于redis令牌桶限流

    ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>
    ```

    ```
    package net.xzh.gateway.config;
    
    import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.server.ServerWebExchange;
    import reactor.core.publisher.Mono;
    
    /**
     * 用作gateway自带的redis令牌桶限流功能 可用sentinel替代
     * 
     * @author CR7
     *
     */
    @Configuration
    public class KeyResolverConfiguration {
    
    	/**
    	 * 基于请求路径的限流
    	 */
    	@Bean
    	public KeyResolver pathKeyResolver() {
    		return new KeyResolver() {
    			@Override
    			public Mono<String> resolve(ServerWebExchange exchange) {
    				return Mono.just(exchange.getRequest().getPath().toString());
    			}
    		};
    	}
    
    	/**
    	 * 基于请求参数的限流
    	 *
    	 */
    
    //	@Bean
    	public KeyResolver paramResolver() {
    		return new KeyResolver() {
    			@Override
    			public Mono<String> resolve(ServerWebExchange exchange) {
    				return Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    			}
    		};
    	}
    
    	/**
    	 * 基于请求ip地址的限流 暂时未能使用
    	 */
    //	@Bean
    	public KeyResolver ipKeyResolver() {
    		return new KeyResolver() {
    			@Override
    			public Mono<String> resolve(ServerWebExchange exchange) {
    				return Mono.just(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"));
    			}
    		};
    	}
    
    	/**
    	 * 基于用户的限流
    	 */
    //	@Bean
    	public KeyResolver userKeyResolver() {
    		return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    	}
    
    }
    ```

    ```
    server:
      port: 8801
    spring:
      application:
        name: gateway-server
      cloud: #配置SpringCloudGateway的路由
        gateway:
          routes:
          - id: user-service
            uri: lb://user-service
            predicates:
              - Path=/user-server/**
            filters:
              - PreserveHostHeader
              - RewritePath=/user-server/(?<segment>.*), /$\{segment}
              - name: RequestRateLimiter
                args:
                  # 使用SpEL从容器中获取对象
                  key-resolver: '#{@pathKeyResolver}' #当需要用到这里的对象时需要去filter包下的KeyResolverConfiguration类中开启对应的限流bean
                  # 令牌桶每秒填充平均速率
                  redis-rate-limiter.replenishRate: 1
                  # 令牌桶的总容量
                  redis-rate-limiter.burstCapacity: 3
          - id: feign-service222
            uri: lb://feign-service
            predicates:
              - Path=/feign-server/**
            filters:
              - PreserveHostHeader
              - RewritePath=/feign-server/(?<segment>.*), /$\{segment}
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
    logging:
      level:
        org.springframework.cloud.gateway: debug
    hystrix:
      command:
        default:
          execution:
            isolation:
              thread:
                timeoutInMilliseconds: 1000
    ```

    访问地址：http://localhost:8801/user-server/user/2

  - 基于Sentinel的限流

    ```
    <!-- sentinel限流依赖 -->
    <dependency>
    	<groupId>com.alibaba.csp</groupId>
    	<artifactId>sentinel-spring-cloud-gateway-adapter</artifactId>
    	<version>1.7.2</version>
    </dependency>
    ```

    按服务名称和自定义api两个维度进行配置

    ```
    package net.xzh.gateway.config;
    
    import java.util.Collections;
    import java.util.HashMap;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Map;
    import java.util.Set;
    
    import javax.annotation.PostConstruct;
    
    import org.springframework.beans.factory.ObjectProvider;
    import org.springframework.cloud.gateway.filter.GlobalFilter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.Ordered;
    import org.springframework.core.annotation.Order;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.codec.ServerCodecConfigurer;
    import org.springframework.web.reactive.function.BodyInserters;
    import org.springframework.web.reactive.function.server.ServerResponse;
    import org.springframework.web.reactive.result.view.ViewResolver;
    import org.springframework.web.server.ServerWebExchange;
    
    import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
    import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
    import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
    import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
    import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
    import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
    import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
    import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
    import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
    import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
    import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
    
    import reactor.core.publisher.Mono;
    
    @Configuration
    public class GatewayConfiguration {
    
        private final List<ViewResolver> viewResolvers;
        private final ServerCodecConfigurer serverCodecConfigurer;
    
        public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
            this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
            this.serverCodecConfigurer = serverCodecConfigurer;
        }
    
        /**
         * 配置限流的异常处理器:SentinelGatewayBlockExceptionHandler
         */
        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
            return new SentinelGatewayBlockExceptionHandler(viewResolvers,serverCodecConfigurer);
        }
        /**
         * 配置限流过滤器
         */
        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        public GlobalFilter sentinelGatewayFilter() {
            return new SentinelGatewayFilter();
        }
        /**
         * 配置初始化的限流参数
         */
        @PostConstruct
        public void initGatewayRules() {
            Set<GatewayFlowRule> rules = new HashSet<>();
            rules.add(new GatewayFlowRule("user-service").setCount(1).setIntervalSec(1));
            
            rules.add(new GatewayFlowRule("user_api").setCount(1).setIntervalSec(1));
            rules.add(new GatewayFlowRule("feign_api").setCount(1).setIntervalSec(1));
            GatewayRuleManager.loadRules(rules);
        }
    
        // 自定义API分组
        @PostConstruct
        private void initCustomizedApis() {
        	Set<ApiDefinition> definitions = new HashSet<>();
    		ApiDefinition api1 = new ApiDefinition("user_api")
    				.setPredicateItems(new HashSet<ApiPredicateItem>() {{
    					add(new ApiPathPredicateItem().setPattern("/user-service/user/**"). //已/user-service/user/开都的所有url
    							setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
    				}});
    		ApiDefinition api2 = new ApiDefinition("feign_api")
    				.setPredicateItems(new HashSet<ApiPredicateItem>() {{
    					add(new ApiPathPredicateItem().setPattern("/feign-service/user/getByUsername")); //完全匹配/feign-service/user/getByUsername的url
    				}});
    		definitions.add(api1);
    		definitions.add(api2);
            GatewayApiDefinitionManager.loadApiDefinitions(definitions);
        }
    
        //自定义异常提示
        @PostConstruct
        public void initBlockHandlers() {
            BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
                public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                    Map map = new HashMap<>();
                    map.put("code", 500);
                    map.put("message", "已限流，请稍后重试");
                    return ServerResponse.status(HttpStatus.OK).//状态码200
                            contentType(MediaType.APPLICATION_JSON_UTF8).//application/json;charset=UTF-8
                            body(BodyInserters.fromObject(map));
                }
            };
            GatewayCallbackManager.setBlockHandler(blockRequestHandler);
        }
    }
    ```

    访问地址：http://localhost:8801/feign-service/user/getByUsername?username=xzh

  


