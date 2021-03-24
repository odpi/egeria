/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.quartz.Job;
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

    private static final String SCHEDULER_STARTING = "QuartzSchedulerApp main thread: {}";
    private static final String SCHEDULER_RUNNING_ERROR = "{} could not run the job for LineageGraph";
    private static final String JOB_RUNNING_ERROR = "The job did not start because of an error with message: {}";
    private static final String SCHEDULER_SHUTDOWN_ERROR = "Exception while attempting to shutdown the scheduler instance, the message is: {}";

    static Scheduler scheduler;
    static LineageGraph lineageGraph;
    static int jobInterval;
    static String jobName;
    static Class <? extends Job> jobClass;
    static JobDetail jobDetail;

    public JobConfiguration(LineageGraph lineageGraph, String jobName, Class <? extends Job> jobClass, int jobInterval) {
        JobConfiguration.lineageGraph = lineageGraph;
        JobConfiguration.jobName = jobName;
        JobConfiguration.jobClass = jobClass;
        JobConfiguration.jobInterval = jobInterval;
        JobConfiguration.jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, GROUP).build();
    }

    public void schedule() {
        final String methodName = "schedule";
        log.debug(SCHEDULER_STARTING, Thread.currentThread().getName());
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            log.error(SCHEDULER_RUNNING_ERROR, methodName);
        }

        Trigger trigger = buildSimpleSchedulerTrigger();
        try {
            scheduleJob(trigger);
        } catch (Exception e) {
            log.error(JOB_RUNNING_ERROR,e.getMessage());
        }

    }

    /**
     *  Stops future job execution by shutting down the scheduler
     */
    public void stop() {
        try {
            if(scheduler != null) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            log.error(SCHEDULER_SHUTDOWN_ERROR, e.getMessage());
        }
    }

    private static void scheduleJob(Trigger trigger) throws Exception {
        if (lineageGraph != null) {
            jobDetail.getJobDataMap().put(JobConstants.OPEN_LINEAGE_GRAPH_STORE, lineageGraph);
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    private static Trigger buildSimpleSchedulerTrigger() {
        return TriggerBuilder.newTrigger().withIdentity(jobName, GROUP)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(jobInterval).repeatForever())
                .build();
    }

}
