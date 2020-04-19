package com.easy.java.demo1.app1.business.service;

import com.easy.java.demo1.app1.consumer.Demo1Application2AtControllerFeignClient;
import com.easy.java.starter.seata.util.SeataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sagaBizService1")
public class SagaBizService1 {

	@Autowired
	private Demo1Application2AtControllerFeignClient atFeignClient;

	public boolean doBiz() throws Throwable {
		atFeignClient.insertAaa(false);
		SeataUtil.print("saga biz service1: insert name=aaa in application2");

		return true;
	}

	public boolean compensate() {
		atFeignClient.deleteAaa();
		SeataUtil.print("saga biz service1: delete name=aaa in application2");

		return true;
	}

}