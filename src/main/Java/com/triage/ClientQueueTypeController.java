package com.triage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.Record;
import com.core.toolbox.kit.ShardKit;
import com.shine.model.Triage;
import com.shine.service.QueueTypeService;
import com.shine.service.TriageService;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.shine.service.impl.TriageServiceImpl;

@ControllerBind(controllerKey = "/clientqueuetype")
public class ClientQueueTypeController extends BaseController {

	QueueTypeService service = new QueueTypeServiceImpl();
	ShardKit shardkit = new ShardKit();
	TriageService triageservice = new TriageServiceImpl();
	//获取分诊台列表
	public void listQueueType() {
		String ip = getPara("ip");
        
        try {
	        if(ip.isEmpty()||ip.equals("")) 
	        	ip=shardkit.getIpAddr(getRequest());
			}
			catch (Exception e) {
				ip=shardkit.getIpAddr(getRequest());
			}
		//String ip = shardkit.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			renderJson(error(IP_ERROR_MSG));
			return;
		}
		Triage triage = triageservice.queryTriageIp(ip);
		List<Record> list =null;
		//判断是按队列还是按叫号器
		if(triage.getReorder_type()==1) {
			list = service.listQueueType(ip);
		} else {
			list = service.listPager(ip);
		}
		if (null == list) {
			renderJson(error(QUEUE_TYPE_NULL_MSG));
			return;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", 0);
			map.put("msg", QUERY_SUCCESS_MSG);
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}
	
	public void listQueueType2() {
		String ip = getPara("ip");
        
        try {
	        if(ip.isEmpty()||ip.equals("")) 
	        	ip=shardkit.getIpAddr(getRequest());
			}
			catch (Exception e) {
				ip=shardkit.getIpAddr(getRequest());
			}
		//String ip = shardkit.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			renderJson(error(IP_ERROR_MSG));
			return;
		}
		Triage triage = triageservice.queryTriageIp(ip);
		List<Record> list =null;
		//判断是按队列还是按叫号器
		if(triage.getReorder_type()==1)
			list= service.listQueueType2(ip,triage.getTriage_type().toString());
		else
			list=service.listPager2(ip,triage.getTriage_type().toString());
		if (null == list) {
			renderJson(error(QUEUE_TYPE_NULL_MSG));
			return;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", 0);
			map.put("msg", QUERY_SUCCESS_MSG);
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}

	public void listQueueTypeName() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shardkit.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}

		List<Record> list = service.listQueueTypeName(ip);
		if (list.isEmpty()) {
			renderJson(error(QUERY_FAIL_MSG));
			return;
		} else {
			map.put("return_msg", "成功");
			map.put("return_code", "success");
			map.put("list", list);
			renderJson(map);
		}
	}

	public void GetQueueTypeOne() {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = getPara("id");
		if (StringKit.isBlank(id)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}

		List<Record> list = service.findByQueueId(id);
		if (list.isEmpty()) {
			map.put("return_msg", "失败");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		} else {
			map.put("return_msg", "成功");
			map.put("return_code", "success");
			map.put("count", list.size());
			map.put("list", list);
			renderJson(map);
		}
	}

}
