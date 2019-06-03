package com.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.common.config.MainConfig;
import com.core.dao.Blade;
import com.shine.model.Doctor;
import com.shine.model.Doctor2queuetype;
import com.shine.model.Item;
import com.shine.model.PatientCode;
import com.shine.model.PatientQueue;
import com.shine.model.QueueType;
import com.shine.model.Source;
import com.shine.service.Doctor2queuetypeService;
import com.shine.service.DoctorService;
import com.shine.service.ItemService;
import com.shine.service.PatientCodeService;
import com.shine.service.PatientQueueService;
import com.shine.service.QueueTypeService;
import com.shine.service.SourceService;
import com.shine.service.impl.Doctor2queuetypeServiceImpl;
import com.shine.service.impl.DoctorServiceImpl;
import com.shine.service.impl.ItemServiceImpl;
import com.shine.service.impl.PatientCodeServiceImpl;
import com.shine.service.impl.PatientQueueServiceImpl;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.shine.service.impl.SourceServiceImpl;
import com.system.controller.base.UrlPermissController;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class GetDataController extends UrlPermissController {
	SourceService sourceservice = new SourceServiceImpl();
	ItemService itemservice = new ItemServiceImpl();
	DoctorService doctorservice = new DoctorServiceImpl();
	Doctor doctor = new Doctor();
	QueueTypeService queuetypeservice = new QueueTypeServiceImpl();
	QueueType queuetype = new QueueType();
	Doctor2queuetypeService doctor2queuetypeservice = new Doctor2queuetypeServiceImpl();
	Doctor2queuetype doctor2queuetype = new Doctor2queuetype();
	PatientQueueService patientqueueservice = new PatientQueueServiceImpl();
	PatientQueue patientqueue = new PatientQueue();
	PatientCodeService patientcodeservice = new PatientCodeServiceImpl();
	PatientCode patientcode = new PatientCode();

	public void getSqlConn() {
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
		List<Record> listqueuetype = null;
		List<Record> listdoctor2queuetype = null;
		List<Record> listpatientqueue = null;
		try {
			for (Item item : listitem) {
				if (1 == item.getType()) {
					sql = "" + item.getSqlstring() + " ";
					System.out.println(sql);
					listdoctor = Db.use(id).find(sql);
				} else if (2 == item.getType()) {
					sql = "" + item.getSqlstring() + "";
					System.out.println(sql);
					listqueuetype = Db.use(id).find(sql);
				} else if (3 == item.getType()) {
					sql = "" + item.getSqlstring() + "";
					System.out.println(sql);
					listdoctor2queuetype = Db.use(id).find(sql);
				} else if (4 == item.getType()) {
					sql = "" + item.getSqlstring() + "";
					System.out.println(sql);
					listpatientqueue = Db.use(id).find(sql);
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
			} else if (2 == item.getType()) {
				for (Record record : listqueuetype) {
					QueueType qt = queuetypeservice.findBySource_id(record.getStr("source_id"));
					if (null != qt && qt.getSource_id() != null) {
						boolean bool = queuetypeservice
								.updateBy(
										"name='" + record.getStr("name") + "',displayname='"
												+ record.getStr("displayname") + "'",
										" where source_id='" + record.getStr("source_id") + "'", null);
						if (bool) {
							renderJson(success("操作成功"));
						} else {
							renderJson(error("操作失败"));
							return;
						}
					} else {

						queuetype.setSource_id(record.getStr("source_id"));
						queuetype.setName(record.getStr("name"));
						queuetype.setDisplayname(record.getStr("displayname"));
						queuetype.setIs_checkin(2);
						queuetype.setIs_reorder(2);
						queuetype.setIs_pretriage(2);
						queuetype.setIs_reserve(2);
						queuetype.setFloor(record.getStr("floor"));
						queuetype.setOpr_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
						boolean bool = queuetypeservice.save(queuetype);
						if (bool) {
							renderJson(success("操作成功"));
						} else {
							renderJson(error("操作失败"));
							return;
						}
					}
				}
			} else if (3 == item.getType()) {
				for (Record record : listdoctor2queuetype) {
					Doctor doctor = doctorservice.findByLogin_id(record.getStr("login_id"));
					int doctor_id = doctor.getDoctor_id();
					QueueType queuetype = queuetypeservice.findBySource_id(record.getStr("queue_type_source_id"));
					int queuetypeid = queuetype.getQueue_type_id();
					List<com.core.toolbox.Record> list1 = doctor2queuetypeservice.findInt(doctor_id, queuetypeid);
					if (!list1.isEmpty()) {
						String sql1 = "DELETE FROM rlt_doctor2queue_type WHERE doctor_id=" + doctor_id
								+ " and queue_type_id=" + queuetypeid + "";
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
			} else if (4 == item.getType()) {
				for (Record record : listpatientqueue) {
					Doctor d = doctorservice.findByLogin_id(record.getStr("doctor_source_id"));
					if (null != d) {
						patientqueue.setDoctor_id(d.getDoctor_id());
					}
					patientqueue.setPatient_id(record.getStr("patient_id"));
					patientqueue.setPatient_name(record.getStr("patient_name"));
					patientqueue.setQueue_type_source_id(record.getStr("queue_type_source_id"));
					QueueType qtype = queuetypeservice.findBySource_id(record.getStr("queue_type_source_id"));
					if (null == qtype) {
						renderJson(error("操作失败"));
						return;
					}
					int queue_type_id = queuetype.getQueue_type_id();
					patientqueue.setQueue_type_id(queue_type_id);
					patientqueue.setSub_queue_order(record.getInt("sub_queue_order"));
					patientqueue.setSub_queue_type(record.getStr("sub_queue_type"));
					patientqueue.setTime_interval(record.getInt("time_interval"));
					if ("1".equals(queuetype.getIs_checkin().toString())) {
						patientqueue.setIs_display(1);
					} else if ("2".equals(queuetype.getIs_checkin().toString())) {
						patientqueue.setIs_display(2);
					}
					if ("1".equals(queuetype.getIs_reorder().toString())) {
						int register_id = 0;
						for (int i = 0; i < listpatientqueue.size(); i++) {
							List<com.core.toolbox.Record> list2 = patientqueueservice
									.maxregisterid(String.valueOf(queue_type_id));
							if (null == list2 || list2.size() == 0) {
								register_id = 1;
							} else {
								register_id = list2.get(0).getInt("register_id");
							}
							patientqueue.setRegister_id(String.valueOf(register_id));
							i++;
						}
					} else if ("2".equals(queuetype.getIs_reorder().toString())) {
						patientqueue.setRegister_id(record.getStr("register_id"));
					} else {
						return;
					}
					patientqueue.setQueue_num(record.getStr("queue_num"));
					patientqueue.setIs_deleted(record.getInt("is_deleted"));
					patientqueue.setFre_date(record.getDate("fre_date"));
					patientqueue.setOpr_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
					patientqueue.setState_patient(0);
					boolean bool = false;
					try {
						bool = patientqueueservice.save(patientqueue);
					} catch (Exception e) {
						e.printStackTrace();
						renderJson(error("操作失败"));
						return;
					}
					if (bool) {
						renderJson(success("操作成功"));
						patientcode.setCode(record.getStr("source_code"));
						PatientQueue qp = patientqueueservice.findByPatient_id(record.getStr("patient_id"));
						int patient_queue_id = qp.getId();
						patientcode.setPatient_queue_id(patient_queue_id);
						PatientCodeService pcser = new PatientCodeServiceImpl();
						boolean boolcode = pcser.save(patientcode);
						if (boolcode) {
							renderJson(success("操作成功"));
						} else {
							renderJson(error("操作失败"));
							return;
						}
					} else {
						renderJson(error("操作失败"));
						return;
					}
				}
			}
		}
	}
}