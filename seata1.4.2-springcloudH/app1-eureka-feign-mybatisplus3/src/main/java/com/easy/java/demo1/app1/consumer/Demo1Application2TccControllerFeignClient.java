package com.easy.java.demo1.app1.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 3:00
 */
@Component
@FeignClient("demo1-application2")
@RequestMapping("/test/tcc")
public interface Demo1Application2TccControllerFeignClient {

	@GetMapping("/dobiz/success")
	void doBizSuccess(@RequestParam Long payMoney);

	@GetMapping("/dobiz/error")
	void duBizError(@RequestParam Long payMoney);


}