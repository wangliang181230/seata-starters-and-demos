package com.easy.java.starter.seata.environmentpostprocessor;

import com.easy.java.common.environmentpostprocessor.AbstractEnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 3:39
 */
public class LoadSeataConfigFileEnvironmentPostProcessor extends AbstractEnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		super.loadConfigFile(environment, "SeataPropertySource", "config/application-seata.yml");
	}

}