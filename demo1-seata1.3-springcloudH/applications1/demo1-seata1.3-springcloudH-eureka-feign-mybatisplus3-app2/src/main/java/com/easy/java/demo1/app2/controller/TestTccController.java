package com.easy.java.demo1.app2.controller;

import com.easy.java.demo1.app2.business.tcc.TccService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试TCC模式
 *
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:40
 */
@RestController
@RequestMapping("/test/tcc")
public class TestTccController {

	@Autowired
	private TccService1 tccService1;

	@GetMapping("/dobiz/success")
	public void doBizSuccess(@RequestParam Long payMoney) {
		tccService1.doBiz(null, payMoney, false);
	}

	@GetMapping("/dobiz/error")
	public void doBizError(@RequestParam Long payMoney) {
		tccService1.doBiz(null, payMoney, true);
	}

}