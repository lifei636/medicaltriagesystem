package com.calling;

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
import com.shine.service.QueueTypeService;
import com.shine.service.TriageService;
import com.shine.service.impl.DoctorServiceImpl;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.shine.service.impl.TriageServiceImpl;

@ControllerBind(controllerKey = "/callqueueType")
public class CallingQueueTypeController extends BaseController {

	DoctorService doctor = new DoctorServiceImpl();
	QueueTypeService service = new QueueTypeServiceImpl();
	TriageService triage = new TriageServiceImpl();
	ShardKit shard = new ShardKit();

	public void list_doctor_queuetype() {
		Map<String, Object> map = new HashMap<String, Object>();
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "医生工号不能为空");
			renderJson(map);
			return;
		}
		Record rt = triage.findtypelogin_id(login_id);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊台分诊模式失败");
			renderJson(map);
			return;
		}
		String type = rt.get("triage_type").toString();
		if ("1".equals(type)) {
			Doctor record = doctor.findByLogin_id(login_id);
			if (null == record) {
				map.put("return_code", "fail");
				map.put("return_msg", "医生工号不正确");
				renderJson(map);
				return;
			}
			List<Record> list = service.list_doctor_queuetype(login_id);
			if (list.size() < 1) {
				map.put("return_code", "fail");
				map.put("return_msg", "医生没有队列");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "success");
				map.put("return_msg", "查询成功");
				map.put("count", list.size());
				map.put("list", list);
				renderJson(map);
			}
		} else {
			Doctor record = doctor.findByLogin_id(login_id);
			if (null == record) {
				map.put("return_code", "fail");
				map.put("return_msg", "医生工号不正确");
				renderJson(map);
				return;
			}
			List<Record> list = service.list_pager_queuetype(login_id, shard.getIpAddr(getRequest()));
			if (list.size() < 1) {
				map.put("return_code", "fail");
				map.put("return_msg", "医生没有队列");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "success");
				map.put("return_msg", "查询成功");
				map.put("count", list.size());
				map.put("list", list);
				renderJson(map);
			}
		}

	}
}
