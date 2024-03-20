/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.scheduler;

import org.odpi.openmetadata.governanceservers.lineagewarehouse.connector.LineageWarehouseGraphConnectorBase;
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

/**
 * Used for setting up the configuration for a Quartz scheduled job using the lineage graph as part of the data map.
 */
public class JobConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JobConfiguration.class);

    private static final String SCHEDULER_STARTING = "QuartzSchedulerApp main thread: {}";
    private static final String SCHEDULER_RUNNING_ERROR = "{} could not start the scheduler";
    private static final String JOB_RUNNING_ERROR = "The job did not start because of an error with message: {}";
    private static final String SCHEDULER_SHUTDOWN_ERROR = "Exception while attempting to shutdown the scheduler instance, the message is: {}";

    private final LineageWarehouseGraphConnectorBase lineageWarehouseGraphConnector;
    private final int                                jobInterval;
    private final String jobName;
    private final Class <? extends Job> jobClass;

    private Scheduler scheduler;

    final JobDetail jobDetail;


    /**
     * Instantiates a new Job configuration.
     *
     * @param lineageWarehouseGraphConnector the lineage graph
     * @param jobName      the job name
     * @param jobClass     the job class
     * @param jobInterval  the job interval
     */
    public JobConfiguration(LineageWarehouseGraphConnectorBase lineageWarehouseGraphConnector, String jobName, Class <? extends Job> jobClass, int jobInterval) {
        this.lineageWarehouseGraphConnector = lineageWarehouseGraphConnector;
        this.jobName                        = jobName;
        this.jobClass                       = jobClass;
        this.jobInterval                    = jobInterval;
        this.jobDetail                      = JobBuilder.newJob(jobClass).withIdentity(jobName, GROUP).build();
    }

    /**
     * Start a scheduler and a job using it.
     */
    public void schedule() {
        final String methodName = "schedule";
        log.debug(SCHEDULER_STARTING, Thread.currentThread().getName());
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.startDelayed(10); // delaying the start for 10 seconds so we get cleaner server startup sequence
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

    private void scheduleJob(Trigger trigger) throws SchedulerException {
        if (lineageWarehouseGraphConnector != null) {
            jobDetail.getJobDataMap().put(JobConstants.OPEN_LINEAGE_GRAPH_STORE, lineageWarehouseGraphConnector);
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    private Trigger buildSimpleSchedulerTrigger() {
        return TriggerBuilder.newTrigger().withIdentity(jobName, GROUP)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(jobInterval).repeatForever())
                .build();
    }

}
