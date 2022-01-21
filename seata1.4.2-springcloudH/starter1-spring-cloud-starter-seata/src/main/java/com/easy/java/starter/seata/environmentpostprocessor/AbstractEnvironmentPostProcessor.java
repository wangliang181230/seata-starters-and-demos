package com.easy.java.starter.seata.environmentpostprocessor;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 3:52
 */
public abstract class AbstractEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	public static final String DEFAULT_PROPERTIES = "defaultProperties";

	/**
	 * 获取默认配置的配置源
	 *
	 * @param environment 环境
	 * @param autoCreate  不存在时是否自动添加
	 * @return
	 */
	public static MapPropertySource getDefaultPropertySource(ConfigurableEnvironment environment, boolean autoCreate) {
		MutablePropertySources propertySources = environment.getPropertySources();

		PropertySource propertySource = propertySources.get(DEFAULT_PROPERTIES);
		if (propertySource instanceof MapPropertySource) {
			return (MapPropertySource) propertySource;
		} else if (!autoCreate) {
			return null;
		} else {
			MapPropertySource newPropertySource;
			if (propertySource != null) {
				newPropertySource = new MapPropertySource(DEFAULT_PROPERTIES + "2", new HashMap<>());
				propertySources.addBefore(DEFAULT_PROPERTIES, newPropertySource);
			} else {
				newPropertySource = new MapPropertySource(DEFAULT_PROPERTIES, new HashMap<>());
				propertySources.addLast(newPropertySource);
			}
			return newPropertySource;
		}
	}

	/**
	 * 添加默认配置项
	 *
	 * @param environment 环境
	 * @param properties  配置信息
	 */
	public static void addDefaultProperties(ConfigurableEnvironment environment, Map<String, Object> properties) {
		if (properties == null || properties.isEmpty()) {
			return;
		}

		MapPropertySource propertySource = getDefaultPropertySource(environment, true);
		assert propertySource != null;
		Map<String, Object> source = propertySource.getSource();
		source.putAll(properties);
	}

	/**
	 * 解析配置文件
	 *
	 * @param propertySourceName 配置源名称
	 * @param profilePath        配置文件路径
	 * @return propertySource 配置源
	 */
	protected PropertySource<Map<String, Object>> parseConfigFile(String propertySourceName, String profilePath) {
		try {
			// 创建资源对象
			ClassPathResource classPathResource = new ClassPathResource(profilePath);

			Properties properties;
			if (profilePath.toLowerCase().endsWith(".yml") || profilePath.toLowerCase().endsWith(".yaml")) {
				// 创建配置文件工厂对象
				YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
				yaml.setResources(classPathResource);
				// 创建配置源对象
				properties = yaml.getObject();
			} else if (profilePath.toLowerCase().endsWith(".properties")) {
				// 创建配置文件工厂对象
				PropertiesFactoryBean pro = new PropertiesFactoryBean();
				pro.setLocation(classPathResource);
				// 创建配置源对象
				properties = pro.getObject();
			} else {
				throw new RuntimeException("暂时不支持该类型配置文件的解析：" + profilePath);
			}

			// 创建配置源，并返回
			return new OriginTrackedMapPropertySource(propertySourceName, Collections.unmodifiableMap(properties), true);
		} catch (IOException e) {
			throw new RuntimeException("解析配置文件失败", e);
		}
	}

	/**
	 * 加载配置文件并生成配置源，添加到末尾，该配置源内的配置优先最低
	 *
	 * @param environment        环境
	 * @param propertySourceName 配置源名称
	 * @param profilePath        配置文件地址
	 */
	protected void loadConfigFile(ConfigurableEnvironment environment, String propertySourceName, String profilePath) {
		// 解析配置文件，获取配置源
		PropertySource<Map<String, Object>> propertySource = this.parseConfigFile(propertySourceName, profilePath);

		// 将配置源添加到末尾，优先最低
		environment.getPropertySources().addLast(propertySource);
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}