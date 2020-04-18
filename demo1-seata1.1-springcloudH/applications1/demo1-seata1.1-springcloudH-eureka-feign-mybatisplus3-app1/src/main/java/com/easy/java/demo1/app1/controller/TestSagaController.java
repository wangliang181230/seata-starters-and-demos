package com.easy.java.demo1.app1.controller;

import io.seata.saga.engine.AsyncCallback;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateMachineInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试SAGA模式
 *
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 21:50
 */
@RestController
@RequestMapping("/test/saga")
public class TestSagaController {

	@Autowired(required = false)
	private StateMachineEngine stateMachineEngine;

	@GetMapping("/success")
	public String testSuccess() {
		if (stateMachineEngine == null) {
			return "请先启用SAGA模式并禁用AT模式";
		}

		String stateMachineName = "testSaga"; // 对应statelang/test-saga.json里的"Name"的值。
		String tenantId = null; // 分组ID，为空时默认为StateMachineConfig里配置的DefaultTenantId的值
		Map<String, Object> startParams = new HashMap<>(); // 不可为null
		// 同步执行，返回状态机实例
		StateMachineInstance inst = stateMachineEngine.start(stateMachineName, tenantId, startParams);
		if (ExecutionStatus.SU.equals(inst.getStatus())) {
			return "测试成功";
		} else {
			return "测试失败，状态机实例的当前状态为：" + inst.getStatus().name();
		}
	}

	@GetMapping("/error")
	public String testError() {
		if (stateMachineEngine == null) {
			return "请先启用SAGA模式并禁用AT模式";
		}

		String stateMachineName = "testSaga"; // 对应statelang/test-saga.json里的"Name"的值。
		String tenantId = null; // 分组ID，为空时默认为StateMachineConfig里配置的DefaultTenantId的值
		Map<String, Object> startParams = new HashMap<>();
		startParams.put("throwException", true); // 故意抛出异常
		AsyncCallback callback = null; // 异步执行结果的callback
		// 异步执行，返回状态机实例
		StateMachineInstance inst = stateMachineEngine.startAsync(stateMachineName, tenantId, startParams, callback);
		if (ExecutionStatus.RU.equals(inst.getStatus())) {
			return "正在异步";
		} else {
			return "启用，状态机实例的当前状态为：" + inst.getStatus().name();
		}
	}

}