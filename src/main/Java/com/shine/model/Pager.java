package com.shine.model;

import java.util.Date;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import com.core.annotation.BindID;
import com.core.model.BaseModel;

/**
 * Generated by Blade. 2017-08-03 10:50:24
 */
@Table(name = "pager")
@BindID(name = "id")
@SuppressWarnings("serial")
public class Pager extends BaseModel {
	private Integer id;
	private Integer doctor_id;
	private Integer state;
	private Integer triage_id;
	private Integer type;
	private String call_rule;
	private String description;
	private String display_name;
	private String ip;
	private String name;
	private Date login_time;
	private String triage_name;
	/*
	 * private Integer call_pass_first_flag; private Integer call_pass_rule_flag;
	 * private Integer call_return_first_flag; private Integer
	 * call_return_rule_flag;
	 */

	@AutoID
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDoctor_id() {
		return doctor_id;
	}

	public void setDoctor_id(Integer doctor_id) {
		this.doctor_id = doctor_id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getTriage_id() {
		return triage_id;
	}

	public void setTriage_id(Integer triage_id) {
		this.triage_id = triage_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCall_rule() {
		return call_rule;
	}

	public void setCall_rule(String call_rule) {
		this.call_rule = call_rule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTriage_name() {
		return triage_name;
	}

	public void setTriage_name(String triage_name) {
		this.triage_name = triage_name;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLogin_time() {
		return login_time;
	}

	public void setLogin_time(Date login_time) {
		this.login_time = login_time;
	}
	/*
	 * public Integer getCall_pass_first_flag() { return call_pass_first_flag; }
	 * 
	 * public void setCall_pass_first_flag(Integer call_pass_first_flag) {
	 * this.call_pass_first_flag = call_pass_first_flag; } public Integer
	 * getCall_pass_rule_flag() { return call_pass_rule_flag; }
	 * 
	 * public void setCall_pass_rule_flag(Integer call_pass_rule_flag) {
	 * this.call_pass_rule_flag = call_pass_rule_flag; } public Integer
	 * getCall_return_first_flag() { return call_return_first_flag; }
	 * 
	 * public void setCall_return_first_flag(Integer call_return_first_flag) {
	 * this.call_return_first_flag = call_return_first_flag; } public Integer
	 * getCall_return_rule_flag() { return call_return_rule_flag; }
	 * 
	 * public void setCall_return_rule_flag(Integer call_return_rule_flag) {
	 * this.call_return_rule_flag = call_return_rule_flag; }
	 */
}
