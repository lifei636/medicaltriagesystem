package com.system.controller.base;

import com.core.base.BaseController;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;

import static com.core.constant.ConstShiro.ADMIN;
import static com.core.constant.ConstShiro.ADMINISTRATOR;

/**
 * 权限仅开放 管理员角色
 */
@RequiresRoles(value = { ADMINISTRATOR, ADMIN }, logical = Logical.OR)
public class AdminBaseController extends BaseController {

}
