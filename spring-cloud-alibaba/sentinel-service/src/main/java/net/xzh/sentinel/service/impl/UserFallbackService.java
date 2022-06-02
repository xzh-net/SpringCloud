package net.xzh.sentinel.service.impl;

import org.springframework.stereotype.Component;

import net.xzh.sentinel.common.CommonResult;
import net.xzh.sentinel.domain.User;
import net.xzh.sentinel.service.UserService;

/**
 * Created by macro on 2019/9/5.
 */
@Component
public class UserFallbackService implements UserService {

	
	public CommonResult create(User user) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return CommonResult.failed("服务降级返回");
    }

    public CommonResult<User> getUser(Long id) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return CommonResult.failed("服务降级返回");
    }

    public CommonResult<User> getByUsername(String username) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return CommonResult.failed("服务降级返回");
    }

    public CommonResult update(User user) {
    	return CommonResult.failed("调用失败，服务被降级");
    }

    public CommonResult delete(Long id) {
    	return CommonResult.failed("调用失败，服务被降级");
    }

}
