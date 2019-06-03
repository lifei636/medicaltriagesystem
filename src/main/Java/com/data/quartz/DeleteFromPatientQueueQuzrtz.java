package com.data.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.shine.controller.PagerController;
import com.shine.controller.PatientCodeController;
import com.shine.controller.PatientQueueController;
import com.shine.controller.SyncDataLogController;

public class DeleteFromPatientQueueQuzrtz implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SyncDataLogController syncdata = new SyncDataLogController();
		try {
			
			syncdata.deletelog();
		} catch (Exception e) {
			String log_name="清空日志";
			String log_msg="清空日志出错";
			String log_exception="清空日志出错"+e.getMessage();
			Integer log_type=10;
			syncdata.InserLog(log_name, log_msg, log_exception, log_type);
		}
		try {
			PatientCodeController patientcode = new PatientCodeController();
			patientcode.deletecode();
		} catch (Exception e) {
			String log_name="清空患者卡号";
			String log_msg="清空患者卡号出错";
			String log_exception="清空患者卡号出错"+e.getMessage();
			Integer log_type=11;
			syncdata.InserLog(log_name, log_msg, log_exception, log_type);
		}
		try {
			PatientQueueController patientqueue = new PatientQueueController();
			patientqueue.deletepatient();
			patientqueue.copyReservePatient();
			
		} catch (Exception e) {
			String log_name="清空患者信息";
			String log_msg="清空患者信息出错";
			String log_exception="清空患者信息出错"+e.getMessage();
			Integer log_type=12;
			syncdata.InserLog(log_name, log_msg, log_exception, log_type);
		}
		
		try {
			PagerController pager=new PagerController();
			pager.loginoutall();
		} catch (Exception e) {
			String log_name="退出叫号器";
			String log_msg="退出叫号器出错";
			String log_exception="退出叫号器出错"+e.getMessage();
			Integer log_type=13;
			syncdata.InserLog(log_name, log_msg, log_exception, log_type);
		}
		
	}
}
