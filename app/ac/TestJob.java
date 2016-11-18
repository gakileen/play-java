package ac;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by acmac on 2016/05/20.
 */
public class TestJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println(new Date() +  " - " + Thread.currentThread().getName());

        System.out.println(context.getTrigger());
    }
}
