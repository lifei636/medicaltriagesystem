package com.data.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.data.GetPatientQueueAndPatientCodeController;

public class GetPatientQueueAndPatientCodeQuartz implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		GetPatientQueueAndPatientCodeController getpatientqueueandpatientcode = new GetPatientQueueAndPatientCodeController();
		getpatientqueueandpatientcode.getPatientQueueAndPatientCode();

	}

}
