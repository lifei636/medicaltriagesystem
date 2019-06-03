/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.core.toolbox.log;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.common.vo.OperationLog;
import com.common.vo.ShiroUser;
import com.core.constant.Const;
import com.core.constant.ConstCache;
import com.core.dao.Blade;
import com.core.dao.Db;
import com.core.interfaces.ILog;
import com.core.toolbox.Func;
import com.core.toolbox.Record;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

/**
 * 系统默认日志记录
 * 
 */
public class BladeLogFactory implements ILog {

	@Override
	public String[] logPatten() {
		String[] patten = { "login", "logout", "grant", "save", "update", "remove", "del", "delete", "restore" };
		return patten;
	}

	@Override
	public Record logMaps() {
		Record maps = Record.create().set("login", "登录").set("logout", "登出").set("grant", "授权").set("save", "新增")
				.set("update", "修改").set("remove", "删除").set("del", "删除").set("delete", "删除").set("restore", "还原");
		return maps;
	}

	@Override
	public boolean isDoLog() {
		Record map = CacheKit.get(ConstCache.SYS_CACHE, "parameter_log", new IDataLoader() {
			@Override
			public Object load() {
				return Db.init().selectOne("select para from tfw_parameter where code = #{code}",
						Record.create().set("code", Const.PARA_LOG_CODE));
			}
		});
		if (map.getStr("para").equals("1")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean doLog(ShiroUser user, String msg, String logName, HttpServletRequest request, boolean succeed) {
		if (null == user) {
			return true;
		}
		try {
			OperationLog log = new OperationLog();
			log.setMethod(msg);
			log.setCreatetime(new Date());
			log.setSucceed((succeed) ? "1" : "0");
			log.setUserid(Func.format(user.getId()));
			log.setLogname(logName);
			boolean temp = Blade.create(OperationLog.class).save(log);
			LogKit.info(msg);
			return temp;
		} catch (Exception ex) {
			return false;
		}
	}

}