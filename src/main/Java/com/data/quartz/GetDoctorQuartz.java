package com.data.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.data.GetDoctorController;

public class GetDoctorQuartz implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		GetDoctorController getdoctor = new GetDoctorController();
		getdoctor.getDoctor();
	}

}
