package com.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.common.config.MainConfig;
import com.core.dao.Blade;
import com.shine.model.Doctor;
import com.shine.model.Item;
import com.shine.model.Source;
import com.shine.service.DoctorService;
import com.shine.service.impl.DoctorServiceImpl;
import com.system.controller.base.UrlPermissController;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class GetDoctorController extends UrlPermissController {

	public void getDoctor() {
		DoctorService doctorservice = new DoctorServiceImpl();
		Doctor doctor = new Doctor();
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
		List<Record> listdoctor = null;

		try {
			for (Item item : listitem) {
				if (1 == item.getType()) {
					sql = "" + item.getSqlstring() + " ";
					System.out.println(sql);
					listdoctor = Db.use(id).find(sql);
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
		for (Item item : listitem) {
			if (1 == item.getType()) {
				for (Record record : listdoctor) {
					Doctor doctor_login_id = doctorservice.findByLogin_id(record.getStr("login_id"));
					if (null != doctor_login_id && doctor_login_id.getDoctor_id() != null) {

						boolean bool = doctorservice.updateBy(
								"name='" + record.getStr("name") + "',department='" + record.getStr("department")
										+ "',title='" + record.getStr("title") + "'",
								" login_id in('" + record.getStr("login_id") + "')", null);
						if (bool) {
							renderJson(success("操作成功"));
						} else {
							renderJson(error("操作失败"));
							return;
						}
					} else {
						doctor.setOpr_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
						doctor.setLogin_id(record.getStr("login_id"));
						doctor.setName(record.getStr("name"));
						doctor.setDepartment(record.getStr("department"));
						doctor.setTitle(record.getStr("title"));
						boolean bool = doctorservice.save(doctor);
						if (bool) {
							renderJson(success("操作成功"));
						} else {
							renderJson(error("操作失败"));
							return;
						}
					}
				}
			}
		}
	}

}
