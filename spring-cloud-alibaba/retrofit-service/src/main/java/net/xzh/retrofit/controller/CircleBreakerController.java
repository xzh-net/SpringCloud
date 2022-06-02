package net.xzh.retrofit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

import net.xzh.retrofit.domain.CommonResult;
import net.xzh.retrofit.domain.User;
import net.xzh.retrofit.service.UserService;

/**
 * 熔断降级
 */
@RestController
@RequestMapping("/breaker")
public class CircleBreakerController {

	private Logger LOGGER = LoggerFactory.getLogger(CircleBreakerController.class);

	@Autowired
	private UserService userService;

	@SentinelResource(value = "fallback", fallback = "handleFallback")
	@RequestMapping(value = "/fallback/{id}", method = RequestMethod.GET)
	public CommonResult fallback(@PathVariable Long id) {
		return userService.getUser(id);
	}

	@SentinelResource(value = "fallbackException", fallback = "handleFallback2", exceptionsToIgnore = {
			NullPointerException.class })
	@RequestMapping(value = "/fallbackException/{id}", method = RequestMethod.GET)
	public CommonResult fallbackException(@PathVariable Long id) {
		if (id == 1) {
			throw new IndexOutOfBoundsException();
		} else if (id == 2) {
			throw new NullPointerException();
		}
		return userService.getUser(id);
	}

	public CommonResult handleFallback(Long id) {
		User defaultUser = new User(-1L, "defaultUser", "123456");
		return new CommonResult<>(defaultUser, "服务降级返回", 200);
	}

	public CommonResult handleFallback2(@PathVariable Long id, Throwable e) {
		LOGGER.error("handleFallback2 id:{},throwable class:{}", id, e.getClass());
		User defaultUser = new User(-2L, "defaultUser2", "123456");
		return new CommonResult<>(defaultUser, "服务降级返回", 200);
	}
}
