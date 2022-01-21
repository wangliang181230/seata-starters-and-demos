package com.easy.java.demo1.app2.business.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 2:54
 */
@Configuration(proxyBeanMethods = false)
@MapperScan(basePackages = "com.easy.java.demo1.app2.business.mapper")
public class MyBatisPlusConfiguration {
}