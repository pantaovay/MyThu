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
		// ��ʼ��������
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		// ��ʼ������ϸ��
		JobDetail jobDetail = newJob(HomeworkJob.class).withIdentity("�鿴��ҵ",
				"MyThu").build();
		// ��ʼ��Cron������
		CronTrigger cronTrigger = newTrigger().withIdentity("�鿴��ҵ������", "MyThu")
				.withSchedule(cronSchedule("0/5 * * * * ?")).forJob("�鿴��ҵ", "MyThu").build();
		// ����������job
		scheduler.scheduleJob(jobDetail, cronTrigger);
		scheduler.start();
	}

}
