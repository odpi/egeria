/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.odpi.openmetadata.accessservices.assetlineage.AssetLineage;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@DisallowConcurrentExecution
public class AssetLineageUpdateJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageUpdateJob.class);

    private static final String GLOSSARY_TERM = "GlossaryTerm";

    private static final String RUN_ASSET_LINEAGE_UPDATE_JOB = "Run AssetLineageUpdateJob task at {} GMT";
    private static final String RUNNING_FAILURE = "AssetLineageUpdateJob task execution at {} GMT failed because of the following exception {}";

    @Override
    public void execute(JobExecutionContext context) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("GMT"));
        log.debug(RUN_ASSET_LINEAGE_UPDATE_JOB, localDateTime);
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        performTask(localDateTime, dataMap);
    }

    /**
     * Calls the Asset Lineage client to determine updates for Glossary Terms starting from the last saved time.
     * Then it calls the connector to save the current run time.
     * @param localDateTime the time when the job last run successfully, also the time to save in the graph
     * @param dataMap the job context data map containing useful data to run the job
     */
    private void performTask(LocalDateTime localDateTime, JobDataMap dataMap) {
        AssetLineage assetLineageClient = (AssetLineage) dataMap.get(JobConstants.ASSET_LINEAGE_CLIENT);
        String localServerName = (String) dataMap.get(JobConstants.ASSET_LINEAGE_SERVER_NAME);
        String localServerUserId = (String) dataMap.get(JobConstants.LOCAL_SERVER_USER_ID);

        try {
            LineageGraph lineageGraph = (LineageGraph) dataMap.get(JobConstants.OPEN_LINEAGE_GRAPH_STORE);
            Optional<LocalDateTime> assetLineageLastUpdateTime = lineageGraph.getAssetLineageUpdateTime();

            assetLineageClient.publishEntities(localServerName, localServerUserId, GLOSSARY_TERM, assetLineageLastUpdateTime);

            lineageGraph.saveAssetLineageUpdateTime(localDateTime);

        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            log.warn(RUNNING_FAILURE, localDateTime, e.getMessage());
        }
    }
}