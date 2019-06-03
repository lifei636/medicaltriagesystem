package com.shine.service.impl;

import com.core.dao.Blade;
import com.core.dao.Db;
import com.core.toolbox.Record;
import com.shine.service.ThirdDataService;

public class ThirdDataServiceImpl implements ThirdDataService {

	
	@Override
	public String docInsertOrUpdate(String type, String doctor_id,String login_id, String name, String title, String department,String description) {
		String r_string = "";
		String sql = "";
		Integer r = 0;
		try {
			if (type.equals("insert")) {
				sql = Blade.dao().getScript("third.docInsert").getSql();
				r = Db.init().insert(sql, Record.create().set("doctor_id", doctor_id).set("login_id", login_id).set("name", name)
						.set("title", title).set("department", department).set("description",description));
				if (r > 0)
					r_string = "添加成功";
				else
					r_string = "添加失败";
			} else {
				sql = Blade.dao().getScript("third.docUpdate").getSql();
				r = Db.init().update(sql, Record.create().set("doctor_id", doctor_id).set("login_id", login_id).set("name", name)
						.set("title", title).set("department", department).set("description",description));
				if (r > 0)
					r_string = "更新成功";
				else
					r_string = "更新失败,本次更新的数据也存在";
			}

		} catch (Exception e) {
			r_string = e.getMessage();
		}
		return r_string;
	}
	@Override
	public String dpmtInsertOrUpdate(String type,String souce_id,String name,String displayname)
	{
		String r_string = "";
		String sql = "";
		Integer r = 0;
		try {
			if (type.equals("insert")) {
				sql = Blade.dao().getScript("third.dpmtInsert").getSql();
				r = Db.init().insert(sql, Record.create().set("souce_id", souce_id).set("name", name)
						.set("displayname", displayname));
				if (r > 0)
					r_string = "添加成功";
				else
					r_string = "添加失败";
			} else {
				sql = Blade.dao().getScript("third.dpmtUpdate").getSql();
				r = Db.init().update(sql, Record.create().set("souce_id", souce_id).set("name", name)
						.set("displayname", displayname));
				if (r > 0)
					r_string = "更新成功";
				else
					r_string = "更新失败";
			}

		} catch (Exception e) {
			r_string = e.getMessage();
		}
		return r_string;
	}
	@Override
	public String docPBInsertOrUpdate(String type,String pb_id,String doctor_id,String name,String title,String title2,String department,String department2,String begin_time,String end_time,Integer totalNmb,Integer remainNmb,Integer is_stop)
	{
		String r_string = "";
		String sql = "";
		Integer r = 0;
		try {
			if (type.equals("insert")) {
				sql = Blade.dao().getScript("third.docPBInsert").getSql();
				r = Db.init().insert(sql, Record.create().set("pb_id", pb_id).set("doctor_id", doctor_id).set("name", name)
						.set("title", title).set("title2", title2).set("department", department).set("department2", department2).set("begin_time", begin_time).set("end_time", end_time).set("totalNmb", totalNmb).set("remainNmb", remainNmb).set("is_stop",is_stop));
				if (r > 0)
					r_string = "添加成功";
				else
					r_string = "添加失败";
			} else {
				sql = Blade.dao().getScript("third.docPBUpdate").getSql();
				r = Db.init().update(sql, Record.create().set("pb_id", pb_id).set("doctor_id", doctor_id).set("name", name)
						.set("title", title).set("title2", title2).set("department", department).set("department2", department2).set("begin_time", begin_time).set("end_time", end_time).set("totalNmb", totalNmb).set("remainNmb", remainNmb).set("is_stop",is_stop));
				if (r > 0)
					r_string = "更新成功";
				else
					r_string = "更新失败";
			}

		} catch (Exception e) {
			r_string = e.getMessage();
		}
		return r_string;
	}
	@Override
	public String  patientInserOrUpdate(String type,String patient_id,String patient_name,String queue_type_source_id,String source_code,String register_id,Integer queue_num,Integer time_interval,String doctor_source_id,Integer is_deleted,String begin_time,String end_time,String fre_date) {
		String r_string = "";
		String sql = "";
		Integer r = 0;
		try {
			if (type.equals("insert")) {
				sql = Blade.dao().getScript("third.patientInsert").getSql();
				//patient_id, patient_name, queue_type_source_id, source_code, register_id, queue_num, time_interval, doctor_source_id, is_deleted,begin_time,end_time, fre_date
				r = Db.init().insert(sql, Record.create().set("patient_id", patient_id).set("patient_name", patient_name).set("queue_type_source_id", queue_type_source_id)
						.set("source_code", source_code).set("register_id", register_id).set("queue_num", queue_num).set("time_interval", time_interval).set("doctor_source_id", doctor_source_id).set("begin_time", begin_time).set("end_time", end_time).set("fre_date", fre_date).set("is_deleted", is_deleted));
				if (r > 0)
					r_string = "添加成功";
				else
					r_string = "添加失败";
			} else {
				sql = Blade.dao().getScript("third.patientUpdate").getSql();
				r = Db.init().update(sql, Record.create().set("patient_id", patient_id).set("patient_name", patient_name).set("queue_type_source_id", queue_type_source_id)
						.set("source_code", source_code).set("register_id", register_id).set("queue_num", queue_num).set("time_interval", time_interval).set("doctor_source_id", doctor_source_id).set("begin_time", begin_time).set("end_time", end_time).set("fre_date", fre_date).set("is_deleted", is_deleted));
				if (r > 0)
					r_string = "更新成功";
				else
					r_string = "更新失败";
			}

		} catch (Exception e) {
			r_string = e.getMessage();
		}
		return r_string;
	}

}
