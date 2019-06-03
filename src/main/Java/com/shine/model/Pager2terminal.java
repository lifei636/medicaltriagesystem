package com.shine.model;

import org.beetl.sql.core.annotatoin.Table;
import com.core.annotation.BindID;
import com.core.model.BaseModel;
import org.beetl.sql.core.annotatoin.AutoID;

/**
 * Generated by Blade. 2017-08-12 15:10:05
 */
@Table(name = "rlt_pager2terminal")
@BindID(name = "pager_id,id")
@SuppressWarnings("serial")
public class Pager2terminal extends BaseModel {
	private Integer id;
	private Integer pager_id;

	@AutoID
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@AutoID
	public Integer getPager_id() {
		return pager_id;
	}

	public void setPager_id(Integer pager_id) {
		this.pager_id = pager_id;
	}

}
