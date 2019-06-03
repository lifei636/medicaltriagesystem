package com.shine.model;

import java.util.Date;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import com.core.annotation.BindID;
import com.core.model.BaseModel;

@Table(name = "db_sql")
@BindID(name = "id")
@SuppressWarnings("serial")
public class DbSql extends BaseModel {
	private Integer id;
	private Integer db_source_id;
	private double interval;
	private Date last_time;
	private String outsql;
	private Integer type;

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

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public Date getLast_time() {
		return last_time;
	}

	public void setLast_time(Date last_time) {
		this.last_time = last_time;
	}

	public String getOutsql() {
		return outsql;
	}

	public void setOutsql(String outsql) {
		this.outsql = outsql;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
