/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.odpi.openmetadata.governanceservers.openlineage.BufferGraphStore;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.lucene.analysis.pattern.PatternTokenizerFactory.GROUP;

public class JobConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JobConfiguration.class);

    private static Scheduler scheduler;
    private static BufferGraphStore bufferGraphStore;
    public JobConfiguration(BufferGraphStore bufferGraphStore){
        this.bufferGraphStore = bufferGraphStore;
        schedule();
    }

    public void schedule() {
        final String methodName = "schedule";
        log.debug(" QuartzSchedulerApp main thread: {}",Thread.currentThread().getName());

        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();

        } catch (SchedulerException e) {
            log.error("{} could not run the job for bufferGraph", methodName);
        }

        Trigger trigger = buildSimpleSchedulerTrigger();
        try {
            scheduleJob(trigger);
        } catch (Exception e) {
            log.error("The job did not start because of an error with message: {}",e.getMessage());
        }

    }

    private static void scheduleJob(Trigger trigger) throws Exception {

        JobDetail jobDetail = JobBuilder.
                                  newJob(BufferGraphJob.class).
                                  withIdentity("BufferGraphJob", GROUP).
                                  build();
        jobDetail.getJobDataMap().put("openLineageGraphStore", bufferGraphStore);

        scheduler.scheduleJob(jobDetail, trigger);

    }

    private static Trigger buildSimpleSchedulerTrigger() {

        //TODO change seconds now is used for developing
        int INTERVAL_SECONDS = 15;

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("BufferGraphJob", GROUP)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(INTERVAL_SECONDS).repeatForever())
                .build();
        return trigger;
    }

}
