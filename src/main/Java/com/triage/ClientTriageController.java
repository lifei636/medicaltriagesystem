package com.triage;

import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.kit.ShardKit;
import com.shine.model.Triage;
import com.shine.service.TriageService;
import com.shine.service.impl.TriageServiceImpl;

@ControllerBind(controllerKey = "/triagecl")
public class ClientTriageController extends BaseController {

	TriageService triageservice = new TriageServiceImpl();
	ShardKit shardkit = new ShardKit();
	//根据IP获取分诊台信息
	public void findByTriageName() {
		String ip = getPara("ip");
        
        try {
	        if(ip.isEmpty()||ip.equals("")) 
	        	ip=shardkit.getIpAddr(getRequest());
			}
			catch (Exception e) {
				ip=shardkit.getIpAddr(getRequest());
			}
		if (StringKit.isBlank(ip)) {
			renderJson(error(IP_NULL_MSG));
			return;
		}
		Triage triage = triageservice.queryTriageIp(ip);

		if (null == triage) {
			renderJson(error(QUERY_FAIL_MSG));
			return;
		} else {
			//Object obj = triage.getName();
			renderJson(json(triage, QUERY_SUCCESS_MSG));
		}
		return;
	}
}
