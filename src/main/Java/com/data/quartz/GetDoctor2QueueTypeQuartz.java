package com.data.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.data.GetDoctor2QueueTypeController;

public class GetDoctor2QueueTypeQuartz implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		GetDoctor2QueueTypeController getdoctor2queuetype = new GetDoctor2QueueTypeController();
		getdoctor2queuetype.getDoctor2QueueType();

	}

}
