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
	// Ƶ��
	public static String frequency = "1";
	/*
	 * ��ʼ�Զ���������
	 */
	public static void task() throws SchedulerException {
		// ��ʼ��������
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		HomeworkCronRunner.scheduler = schedulerFactory.getScheduler();
		// ��ʼ������ϸ��
		JobDetail jobDetail = newJob(HomeworkJob.class).withIdentity("�鿴��ҵ",
				"MyThu").build();
		// ��ʼ��Cron������
		CronTrigger cronTrigger = newTrigger().withIdentity("�鿴��ҵ������", "MyThu")
				.withSchedule(cronSchedule("0 0 0/" + HomeworkCronRunner.frequency + " * * ?")).forJob("�鿴��ҵ", "MyThu").build();
		// ����������job
		HomeworkCronRunner.scheduler.scheduleJob(jobDetail, cronTrigger);
		HomeworkCronRunner.scheduler.start();
	}
	
	/*
	 * �������
	 */
	public static void stop() throws SchedulerException {
		HomeworkCronRunner.scheduler.shutdown(true);
	}
}
