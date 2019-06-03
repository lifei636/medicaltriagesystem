package com.triage;

import java.util.HashMap;
import java.util.Map;

import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.Record;
import com.core.toolbox.kit.ShardKit;
import com.shine.service.PagerService;
import com.shine.service.impl.PagerServiceImpl;

@ControllerBind(controllerKey = "/clientpager")
public class ClientPagerController extends BaseController {

	PagerService pager = new PagerServiceImpl();
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
		Record record = pager.findip(ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "成功");
			map.put("zsmc", record.get("display_name"));
			renderJson(map);
		}
		renderJson(map);
	}
}
