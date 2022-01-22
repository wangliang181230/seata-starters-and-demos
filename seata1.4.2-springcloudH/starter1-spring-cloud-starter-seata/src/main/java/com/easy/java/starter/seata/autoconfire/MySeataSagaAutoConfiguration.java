package com.easy.java.starter.seata.autoconfire;

import com.easy.java.starter.seata.util.ResourceUtil;
import com.easy.java.starter.seata.properties.MySeataSagaThreadPoolProperties;
import io.seata.saga.engine.StateMachineConfig;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import io.seata.spring.boot.autoconfigure.StarterConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import javax.sql.DataSource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Saga auto configuration.
 *
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/15
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(StarterConstants.SEATA_PREFIX + ".saga.enabled")
public class MySeataSagaAutoConfiguration {

    public static final String SAGA_ASYNC_THREAD_POOL_EXECUTOR_BEAN_NAME = "seataSagaAsyncThreadPoolExecutor";
    public static final String SAGA_REJECTED_EXECUTION_HANDLER_BEAN_NAME = "seataSagaRejectedExecutionHandler";

    /**
     * Create state machine config bean.
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    @ConfigurationProperties("seata.saga.state-machine")
    public StateMachineConfig dbStateMachineConfig(
            DataSource dataSource,
            @Autowired(required = false) ThreadPoolExecutor threadPoolExecutor) {
        DbStateMachineConfig config = new DbStateMachineConfig();
        config.setDataSource(dataSource);

        if (threadPoolExecutor != null) {
            config.setThreadPoolExecutor(threadPoolExecutor);
        }

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

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "seata.saga.state-machine.enable-async", havingValue = "true")
    @EnableConfigurationProperties({MySeataSagaThreadPoolProperties.class})
    static class SagaAsyncThreadPoolExecutorConfiguration {

        /**
         * Create rejected execution handler bean.
         */
        @Bean(SAGA_REJECTED_EXECUTION_HANDLER_BEAN_NAME)
        @ConditionalOnMissingBean
        public RejectedExecutionHandler sagaRejectedExecutionHandler() {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        }

        /**
         * Create state machine async thread pool executor bean.
         */
        @Bean(SAGA_ASYNC_THREAD_POOL_EXECUTOR_BEAN_NAME)
        @ConditionalOnMissingBean
        public ThreadPoolExecutor sagaAsyncThreadPoolExecutor(
                MySeataSagaThreadPoolProperties properties,
                @Qualifier(SAGA_REJECTED_EXECUTION_HANDLER_BEAN_NAME) RejectedExecutionHandler rejectedExecutionHandler) {
            ThreadPoolExecutorFactoryBean threadFactory = new ThreadPoolExecutorFactoryBean();
            threadFactory.setBeanName("sagaStateMachineThreadPoolExecutorFactory");
            threadFactory.setThreadNamePrefix("sagaAsyncExecute-");
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
                    threadFactory,
                    rejectedExecutionHandler
            );

            return threadPoolExecutor;
        }
    }
}
