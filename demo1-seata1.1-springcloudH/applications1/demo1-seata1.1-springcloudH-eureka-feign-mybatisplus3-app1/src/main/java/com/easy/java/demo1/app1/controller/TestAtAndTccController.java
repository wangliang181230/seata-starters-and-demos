package com.easy.java.demo1.app1.controller;

import com.easy.java.demo1.app1.business.mapper.TestTable1Mapper;
import com.easy.java.demo1.app1.consumer.Demo1Application2AtControllerFeignClient;
import com.easy.java.demo1.app1.consumer.Demo1Application2TccControllerFeignClient;
import com.easy.java.demo1.app1.domain.entity.TestTable1;
import com.easy.java.starter.seata.util.SeataUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试AT+TCC混合模式
 *
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:40
 */
@RestController
@RequestMapping("/test/tcc")
public class TestAtAndTccController {

	@Autowired
	private TestTable1Mapper mapper;

	@Autowired
	private Demo1Application2AtControllerFeignClient atFeignClient;
	@Autowired
	private Demo1Application2TccControllerFeignClient tccFeignClient;

	@GlobalTransactional
	@GetMapping("/success")
	public String testSuccess() {
		TestTable1 entity = new TestTable1();
		entity.setName("xxx");
		mapper.insert(entity);
		SeataUtil.print("insert xxx");

		tccFeignClient.doBiz(5L); // 支付5元

		atFeignClient.insertAaa(false);
		atFeignClient.insertBbb(false);

		return "测试成功";
	}

	@GlobalTransactional
	@GetMapping("/error")
	public void testError() {
		TestTable1 entity = new TestTable1();
		entity.setName("yyy");
		mapper.insert(entity);
		SeataUtil.print("insert yyy");

		tccFeignClient.doBiz(5L); // 支付5元

		atFeignClient.insertAaa(false);
		atFeignClient.insertBbb(false);

		SeataUtil.print("当前服务自己故意抛异常");
		throw new RuntimeException("当前服务自己故意抛异常");
	}

	@GlobalTransactional
	@GetMapping("/error2")
	public void testError2() {
		TestTable1 entity = new TestTable1();
		entity.setName("zzz");
		mapper.insert(entity);
		SeataUtil.print("insert zzz");

		tccFeignClient.doBiz(5L); // 支付5元

		atFeignClient.insertAaa(false);
		atFeignClient.insertBbb(true); // 由微服务端抛出异常
	}

}