/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.lucene.analysis.pattern.PatternTokenizerFactory.GROUP;

public class JobConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JobConfiguration.class);

    private static Scheduler scheduler;
    private static LineageGraph lineageGraph;
    private static int jobInterval;

    public JobConfiguration(LineageGraph lineageGraph, int jobInterval){
        this.lineageGraph = lineageGraph;
        this.jobInterval = jobInterval;
        schedule();
    }

    public void schedule() {
        final String methodName = "schedule";
        log.debug(" QuartzSchedulerApp main thread: {}",Thread.currentThread().getName());
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("{} could not run the job for LineageGraph", methodName);
        }

        Trigger trigger = buildSimpleSchedulerTrigger();
        try {
            scheduleJob(trigger);
        } catch (Exception e) {
            log.error("The job did not start because of an error with message: {}",e.getMessage());
        }

    }

    private static void scheduleJob(Trigger trigger) throws Exception {

        if(lineageGraph != null) {
            JobDetail jobDetail = JobBuilder.
                    newJob(LineageGraphJob.class).
                    withIdentity("LineageGraphJob", GROUP).
                    build();
            jobDetail.getJobDataMap().put("openLineageGraphStore", lineageGraph);
            scheduler.scheduleJob(jobDetail, trigger);
        }

    }

    private static Trigger buildSimpleSchedulerTrigger() {

        return TriggerBuilder.newTrigger().withIdentity("LineageGraphJob", GROUP)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(jobInterval).repeatForever())
                .build();
    }

}
