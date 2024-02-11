/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.scheduler;

import org.odpi.openmetadata.governanceservers.lineagewarehouse.connector.LineageWarehouseGraphConnectorBase;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

@DisallowConcurrentExecution
public class LineageGraphJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(LineageGraphJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        LocalDateTime localTime = LocalDateTime.now(ZoneId.of("GMT"));
        log.debug("Run LineageGraphJob task at {} GMT", localTime);

        JobDataMap                         dataMap      = context.getJobDetail().getJobDataMap();
        LineageWarehouseGraphConnectorBase lineageGraph = (LineageWarehouseGraphConnectorBase) dataMap.get(JobConstants.OPEN_LINEAGE_GRAPH_STORE);
        performTask(lineageGraph);
    }

    /**
     * Delegates the call for the scheduler to the connector.
     *
     */
    private void performTask(LineageWarehouseGraphConnectorBase lineageGraph){
        lineageGraph.performLineageGraphJob();
    }
}
