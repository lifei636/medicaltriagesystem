package com.shine.model;

import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;

import com.core.annotation.BindID;
import com.core.model.BaseModel;

import org.beetl.sql.core.annotatoin.AutoID;

/**
 * Generated by Blade. 2017-08-17 14:14:07
 */
@Table(name = "item")
@BindID(name = "id")
@SuppressWarnings("serial")
public class Item extends BaseModel {
	private Integer id;
	private Integer db_source_id;
	private Integer interval;
	private Integer type;
	private String Search_fields;
	private String description;
	private String name;
	private String sqlstring;
	private Date last_time;

	@AutoID
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDb_source_id() {
		return db_source_id;
	}

	public void setDb_source_id(Integer db_source_id) {
		this.db_source_id = db_source_id;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSearch_fields() {
		return Search_fields;
	}

	public void setSearch_fields(String Search_fields) {
		this.Search_fields = Search_fields;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSqlstring() {
		return sqlstring;
	}

	public void setSqlstring(String sqlstring) {
		this.sqlstring = sqlstring;
	}

	public Date getLast_time() {
		return last_time;
	}

	public void setLast_time(Date last_time) {
		this.last_time = last_time;
	}

}