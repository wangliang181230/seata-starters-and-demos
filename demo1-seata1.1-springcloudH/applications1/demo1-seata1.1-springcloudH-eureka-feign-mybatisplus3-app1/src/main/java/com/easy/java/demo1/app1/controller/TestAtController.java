package com.easy.java.demo1.app1.controller;

import com.easy.java.demo1.app1.business.mapper.TestTable1Mapper;
import com.easy.java.demo1.app1.consumer.Demo1Application2FeignClient;
import com.easy.java.demo1.app1.domain.entity.TestTable1;
import com.easy.java.starter.seata.util.SeataUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试AT模式
 *
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:40
 */
@RestController
public class TestAtController {

	@Autowired
	private TestTable1Mapper mapper;

	@Autowired
	private Demo1Application2FeignClient feignClient;

	@GlobalTransactional
	@GetMapping("/test/at/success")
	public String testSuccess() {
		TestTable1 entity = new TestTable1();
		entity.setName("xxx");
		mapper.insert(entity);
		SeataUtil.print("insert xxx");

		feignClient.insertAaa(false);
		feignClient.insertBbb(false);

		return "测试成功";
	}

	@GlobalTransactional
	@GetMapping("/test/at/error")
	public void testError() {
		TestTable1 entity = new TestTable1();
		entity.setName("yyy");
		mapper.insert(entity);
		SeataUtil.print("insert yyy");

		feignClient.insertAaa(false);
		feignClient.insertBbb(false);

		SeataUtil.print("当前服务自己故意抛异常");
		throw new RuntimeException("当前服务自己故意抛异常");
	}

	@GlobalTransactional
	@GetMapping("/test/at/error2")
	public void testError2() {
		TestTable1 entity = new TestTable1();
		entity.setName("zzz");
		mapper.insert(entity);
		SeataUtil.print("insert zzz");

		feignClient.insertAaa(false);
		feignClient.insertBbb(true); // 由微服务端抛出异常
	}

}