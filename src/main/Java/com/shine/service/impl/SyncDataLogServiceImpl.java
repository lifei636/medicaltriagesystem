package com.shine.service.impl;

import com.core.base.service.impl.BaseService;
import com.core.dao.Blade;
import com.core.dao.Db;
import com.core.toolbox.Record;
import com.shine.model.SyncDataLog;
import com.shine.service.SyncDataLogService;

/**
 * Generated by Blade. 2017-09-18 15:11:34
 */
public class SyncDataLogServiceImpl extends BaseService<SyncDataLog> implements SyncDataLogService {

	@Override
	public int deletesyncdatalog() {
		String sql = Blade.dao().getScript("SyncDataLog.delete").getSql();
		return Db.init().delete(sql, null);
	}
	
	@Override
	public void InsertLog(String log_name,String log_msg,String log_exception,Integer log_type)
	{
		String sql =Blade.dao().getScript("SyncDataLog.InsertLog").getSql();
		Db.init().insert(sql, Record.create().set("log_name", log_name).set("log_msg", log_msg).set("log_exception", log_exception).set("log_type", log_type));
	}

}