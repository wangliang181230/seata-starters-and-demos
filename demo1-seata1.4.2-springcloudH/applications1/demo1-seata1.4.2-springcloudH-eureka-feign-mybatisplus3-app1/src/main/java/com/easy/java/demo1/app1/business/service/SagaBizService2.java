package com.easy.java.demo1.app1.business.service;

import com.easy.java.demo1.app1.business.mapper.TestTable1Mapper;
import com.easy.java.demo1.app1.domain.entity.TestTable1;
import com.easy.java.starter.seata.util.SeataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("sagaBizService2")
public class SagaBizService2 {

	@Autowired
	private TestTable1Mapper mapper;

	public boolean doBiz(Map<String, Object> params) {
		TestTable1 entity = new TestTable1();
		entity.setName("222");
		mapper.insert(entity);
		SeataUtil.print("saga biz service2: insert name=222");

		if (params != null && Boolean.TRUE.equals(params.get("doThrowException"))) {
			throw new RuntimeException("故意制造异常");
		}

		return true;
	}

	public boolean compensate() {
		Map<String, Object> params = new HashMap<>();
		params.put("name", "222");
		mapper.deleteByMap(params);
		SeataUtil.print("saga biz service2: delete name=222");

		return true;
	}

}