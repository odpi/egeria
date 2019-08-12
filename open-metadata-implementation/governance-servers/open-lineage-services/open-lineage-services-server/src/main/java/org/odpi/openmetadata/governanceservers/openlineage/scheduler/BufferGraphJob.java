/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;

public class BufferGraphJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println("Run QuartzJob at " + localTime.toString());

        BufferGraphJobTask mytask = new BufferGraphJobTask();
        mytask.perform();
    }
}
