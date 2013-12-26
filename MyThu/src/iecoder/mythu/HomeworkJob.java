package iecoder.mythu;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HomeworkJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// »æÖÆ×÷ÒµGUI
		WindowHomeworkInfo.createUI();
	}

}
