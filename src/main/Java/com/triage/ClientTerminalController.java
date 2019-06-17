package com.triage;

import java.util.HashMap;
import java.util.Map;

import com.staticutil.ConstantModel;
import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.kit.ShardKit;
import com.shine.model.Terminal;
import com.shine.service.TerminalService;
import com.shine.service.impl.TerminalServiceImpl;

@ControllerBind(controllerKey = "/clientterminal")
public class ClientTerminalController extends BaseController {

	TerminalService terminal = new TerminalServiceImpl();
	ShardKit shard = new ShardKit();

	public void pager_name() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shard.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			renderJson(map);
			return;
		}
		Terminal tm = terminal.findbyip(ip);
		if (null == tm) {
			map.put("return_code", "fail");
			map.put("return_msg", "失败");
			renderJson(map);
			return;
		} else {
			map.put("showNumber", ConstantModel.DOOR_SHOWNUMBER);
			map.put("return_code", "success");
			map.put("return_msg", "成功");
			map.put("zsmc", tm.getName());
			map.put("zsmc_display", tm.getDisplay_name());
			renderJson(map);
		}
		renderJson(map);
	}
}
