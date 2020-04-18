package com.easy.java.demo1.app2.business.tcc.impl;

import com.easy.java.demo1.app2.business.tcc.TccService1;
import com.easy.java.starter.seata.util.SeataUtil;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangliang <841369634@qq.com>
 * @date 2020/4/18 11:11
 */
@Service
public class TccService1Impl implements TccService1 {

	// 以下这个静态变量作为资源
	private static long money = 100; // 账号资金，默认100元
	private static long lockMoney = 0; // 冻结的资金数量

	@Override
	@Transactional // 一般情况下，此方法上会加本地事务，用于控制当前方法的事务一致性
	public void doBiz(BusinessActionContext businessActionContext,
					  long payMoney) {
		// 理论上，这里要对业务资源数据进行校验，并对资源进行预留操作，达到事务隔离性的目的。
		SeataUtil.print("tcc service1 do biz: ");

		// 校验资源：判断余额是否充足
		SeataUtil.print("tcc service1 do biz: 判断余额是否充足");
		if (money < payMoney) {
			SeataUtil.print("tcc service1 do biz: 余额不足");
			throw new RuntimeException("余额不足");
		}

		// 预留资源：将需支付金额先冻结
		SeataUtil.print(String.format("tcc service1 do biz: 当前余额：%d, 当前冻结金额：%d, 需冻结金额：%d", money, lockMoney, payMoney));
		money -= payMoney;
		lockMoney += payMoney;
		SeataUtil.print(String.format("tcc service1 do biz: 冻结成功，当前余额：%d, 当前冻结金额：%d", money, lockMoney));
	}

	/**
	 * 二阶段提交
	 * 方法名需与上方commitMethod一致，上方commitMethod不配置时，默认为：commit
	 *
	 * @param businessActionContext TCC分支事务上下文（必需的参数）
	 * @return
	 */
	@Override
	public boolean commit1(BusinessActionContext businessActionContext) {
		// 消费预留的资源：整个事务成功，将上面冻结了的需支付金额真正扣除
		long payMoney = Long.valueOf(String.valueOf(businessActionContext.getActionContext("payMoney")));

		if (lockMoney >= payMoney) {
			SeataUtil.print(String.format("tcc: 全局事务成功，将上面冻结了的需支付金额真正扣除: %d", payMoney));
			lockMoney -= payMoney;
			SeataUtil.print(String.format("tcc: 冻结金额扣除成功，当前余额：%d, 当前冻结金额：%d", money, lockMoney));
			return true;
		} else {
			String errorMsg = String.format("tcc: 冻结的金额数量不正确，冻结金额：%d, 需支付金额：%d，请检查你的代码是否正确", lockMoney, payMoney);
			SeataUtil.print(errorMsg);

			// 抛异常或返回false，之后seata会执行重试机制
			// 要注意，C-Commit里抛异常或返回false是不会触发全局事务的回滚，而是进入重试机制。
			//throw new RuntimeException(errorMsg);
			return false;
		}
	}

	/**
	 * 二阶段回滚
	 * 方法名需与上方commitMethod一致，上方rollbackMethod不配置时，默认为：rollback
	 *
	 * @param businessActionContext TCC分支事务上下文（必需的参数）
	 * @return
	 */
	@Override
	public boolean rollback1(BusinessActionContext businessActionContext) {
		// 事务回滚，将之前冻结的需支付金额恢复到余额中。

		long payMoney = Long.valueOf(String.valueOf(businessActionContext.getActionContext("payMoney")));

		if (lockMoney >= payMoney) {
			SeataUtil.print(String.format("tcc: 全局事务失败，将上面冻结了的需支付金额恢复到余额: %d", payMoney));
			lockMoney -= payMoney;
			money += payMoney;
			SeataUtil.print(String.format("tcc: 冻结金额已恢复到余额中，当前余额：%d, 当前冻结金额：%d，请检查是否与diBiz方法中打印的值一致。", money, lockMoney));
			return true;
		} else {
			String errorMsg = String.format("tcc: 冻结的金额数量不正确，冻结金额：%d, 需支付金额：%d，请检查你的代码是否正确", lockMoney, payMoney);
			SeataUtil.print(errorMsg);

			// 抛异常或返回false，之后seata会执行重试机制，重新尝试调用此方法
			//throw new RuntimeException(errorMsg);
			return false;
		}
	}
}
