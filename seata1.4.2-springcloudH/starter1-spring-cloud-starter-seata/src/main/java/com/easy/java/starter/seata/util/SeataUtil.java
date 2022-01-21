package com.easy.java.starter.seata.util;

import io.seata.core.context.RootContext;
import io.seata.core.model.BranchType;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 10:35
 */
public class SeataUtil {

    public static void print(String msg) {
        long threadId = Thread.currentThread().getId();
        boolean inGlobalTransaction = RootContext.inGlobalTransaction();
        String xid = RootContext.getXID();
        BranchType branchType = RootContext.getBranchType();
        System.out.printf("Seata: [%d] [%s, %s, %s]: %s%n",
                threadId,
                inGlobalTransaction,
                xid,
                branchType,
                msg);
    }

}