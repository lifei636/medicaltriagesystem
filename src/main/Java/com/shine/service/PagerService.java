package com.shine.service;

import java.util.List;

import com.core.base.service.IService;
import com.core.toolbox.Record;
import com.shine.model.Pager;

/**
 * Generated by Blade. 2017-08-03 10:50:24
 */
public interface PagerService extends IService<Pager> {

	// \u67E5\u8BE2\u6240\u6709\u5206\u8BCA\u53F0\u540D\u79F0
	public List<Record> queryTriageName();

	// \u6839\u636E\u53EB\u53F7\u5668\u540D\u79F0\u67E5\u8BE2
	public Pager findByName(String name);

	// \u6839\u636Eip\u67E5\u8BE2
	public Pager findByIP(String ip);

	public int updatepagerinsert(String login_id, String ip);

	public int updatedoctorid(String pager_ip, String login_id);

	public Record call_login(String login_id);

	public boolean updatepagerdelete(String ip);

	public int logout(String pager_ip, String login_id);

	public Record findip(String ip);
	
	public int loginoutall();
}