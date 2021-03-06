package com.shine.service;

import java.util.List;

import com.core.base.service.IService;
import com.core.toolbox.Record;
import com.shine.model.Doctor;

/**
 * Generated by Blade. 2017-08-03 10:50:24
 */
public interface DoctorService extends IService<Doctor> {

	public List<Record> queryQueueTypeName();

	public Doctor findByLogin_id(String login_id);

	public List<Record> list_doctor();

	public List<Record> call_doctor_queueType_name(String login_id, String ip, String type);

	public Record queryByip(String ip);

	public List<Record> queryByipAll(String ip);
	
	
	//
}
