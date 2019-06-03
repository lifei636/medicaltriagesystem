package com.system.meta.intercept;

import com.core.base.BaseValidator;
import com.system.service.MenuService;
import com.system.service.impl.MenuServiceImpl;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class MenuValidator extends BaseValidator {

	MenuService service = new MenuServiceImpl();

	@Override
	protected void validate(Controller c) {
		if (getActionKey().toString().indexOf("update") == -1) {
			validateRequired("tfw_menu.pcode", "请输入菜单父编号");
			validateCode("tfw_menu.code", "菜单编号已存在!");
		}
		// validateSql("tfw_menu.source", "含有非法字符,请仔细检查!");
	}

	@Override
	protected void handleError(Controller c) {
		c.renderJson(result);
	}

	protected void validateCode(String field, String errorMessage) {
		String code = getController().getPara(field);
		if (StrKit.isBlank(code)) {
			addError("请输入菜单编号!");
		}
		if (service.isExistCode(code)) {
			addError(errorMessage);
		}
	}

}
