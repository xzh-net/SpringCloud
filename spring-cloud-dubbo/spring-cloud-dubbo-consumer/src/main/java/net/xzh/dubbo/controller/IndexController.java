package net.xzh.dubbo.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.xzh.dubbo.service.ProductService;

@RestController
public class IndexController {
	// 服务引用
	@Reference
	private ProductService productService;

	@GetMapping("/test")
	private String send(@RequestParam int id) {
		return "nacos集成->" + productService.findByPid(id).toString();
	}
}