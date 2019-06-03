package com.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.common.config.MainConfig;
import com.core.dao.Blade;
import com.shine.model.Item;
import com.shine.model.QueueType;
import com.shine.model.Source;
import com.shine.service.QueueTypeService;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.system.controller.base.UrlPermissController;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class GetQueueTypeController extends UrlPermissController {

	public void getQueueType() {
		QueueTypeService queuetypeservice = new QueueTypeServiceImpl();
		QueueType queuetype = new QueueType();
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
		List<Record> listqueuetype = null;
		try {
			for (Item item : listitem) {
				if (2 == item.getType()) {
					sql = "" + item.getSqlstring() + "";
					System.out.println(sql);
					listqueuetype = Db.use(id).find(sql);
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
		for (Record record : listqueuetype) {
			QueueType qt = queuetypeservice.findBySource_id(record.getStr("source_id"));
			if (null != qt && qt.getSource_id() != null) {
				boolean bool = queuetypeservice.updateBy(
						"name='" + record.getStr("name") + "',displayname='" + record.getStr("displayname") + "'",
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
	}
}
