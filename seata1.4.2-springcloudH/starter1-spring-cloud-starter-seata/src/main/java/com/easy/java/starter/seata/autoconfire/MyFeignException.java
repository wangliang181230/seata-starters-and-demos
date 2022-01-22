package com.easy.java.starter.seata.autoconfire;

/**
 * @author wangliang181230
 */
public class MyFeignException extends RuntimeException {

	private final String code;

	public MyFeignException(String code, String message) {
		super(message);
		this.code = code;
	}

	public MyFeignException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
