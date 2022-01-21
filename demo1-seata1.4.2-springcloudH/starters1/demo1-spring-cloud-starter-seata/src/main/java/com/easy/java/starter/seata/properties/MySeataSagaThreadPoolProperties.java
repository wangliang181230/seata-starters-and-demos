package com.easy.java.starter.seata.properties;

import io.seata.spring.boot.autoconfigure.StarterConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(StarterConstants.SEATA_PREFIX + ".saga.thread-pool")
public class MySeataSagaThreadPoolProperties {

	/**
	 * 默认线程池大小
	 */
	private int corePoolSize = 1;

	/**
	 * 最大线程池大小
	 */
	private int maxPoolSize = 20;

	/**
	 * 线程保持活跃的时间，单位：秒
	 */
	private int keepAliveTime = 60;


	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
}