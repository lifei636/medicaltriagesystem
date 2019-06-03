package com.shine.service;

import java.util.List;

import com.core.base.service.IService;
import com.core.toolbox.Record;
import com.shine.model.PatientCode;

/**
 * Generated by Blade. 2017-08-03 10:50:24
 */
public interface PatientCodeService extends IService<PatientCode> {

	public int deletecode();

	public List<Record> listall();
}