package com.shine.service;

import java.util.List;

import com.core.base.service.IService;
import com.core.toolbox.Record;
import com.shine.model.Triage;

/**
 * Generated by ranzhilin. 2017-08-03 10:50:24
 */
public interface TriageService extends IService<Triage> {

	public List<Record> queryAll();

	public Triage queryTriageName(String triageName);

	public Triage queryTriageIp(String triageIp);

	public Triage queryLogin(String ip, String password);

	public List<Record> queryByQueueType(String ip);

	public Record findtypelogin_id(String login_id);

	public Record findbypagerip(String triageIp);
	public Record findbytriageip(String triageIp);
	
	public Record  findtriageBytemIP(String temIp) ;
}