package com.shine.service.impl;

import java.util.List;

import com.core.base.service.impl.BaseService;
import com.core.dao.Blade;
import com.core.dao.Db;
import com.core.toolbox.Record;
import com.shine.model.Doctor2queuetype;
import com.shine.service.Doctor2queuetypeService;

/**
 * Generated by Blade. 2017-08-03 10:50:24
 */
public class Doctor2queuetypeServiceImpl extends BaseService<Doctor2queuetype> implements Doctor2queuetypeService {

	Blade blade = Blade.create(Doctor2queuetype.class);

	@Override
	public List<Record> findDoctorPaiban(String ip) {
		String sql = Blade.dao().getScript("Doctor2queuetype.findDoctorPaiban").getSql();
		return Db.init().selectList(sql, Record.create().set("ip", ip));
	}

	@Override
	public boolean update_rdq(String ids, String onduty) {
		return blade.updateBy("onduty=#{onduty}", "id in (" + ids + ")", Record.create().set("onduty", onduty));
	}

	@Override
	public int delete_pb(String ids) {
		return blade.deleteBy("id in (" + ids + ")", null);
	}

	@Override
	public Record finddoctorandqueuetypeid(String doctor_id, String queue_type_id) {
		return blade.findFirstBy("doctor_id=#{doctor_id} and queue_type_id=#{queue_type_id}",
				Record.create().set("doctor_id", doctor_id).set("queue_type_id", queue_type_id));
	}

	@Override
	public Record querydoctorpaiban(String login_id) {
		String sql = Blade.dao().getScript("Doctor2queuetype.querydoctorpaiban").getSql();
		return Db.init().selectOne(sql, Record.create().set("login_id", login_id));
	}

	@Override
	public List<Record> findInt(int doctor_id, int queue_type_id) {
		return blade.findBy("doctor_id = #{doctor_id} AND queue_type_id = #{queue_type_id}",
				Record.create().set("doctor_id", doctor_id).set("queue_type_id", queue_type_id));
	}

	@Override
	public List<Record> findeDoctorPaibanByWeek(String week) {
		String sql = Blade.dao().getScript("Doctor2queuetype.findDoctorPaibanWeek").getSql();
		return Db.init().selectList(sql, Record.create().set("week", week));
	}
	
	@Override
	public List<Record> paiban_week_doctor(String login_ids)
	{
		String sql = Blade.dao().getScript("Doctor2queuetype.paiban_week_doctor").getSql();
		return Db.init().selectList(sql, Record.create().set("login_ids", login_ids));
	}
	@Override
	public List<Record> paiban_week_doctor_page(String login_ids,int begin,int size)
	{
		String sql = Blade.dao().getScript("Doctor2queuetype.paiban_week_doctor_page").getSql();
		return Db.init().selectList(sql, Record.create().set("login_ids", login_ids).set("begin", begin).set("end", size));
	}
}
