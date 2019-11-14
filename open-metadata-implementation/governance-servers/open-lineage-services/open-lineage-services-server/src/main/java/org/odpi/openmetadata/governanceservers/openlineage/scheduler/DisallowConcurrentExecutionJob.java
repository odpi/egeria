package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@DisallowConcurrentExecution
public class DisallowConcurrentExecutionJob implements Job {

    private final Logger log = LoggerFactory.getLogger(DisallowConcurrentExecutionJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("DisallowConcurrentExecutionJob executed on {}", new Date());
    }
}