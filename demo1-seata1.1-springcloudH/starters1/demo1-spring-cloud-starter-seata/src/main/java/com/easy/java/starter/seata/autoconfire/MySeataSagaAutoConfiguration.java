package com.easy.java.starter.seata.autoconfire;

import com.easy.java.common.util.ResourceUtil;
import com.easy.java.starter.seata.properties.MySeataSagaThreadPoolProperties;
import io.seata.saga.engine.StateMachineConfig;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import io.seata.spring.boot.autoconfigure.StarterConstants;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import javax.sql.DataSource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Saga auto configuration.
 *
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/15
 */
@Configuration
@ConditionalOnProperty(StarterConstants.SEATA_PREFIX + ".saga.enabled")
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({MySeataSagaThreadPoolProperties.class})
public class MySeataSagaAutoConfiguration {

	/**
	 * Create state machine thread pool bean.
	 */
	@Bean
	public ThreadPoolExecutor sagaStateMachineThreadPoolExecutor(MySeataSagaThreadPoolProperties properties) {
		ThreadPoolExecutorFactoryBean threadFactory = new ThreadPoolExecutorFactoryBean();
		threadFactory.setCorePoolSize(properties.getCorePoolSize());
		threadFactory.setMaxPoolSize(properties.getMaxPoolSize());
		threadFactory.setKeepAliveSeconds(properties.getKeepAliveTime());

		BlockingQueue<Runnable> queue = new LinkedBlockingQueue();

		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				properties.getCorePoolSize(),
				properties.getMaxPoolSize(),
				properties.getKeepAliveTime(),
				TimeUnit.SECONDS,
				queue,
				threadFactory
		);

		return threadPoolExecutor;
	}

	/**
	 * Create state machine config bean.
	 */
	@Bean
	@ConditionalOnBean(DataSource.class)
	@ConditionalOnMissingBean
	@ConfigurationProperties(StarterConstants.SEATA_PREFIX + ".saga.state-machine")
	public DbStateMachineConfig dbStateMachineConfig(DataSource dataSource, ThreadPoolExecutor threadPoolExecutor) {
		DbStateMachineConfig config = new DbStateMachineConfig();
		config.setDataSource(dataSource);
		config.setThreadPoolExecutor(threadPoolExecutor);

		Resource[] resources = ResourceUtil.getResources("classpath*:statelang/*.json");
		if (resources != null && resources.length > 0) {
			config.setResources(resources);
		}

		return config;
	}

	/**
	 * Create state machine engine bean.
	 */
	@Bean
	@ConditionalOnMissingBean
	public StateMachineEngine stateMachineEngine(StateMachineConfig config) {
		ProcessCtrlStateMachineEngine engine = new ProcessCtrlStateMachineEngine();
		engine.setStateMachineConfig(config);
		new StateMachineEngineHolder().setStateMachineEngine(engine);
		return engine;
	}
}
