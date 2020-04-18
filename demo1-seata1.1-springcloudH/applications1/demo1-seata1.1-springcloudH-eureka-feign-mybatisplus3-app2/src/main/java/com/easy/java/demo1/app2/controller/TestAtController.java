package com.easy.java.demo1.app2.controller;

import com.easy.java.demo1.app2.business.mapper.TestTable2Mapper;
import com.easy.java.demo1.app2.domain.entity.TestTable2;
import com.easy.java.starter.seata.util.SeataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试AT模式
 *
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:40
 */
@RestController
public class TestAtController {

	@Autowired
	private TestTable2Mapper mapper;

	@GetMapping("/test/insert/aaa")
	//@Transactional // 不添加事务，与insertBbb()方法做对比
	public long insertAaa(@RequestParam(required = false) Boolean throwException) {
		TestTable2 entity = new TestTable2();
		entity.setName("aaa");
		mapper.insert(entity);
		SeataUtil.print("insert aaa");

		TestTable2 entity2 = new TestTable2();
		entity2.setName("aaaaaa");
		mapper.insert(entity2);
		SeataUtil.print("insert aaaaaa");

		if (Boolean.TRUE.equals(throwException)) {
			SeataUtil.print("由微服务提供者抛出异常，使全局事务回滚: insertAaa()");
			throw new RuntimeException("由微服务提供者抛出异常，使全局事务回滚: insertAaa()");
		}

		SeataUtil.print("return aaa id");
		return entity.getId();
	}

	@GetMapping("/test/insert/bbb")
	@Transactional // 添加事务，当抛出异常时，当前分支事务将不会创建
	public long insertBbb(@RequestParam(required = false) Boolean throwException) {
		TestTable2 entity = new TestTable2();
		entity.setName("bbb");
		mapper.insert(entity);
		SeataUtil.print("insert bbb");

		TestTable2 entity2 = new TestTable2();
		entity2.setName("bbbbbb");
		mapper.insert(entity2);
		SeataUtil.print("insert bbbbbb");

		if (Boolean.TRUE.equals(throwException)) {
			SeataUtil.print("由微服务提供者抛出异常，使全局事务回滚: insertBbb()");
			throw new RuntimeException("由微服务提供者抛出异常，使全局事务回滚: insertBbb()");
		}

		SeataUtil.print("return bbb id");
		return entity.getId();
	}


	@GetMapping("/test/delete/aaa")
	public void deleteAaa() {
		SeataUtil.print("delete name=aaa");
		Map<String, Object> params = new HashMap<>();
		params.put("name", "aaa");
		mapper.deleteByMap(params);
	}

	@GetMapping("/test/delete/bbb")
	public void deleteBbb() {
		SeataUtil.print("delete name=bbb");
		Map<String, Object> params = new HashMap<>();
		params.put("name", "bbb");
		mapper.deleteByMap(params);
	}

	@GetMapping("/test/delete")
	public void delete(@RequestParam Long id) {
		SeataUtil.print("delete by id: " + id);
		mapper.deleteById(id);
	}

}