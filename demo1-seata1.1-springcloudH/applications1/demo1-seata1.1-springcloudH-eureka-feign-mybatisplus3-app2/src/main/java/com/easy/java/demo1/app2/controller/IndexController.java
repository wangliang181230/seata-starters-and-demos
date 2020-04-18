package com.easy.java.demo1.app2.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:55
 */
@Lazy
@Controller
@Api(hidden = true)
public class IndexController {

	@GetMapping("/")
	@ApiOperation(value = "首页", hidden = true)
	public void index(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui.html");
	}

}