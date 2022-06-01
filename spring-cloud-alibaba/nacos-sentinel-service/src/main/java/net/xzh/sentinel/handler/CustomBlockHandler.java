package net.xzh.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

import net.xzh.sentinel.common.CommonResult;

/**
 * 此类型用来处理限流自定义逻辑
 * @author CR7
 */

public class CustomBlockHandler {
	public static CommonResult handlerException1(BlockException exception){
        return CommonResult.failed("handlerException1：系统异常，请稍后重试！");
    }
}