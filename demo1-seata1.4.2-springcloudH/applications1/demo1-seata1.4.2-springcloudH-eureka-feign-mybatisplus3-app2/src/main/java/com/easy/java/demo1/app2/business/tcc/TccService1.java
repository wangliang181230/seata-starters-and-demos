package com.easy.java.demo1.app2.business.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 11:07
 */
@LocalTCC // seata1.3.0版本中，此注解必须加在接口上，否则无法被TCC解析器成一个TCC接口，不知道seata以后会不会做调整。
public interface TccService1 {

	@TwoPhaseBusinessAction(name = "tccService1", commitMethod = "commit1", rollbackMethod = "rollback1")
	void doBiz(BusinessActionContext businessActionContext,
			   @BusinessActionContextParameter(paramName = "payMoney", isShardingParam = true) long payMoney,
			   boolean throwException);

	/**
	 * 二阶段提交
	 * 方法名需与上方commitMethod一致，上方commitMethod不配置时，默认为：commit
	 *
	 * @param businessActionContext TCC分支事务上下文（必需的参数）
	 * @return
	 */
	boolean commit1(BusinessActionContext businessActionContext);

	/**
	 * 二阶段回滚
	 * 方法名需与上方commitMethod一致，上方rollbackMethod不配置时，默认为：rollback
	 *
	 * @param businessActionContext TCC分支事务上下文（必需的参数）
	 * @return
	 */
	boolean rollback1(BusinessActionContext businessActionContext);

}
