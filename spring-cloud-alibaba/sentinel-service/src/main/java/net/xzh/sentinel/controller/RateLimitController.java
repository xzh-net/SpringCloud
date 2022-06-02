package net.xzh.sentinel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import net.xzh.sentinel.common.CommonResult;
import net.xzh.sentinel.handler.CustomBlockHandler;

/**
 * 限流功能
 */
@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {

	/**
	 * 按资源名称限流，需要指定限流处理逻辑
	 */
	@GetMapping("/byResource")
	@SentinelResource(value = "byResource", blockHandler = "handleException")
	public CommonResult byResource() {
		return CommonResult.success("按资源名称限流");
	}

	/**
	 * 按URL限流，有默认的限流处理逻辑
	 */
	@GetMapping("/byUrl")
	@SentinelResource(value = "byUrl")
	public CommonResult byUrl() {
		return CommonResult.success("按url限流");
	}

	/**
	 * 自定义通用的限流处理逻辑
	 */
	@GetMapping("/customBlockHandler")
	@SentinelResource(value = "customBlockHandler", blockHandlerClass = CustomBlockHandler.class, blockHandler = "handlerException1")
	public CommonResult customBlockHandler() {
		return CommonResult.success("自定义限流");
	}

	public CommonResult handleException(BlockException exception) {
		return CommonResult.failed("系統繁忙");
	}

}
