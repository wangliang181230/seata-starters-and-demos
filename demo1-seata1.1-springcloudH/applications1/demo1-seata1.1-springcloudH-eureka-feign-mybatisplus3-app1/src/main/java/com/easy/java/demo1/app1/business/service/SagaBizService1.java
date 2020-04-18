package com.easy.java.demo1.app1.business.service;

import com.easy.java.demo1.app1.business.mapper.TestTable1Mapper;
import com.easy.java.demo1.app1.domain.entity.TestTable1;
import com.easy.java.starter.seata.util.SeataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("sagaBizService1")
public class SagaBizService1 {

	@Autowired
	private TestTable1Mapper mapper;

	public boolean doBiz() throws Throwable {
		TestTable1 entity = new TestTable1();
		entity.setName("111");
		mapper.insert(entity);
		SeataUtil.print("saga biz service1: insert name=111");

		return true;
	}

	public boolean compensate() {
		Map<String, Object> params = new HashMap<>();
		params.put("name", "111");
		mapper.deleteByMap(params);
		SeataUtil.print("saga biz service1: delete name=111");

		return true;
	}

}