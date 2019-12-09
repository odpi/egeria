/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.odpi.openmetadata.governanceservers.openlineage.buffergraph.BufferGraph;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@DisallowConcurrentExecution
public class BufferGraphJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(BufferGraphJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println("Run QuartzJob at " + localTime);

        JobKey key = context.getJobDetail().getKey();

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        BufferGraph bufferGraph = (BufferGraph) dataMap.get("openLineageGraphStore");

        performTask(bufferGraph);
    }

    private void performTask(BufferGraph bufferGraph){
        bufferGraph.schedulerTask();
    }
}
