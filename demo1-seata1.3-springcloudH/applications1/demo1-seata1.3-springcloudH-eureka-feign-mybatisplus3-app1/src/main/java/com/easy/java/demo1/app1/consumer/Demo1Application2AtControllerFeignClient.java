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
@RequestMapping("/test/at")
public interface Demo1Application2AtControllerFeignClient {

	@GetMapping("/insert/aaa")
	long insertAaa(@RequestParam(required = false) Boolean throwException);

	@GetMapping("/insert/bbb")
	long insertBbb(@RequestParam(required = false) Boolean throwException);

	@GetMapping("/delete/aaa")
	void deleteAaa();

	@GetMapping("/delete/bbb")
	void deleteBbb();

	@GetMapping("/delete")
	void delete(long id);

}