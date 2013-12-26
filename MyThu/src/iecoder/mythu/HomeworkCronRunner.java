package iecoder.mythu;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

public class HomeworkCronRunner {
	public static void task() throws SchedulerException {
		// 初始化调度器
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		// 初始化工作细节
		JobDetail jobDetail = newJob(HomeworkJob.class).withIdentity("查看作业",
				"MyThu").build();
		// 初始化Cron触发器
		CronTrigger cronTrigger = newTrigger().withIdentity("查看作业触发器", "MyThu")
				.withSchedule(cronSchedule("0/5 * * * * ?")).forJob("查看作业", "MyThu").build();
		// 调度器控制job
		scheduler.scheduleJob(jobDetail, cronTrigger);
		scheduler.start();
	}

}
