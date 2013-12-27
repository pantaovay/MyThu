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
	public static Scheduler scheduler;
	// 频率
	public static String frequency = "1";
	/*
	 * 开始自动提醒任务
	 */
	public static void task() throws SchedulerException {
		// 初始化调度器
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		HomeworkCronRunner.scheduler = schedulerFactory.getScheduler();
		// 初始化工作细节
		JobDetail jobDetail = newJob(HomeworkJob.class).withIdentity("查看作业",
				"MyThu").build();
		// 初始化Cron触发器
		CronTrigger cronTrigger = newTrigger().withIdentity("查看作业触发器", "MyThu")
				.withSchedule(cronSchedule("0 0 0/" + HomeworkCronRunner.frequency + " * * ?")).forJob("查看作业", "MyThu").build();
		// 调度器控制job
		HomeworkCronRunner.scheduler.scheduleJob(jobDetail, cronTrigger);
		HomeworkCronRunner.scheduler.start();
	}
	
	/*
	 * 清除任务
	 */
	public static void stop() throws SchedulerException {
		HomeworkCronRunner.scheduler.shutdown(true);
	}
}
