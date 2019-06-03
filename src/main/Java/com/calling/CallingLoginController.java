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
import com.shine.model.Pager;
import com.shine.service.Doctor2queuetypeService;
import com.shine.service.DoctorService;
import com.shine.service.Pager2queuetypeService;
import com.shine.service.PagerService;
import com.shine.service.QueueTypeService;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.shine.service.TriageService;
import com.shine.service.impl.Doctor2queuetypeServiceImpl;
import com.shine.service.impl.DoctorServiceImpl;
import com.shine.service.impl.Pager2queuetypeServiceImpl;
import com.shine.service.impl.PagerServiceImpl;
import com.shine.service.impl.TriageServiceImpl;

@ControllerBind(controllerKey = "/call_login")
public class CallingLoginController extends BaseController {

	DoctorService servicedoctor = new DoctorServiceImpl();
	ShardKit shared = new ShardKit();
	PagerService servicepager = new PagerServiceImpl();
	Doctor2queuetypeService rdq = new Doctor2queuetypeServiceImpl();
	Pager2queuetypeService rpq = new Pager2queuetypeServiceImpl();
	TriageService triage = new TriageServiceImpl();
	QueueTypeService queue =new QueueTypeServiceImpl();
	public void call_login() {
		Map<String, Object> map = new HashMap<String, Object>();
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请输入您的工号进行登陆");
			renderJson(map);
			return;
		}
		Doctor recordloginid = servicedoctor.findByLogin_id(login_id);
		if (null == recordloginid) {
			map.put("return_code", "fail");
			map.put("return_msg", "当前医生工号错误,请输入正确的医生工号");
			renderJson(map);
			return;
		}
		String ip = shared.getIpAddr(getRequest());
		Pager recordip = servicepager.findByIP(ip);
		if (null == recordip) {
			map.put("return_code", "fail");
			map.put("return_msg", "当前电脑没有配置为叫号器");
			renderJson(map);
			return;
		}

		Record rt = triage.findbypagerip(ip);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "当前电脑没有配置叫号器设置");
			renderJson(map);
			return;
		}
		String type = rt.getStr("triage_type");

		if ("1".equals(type)) {
			Record qdp = rdq.querydoctorpaiban(login_id);
			if (null == qdp) {
				map.put("return_code", "fail");
				map.put("return_msg", "当前医生没有排班信息，请联系分诊台护士");
				renderJson(map);
				return;
			} else {
				servicepager.updatepagerinsert(login_id, ip);
				servicepager.updatedoctorid(ip, login_id);
				Record record = servicepager.call_login(login_id);
				if (null == record) {
					map.put("return_code", "fail");
					map.put("return_msg", "登陆失败,请确认登陆工号是否正确");
					renderJson(map);
					return;
				} else {
					List<Record> list = servicedoctor.call_doctor_queueType_name(login_id, ip, type);
					if (null == list) {
						map.put("return_code", "fail");
						map.put("return_msg", "该医生没有分配队列");
						renderJson(map);
						return;
					} else {
						map.put("return_code", "success");
						map.put("return_msg", "操作成功");
						map.put("list", list);
						map.put("queue_type_id", list.get(0).get("queue_type_id"));
						//queue.ResetCallStatus( list.get(0).get("queue_type_id").toString());
						setSessionAttr("login_id", login_id);
						setSessionAttr("ip", ip);
						renderJson(map);
					}
				}
			}
		} else if ("2".equals(type)) {
			int bool = servicepager.updatepagerinsert(login_id, ip);
			if (bool > 0) {
				Record qpp = rpq.querypagerpaiban(login_id);
				if (null == qpp) {
					Pager pag = servicepager.findFirstBy("ip=#{ip}", Record.create().set("ip", ip));
					if (null != pag.getDoctor_id()) {
						boolean boole = servicepager.updatepagerdelete(ip);
						if (boole) {
							map.put("return_code", "fail");
							map.put("return_msg", "当前叫号器没有排班信息，请联系分诊台护士");
							renderJson(map);
							return;
						}
					}
				} else {
					Record record = servicepager.call_login(login_id);
					servicepager.updatedoctorid(ip, login_id);
					if (null == record) {
						map.put("return_code", "fail");
						map.put("return_msg", "登陆失败,请确认登陆工号是否正确");
						renderJson(map);
						return;
					} else {
						List<Record> list = servicedoctor.call_doctor_queueType_name(login_id, ip, type);
						if (null == list) {
							map.put("return_code", "fail");
							map.put("return_msg", "该医生没有分配队列");
							renderJson(map);
							return;
						} else {
							map.put("return_code", "success");
							map.put("return_msg", "操作成功");
							map.put("list", list);
							setSessionAttr("queue_type_id", list.get(0).get("queue_type_id"));
							setSessionAttr("login_id", login_id);
							renderJson(map);
						}
					}
				}
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "系统错误，请联系管理员");
			renderJson(map);
			return;
		}
		renderJson(map);
	}
}
