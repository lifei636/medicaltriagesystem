package com.calling;

import java.util.HashMap;
import java.util.Map;

import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.kit.ShardKit;
import com.shine.service.PagerService;
import com.shine.service.impl.PagerServiceImpl;

@ControllerBind(controllerKey = "/call_logout")
public class CallingLoginOutController extends BaseController {
	PagerService service = new PagerServiceImpl();
	ShardKit shared = new ShardKit();

	public void Logout() {
		Map<String, Object> map = new HashMap<String, Object>();
		String pager_ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请填写叫号器IP");
			renderJson(map);
			return;
		}
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请填写登陆工号");
			renderJson(map);
			return;
		}
		int num = service.logout(pager_ip, login_id);
		getSession().removeAttribute("ip");
		getSession().removeAttribute("login_id");
		if (num < 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "医生退出登陆失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "医生退出登陆成功");
			renderJson(map);
		}
		renderJson(map);
	}
}
