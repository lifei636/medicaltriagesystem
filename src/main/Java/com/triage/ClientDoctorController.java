package com.triage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.Record;
import com.core.toolbox.kit.ShardKit;
import com.shine.model.Doctor;
import com.shine.service.DoctorService;
import com.shine.service.Pager2terminalService;
import com.shine.service.impl.DoctorServiceImpl;
import com.shine.service.impl.Pager2terminalServiceImpl;

@ControllerBind(controllerKey = "/clientdoctor")
public class ClientDoctorController extends BaseController {

	ShardKit shard = new ShardKit();
	DoctorService service = new DoctorServiceImpl();
	Pager2terminalService ptservice = new Pager2terminalServiceImpl();

	public void list_doctor() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> list = service.list_doctor();
		if (list.isEmpty()) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询医生成功");
			map.put("count", list.size());
			map.put("list", list);
			renderJson(map);
		}
	}

	public void termianleByPager() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shard.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取屏ip失败");
			renderJson(map);
			return;
		}
		List<Record> list = service.queryByipAll(ip);
		String ips = "";
		for (Record rd : list) {
			ips += rd.getStr("ip") + ",";
		}
		map.put("return_code", "success");
		map.put("return_msg", "获取关联ip成功");
		map.put("ips", ips);
		renderJson(map);
		return;
	}
	public void getDoctorInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		String login_id=getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请输入医生login_id");
			renderJson(map);
			return;
		}
		else
		{
			Doctor r=service.findByLogin_id(login_id);
			map.put("return_code", "success");
			map.put("return_msg", "信息查询成功");
			map.put("doctor", r);
			renderJson(map);
		}
	}
	public void doctor_room_door() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shard.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取屏ip失败");
			renderJson(map);
			return;
		}
		Record r = ptservice.findByIP(ip);
		if (null == r) {
			map.put("return_code", "fail");
			map.put("return_msg", "该屏还未与叫号器绑定关系");
			renderJson(map);
			return;
		}
		Record record = service.queryByip(ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "还未有医生登陆诊室");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "信息查询成功");
			map.put("doctorinfo", record);
			renderJson(map);
		}
	}
}
