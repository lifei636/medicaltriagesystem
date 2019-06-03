package com.system.meta.intercept;

import com.core.aop.AopContext;
import com.core.constant.ConstShiro;
import com.core.jfinal.ext.kit.JStrKit;
import com.core.jfinal.ext.shiro.ShiroKit;
import com.core.meta.PageIntercept;

public class RoleIntercept extends PageIntercept {

	@Override
	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String roles = ShiroKit.getUser().getRoles() + "," + ShiroKit.getUser().getSubRoles();
			String condition = "and id in (" + JStrKit.removeSuffix(roles, ",") + ")";
			ac.setCondition(condition);
		}
	}

}
