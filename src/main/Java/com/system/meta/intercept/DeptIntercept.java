package com.system.meta.intercept;

import com.core.aop.AopContext;
import com.core.constant.ConstShiro;
import com.core.jfinal.ext.kit.JStrKit;
import com.core.jfinal.ext.shiro.ShiroKit;
import com.core.meta.PageIntercept;

public class DeptIntercept extends PageIntercept {

	@Override
	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String depts = ShiroKit.getUser().getDeptId() + "," + ShiroKit.getUser().getSubDepts();
			String condition = "and id in (" + JStrKit.removeSuffix(depts, ",") + ")";
			ac.setCondition(condition);
		}
	}

}
