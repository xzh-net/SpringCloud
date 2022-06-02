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
