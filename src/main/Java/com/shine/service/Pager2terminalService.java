package com.shine.service;

import com.core.base.service.IService;
import com.core.toolbox.Record;
import com.shine.model.Pager2terminal;

/**
 * Generated by Blade. 2017-08-12 15:10:05
 */
public interface Pager2terminalService extends IService<Pager2terminal> {

	public Record findByIP(String ip);
}