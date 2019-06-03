package com.data.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.data.GetQueueTypeController;

public class GetQueueTypeQuartz implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		GetQueueTypeController getqueuetype = new GetQueueTypeController();
		getqueuetype.getQueueType();

	}

}
