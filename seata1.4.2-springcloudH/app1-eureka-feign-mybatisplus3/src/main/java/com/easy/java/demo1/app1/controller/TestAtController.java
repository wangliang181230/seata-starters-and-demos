package com.easy.java.demo1.app1.controller;

import com.easy.java.demo1.app1.business.mapper.TestTable1Mapper;
import com.easy.java.demo1.app1.consumer.Demo1Application2AtControllerFeignClient;
import com.easy.java.demo1.app1.domain.entity.TestTable1;
import com.easy.java.starter.seata.util.SeataUtil;
import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试AT模式
 *
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:40
 */
@RestController
@RequestMapping("/test/at")
public class TestAtController {

	@Autowired
	private TestTable1Mapper mapper;

	@Autowired
	private Demo1Application2AtControllerFeignClient feignClient;

	@GlobalTransactional
	@GetMapping("/success")
	public String testSuccess() {
		TestTable1 entity = new TestTable1();
		entity.setName("xxx");
		mapper.insert(entity);
		SeataUtil.print("insert xxx");

		feignClient.insertAaa(false);
		feignClient.insertBbb(false);

		return "测试成功";
	}

	/**
	 * 该方法没有纳入全局事务的管理，
	 * 所以需要添加 @GlobalLock + @Transactional + select for update 来保证数据不被脏写。
	 *
	 * @param id
	 * @return
	 */
	@GlobalLock
	@Transactional
	@GetMapping("/test2")
	public String test2(String id) {
		TestTable1 entity = mapper.selectByIdForUpdate(id);
		entity.setName("222324234");
		mapper.updateById(entity);
		return "测试更新成功";
	}

	@GlobalTransactional
	@GetMapping("/error")
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
	@GetMapping("/error2")
	public void testError2() {
		TestTable1 entity = new TestTable1();
		entity.setName("zzz");
		mapper.insert(entity);
		SeataUtil.print("insert zzz");

		feignClient.insertAaa(false);
		feignClient.insertBbb(true); // 由微服务端抛出异常
	}

}