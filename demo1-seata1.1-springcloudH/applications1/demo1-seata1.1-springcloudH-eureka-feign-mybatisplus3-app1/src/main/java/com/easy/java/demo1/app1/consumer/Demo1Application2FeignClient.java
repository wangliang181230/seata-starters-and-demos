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
@RequestMapping
public interface Demo1Application2FeignClient {

	@GetMapping("/test/insert/aaa")
	long insertAaa(@RequestParam(required = false) Boolean throwException);

	@GetMapping("/test/insert/bbb")
	long insertBbb(@RequestParam(required = false) Boolean throwException);

	@GetMapping("/test/delete/aaa")
	void deleteAaa();

	@GetMapping("/test/delete/bbb")
	void deleteBbb();

	@GetMapping("/test/delete")
	void delete(long id);

}