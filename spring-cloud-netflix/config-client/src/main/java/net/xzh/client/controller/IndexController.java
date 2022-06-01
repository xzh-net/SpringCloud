package net.xzh.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // 开启动态刷新
public class IndexController{

	@Value("${name}")
	private String name;

	@RequestMapping(value = "/index")
	public String test() {
		return name;
	}

}