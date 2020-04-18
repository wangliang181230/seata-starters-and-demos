package com.easy.java.starter.seata.util;

import io.seata.core.context.RootContext;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 10:35
 */
public class SeataUtil {

	public static void print(String msg) {
		long threadId = Thread.currentThread().getId();
		boolean inGlobalTransaction = RootContext.inGlobalTransaction();
		String xidType = RootContext.getXIDInterceptorType();
		String xid = xidType != null ? xidType : RootContext.getXID();
		System.out.println(String.format("Seata: [%d] [%s] [%s]: %s",
				threadId,
				inGlobalTransaction ? "true" : "false",
				xid,
				msg));
	}

}