package net.xzh.sentinel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

import net.xzh.sentinel.common.CommonResult;

/**
 * 熔断功能
 */
@RestController
@RequestMapping("/breaker")
public class CircleBreakerController {

	private Logger LOGGER = LoggerFactory.getLogger(CircleBreakerController.class);
	@Autowired
	private RestTemplate restTemplate;
	@Value("${service-url.user-service}")
	private String userServiceUrl;

	@RequestMapping("/fallback/{id}")
	@SentinelResource(value = "fallback", fallback = "handleFallback")
	public CommonResult fallback(@PathVariable Long id) {
		return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
	}

	@RequestMapping("/fallbackException/{id}")
	@SentinelResource(value = "fallbackException", fallback = "handleFallback2", exceptionsToIgnore = {
			NullPointerException.class })
	public CommonResult fallbackException(@PathVariable Long id) {
		if (id == 1) {
			throw new IndexOutOfBoundsException();
		} else if (id == 2) {
			throw new NullPointerException();
		}
		return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
	}

    
	public CommonResult handleFallback(Long id) {
		return CommonResult.failed("服务不可用降级");
	}

	public CommonResult handleFallback2(@PathVariable Long id, Throwable e) {
		LOGGER.error("handleFallback2 id:{},throwable class:{}", id, e.getClass());
		return CommonResult.failed("服务不可用降级2");
	}
}
