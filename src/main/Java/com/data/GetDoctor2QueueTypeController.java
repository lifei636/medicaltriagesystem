package com.data;

import java.util.List;

import com.common.config.MainConfig;
import com.core.dao.Blade;
import com.shine.model.Doctor;
import com.shine.model.Doctor2queuetype;
import com.shine.model.Item;
import com.shine.model.QueueType;
import com.shine.model.Source;
import com.shine.service.Doctor2queuetypeService;
import com.shine.service.DoctorService;
import com.shine.service.QueueTypeService;
import com.shine.service.impl.Doctor2queuetypeServiceImpl;
import com.shine.service.impl.DoctorServiceImpl;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.system.controller.base.UrlPermissController;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class GetDoctor2QueueTypeController extends UrlPermissController {

	Doctor2queuetypeService doctor2queuetypeservice = new Doctor2queuetypeServiceImpl();
	Doctor2queuetype doctor2queuetype = new Doctor2queuetype();
	DoctorService doctorservice = new DoctorServiceImpl();
	Doctor doctor = new Doctor();
	QueueTypeService queuetypeservice = new QueueTypeServiceImpl();
	QueueType queuetype = new QueueType();

	public void getDoctor2QueueType() {

		String id = "";
		String username = "";
		String password = "";
		String url = "";
		String driver = "";
		List<Source> list = Blade.create(Source.class).findAll();
		for (Source source : list) {
			id = source.getId().toString().trim();
			username = source.getUsername().trim();
			password = source.getPassword().trim();
			url = source.getUrl().trim();
			driver = source.getDriver().trim();
		}
		List<Item> listitem = Blade.create(Item.class).findBy("db_source_id=#{db_source_id}",
				com.core.toolbox.Record.create().set("db_source_id", id));
		String sql = "";
		DruidPlugin druidPlugin = null;
		ActiveRecordPlugin arp = null;
		if (!MainConfig.dbconfigs.contains(id)) {
			druidPlugin = new DruidPlugin(url, username, password, driver);
			arp = new ActiveRecordPlugin(id, druidPlugin);
			druidPlugin.start();
			arp.start();
			MainConfig.dbconfigs.add(String.valueOf(id));
		}
		List<Record> listdoctor2queuetype = null;
		try {
			for (Item item : listitem) {
				if (3 == item.getType()) {
					sql = "" + item.getSqlstring() + "";
					System.out.println(sql);
					listdoctor2queuetype = Db.use(id).find(sql);
				}
			}
		} catch (Exception e) {
			if (druidPlugin != null) {
				druidPlugin.stop();
			}
			if (arp != null) {
				arp.stop();
			}
			MainConfig.dbconfigs.remove(id);
			renderJson(error("sql执行异常"));
			return;
		}

		for (Record record : listdoctor2queuetype) {
			Doctor doctor = doctorservice.findByLogin_id(record.getStr("login_id"));
			int doctor_id = doctor.getDoctor_id();
			QueueType queuetype = queuetypeservice.findBySource_id(record.getStr("queue_type_source_id"));
			int queuetypeid = queuetype.getQueue_type_id();
			List<com.core.toolbox.Record> list1 = doctor2queuetypeservice.findInt(doctor_id, queuetypeid);
			if (!list1.isEmpty()) {
				String sql1 = "DELETE FROM rlt_doctor2queue_type WHERE doctor_id=" + doctor_id + " and queue_type_id="
						+ queuetypeid + "";
				com.core.dao.Db.init().delete(sql1, null);
				doctor2queuetype.setDoctor_id(doctor_id);
				doctor2queuetype.setQueue_type_id(queuetypeid);
				doctor2queuetype.setOnduty(record.getStr("onduty"));
				boolean bool = false;
				try {
					bool = doctor2queuetypeservice.update(doctor2queuetype);
					if (bool) {
						renderJson(success("操作成功"));
					} else {
						renderJson(error("操作失败"));
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				doctor2queuetype.setDoctor_id(doctor_id);
				doctor2queuetype.setQueue_type_id(queuetypeid);
				doctor2queuetype.setOnduty(record.getStr("onduty"));
				boolean bool = false;
				try {
					bool = doctor2queuetypeservice.save(doctor2queuetype);
					if (bool) {
						renderJson(success("操作成功"));
					} else {
						renderJson(error("操作失败"));
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
