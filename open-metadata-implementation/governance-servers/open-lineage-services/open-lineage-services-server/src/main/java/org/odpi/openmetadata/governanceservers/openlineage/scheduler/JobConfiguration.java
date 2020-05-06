/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.odpi.openmetadata.governanceservers.openlineage.buffergraph.BufferGraph;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.lucene.analysis.pattern.PatternTokenizerFactory.GROUP;

public class JobConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JobConfiguration.class);

    private static Scheduler scheduler;
    private static BufferGraph bufferGraph;

    public JobConfiguration(BufferGraph bufferGraph){
        this.bufferGraph = bufferGraph;
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

        if(bufferGraph != null) {
            JobDetail jobDetail = JobBuilder.
                    newJob(BufferGraphJob.class).
                    withIdentity("BufferGraphJob", GROUP).
                    build();
            jobDetail.getJobDataMap().put("openLineageGraphStore", bufferGraph);
            scheduler.scheduleJob(jobDetail, trigger);
        }

    }

    private static Trigger buildSimpleSchedulerTrigger() {


//        int INTERVAL_SECONDS = 600;
        //TODO Remove after development
        int INTERVAL_SECONDS = 10;

        return TriggerBuilder.newTrigger().withIdentity("BufferGraphJob", GROUP)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(INTERVAL_SECONDS).repeatForever())
                .build();
    }

}
