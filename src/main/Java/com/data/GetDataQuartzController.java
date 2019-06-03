package com.data;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GetDataQuartzController implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		GetDataController getdata = new GetDataController();
		getdata.getSqlConn();

	}
}
