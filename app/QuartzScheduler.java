import common.utils.Log;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import play.Configuration;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by acmac on 2016/05/20.
 */
public class QuartzScheduler {

    private static final String TAG = QuartzScheduler.class.getSimpleName();

    private static Scheduler scheduler;

    public static void start() {
        try {
            Log.i(TAG, "start");

            if (scheduler == null) {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
            }

            initJobs();

            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        try {
            Log.i(TAG, "shutdown");

            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private static void initJobs() {
        Log.i(TAG, "initJobs start...");

        Configuration configuration = Configuration.root().getConfig("quartz_jobs");

        configuration.entrySet().forEach(entry -> {
            String key = entry.getKey();
            String value = configuration.getString(key);
            Log.i(TAG, "initJobs", key, value);

            try {
                scheduleCronJob((Class<? extends Job>) Class.forName(key), value);
            } catch (SchedulerException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        Log.i(TAG, "initJobs success");
    }

    public static Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(jobDetail, trigger);
    }

    public static Date scheduleCronJob(Class <? extends Job> jobClass, String cronExpression) throws SchedulerException {
        JobDetail jobDetail = newJob(jobClass).withIdentity(jobClass.getSimpleName()).build();

        Trigger trigger = newTrigger().withSchedule(cronSchedule(cronExpression)).build();

        return scheduleJob(jobDetail, trigger);
    }

}
