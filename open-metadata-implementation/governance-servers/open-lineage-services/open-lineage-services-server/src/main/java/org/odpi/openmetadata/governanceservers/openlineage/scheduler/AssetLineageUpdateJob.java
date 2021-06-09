/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.assetlineage.AssetLineage;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static java.time.LocalDateTime.*;

@DisallowConcurrentExecution
public class AssetLineageUpdateJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageUpdateJob.class);

    private static final String GLOSSARY_TERM = "GlossaryTerm";
    private static final String RUN_ASSET_LINEAGE_UPDATE_JOB = "Polling AssetLineage OMAS for changes as of time {} {}";
    private static final String RUNNING_FAILURE = "AssetLineageUpdateJob task execution at {} {} failed because of the following exception {}";
    private static final String ASSET_LINEAGE_CONFIG_DEFAULT_VALUE_ERROR = "AssetLineageUpdateJob default value" +
            " was defined as '{}' and it should have an ISO-8601 format such as '{}'. The job will shutdown and won't start again. " +
            "Correct the default value and restart the server instance.";
    private static final String LAST_UPDATE_TIME_UNKNOWN = "Last update time unknown";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime localDateTime = now(ZoneId.systemDefault());
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        performTask(localDateTime, dataMap);
    }

    /**
     * Calls the Asset Lineage client to determine updates for Glossary Terms starting from the last saved time.
     * Then it calls the connector to save the current run time.
     *
     * @param localDateTime the time when the job last run successfully, also the time to save in the graph
     * @param dataMap       the job context data map containing useful data to run the job
     */
    private void performTask(LocalDateTime localDateTime, JobDataMap dataMap) throws JobExecutionException {
        AssetLineage assetLineageClient = (AssetLineage) dataMap.get(JobConstants.ASSET_LINEAGE_CLIENT);
        String localServerName = (String) dataMap.get(JobConstants.ASSET_LINEAGE_SERVER_NAME);
        String localServerUserId = (String) dataMap.get(JobConstants.LOCAL_SERVER_USER_ID);

        try {

            LineageGraph lineageGraph = (LineageGraph) dataMap.get(JobConstants.OPEN_LINEAGE_GRAPH_STORE);
            String configAssetLineageDefaultTime = (String) dataMap.get(JobConstants.CONFIG_ASSET_LINEAGE_LAST_UPDATE_TIME);
            Optional<Long> storedAssetLineageUpdateTime = lineageGraph.getAssetLineageUpdateTime();
            Optional<LocalDateTime> assetLineageLastUpdateTime = getAssetLineageLastUpdateTime(configAssetLineageDefaultTime,
                    storedAssetLineageUpdateTime, localDateTime);
            assetLineageLastUpdateTime.ifPresent(lastUpdateTime -> log.debug(RUN_ASSET_LINEAGE_UPDATE_JOB, lastUpdateTime, ZoneId.systemDefault().getId()));
            assetLineageClient.publishEntities(localServerName, localServerUserId, GLOSSARY_TERM, assetLineageLastUpdateTime);

        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            log.warn(RUNNING_FAILURE, localDateTime, ZoneId.systemDefault(), e.getMessage());
        }
    }

    /**
     *
     * Gets the best candidate for last known time when lineage was published for entities.
     * This timestamp is kept in the graph store, if present this is used.
     * If not, tries the default preset value provided by the user in the configuration document.
     * In none of the above, empty value is returned - means most probably no initial lineage load process was completed before.
     *
     * @param configAssetLineageDefaultTime the asset lineage config default time
     * @param storedUpdateTime    the update time retrieved form the graph store
     * @param localDateTime       the local date time indicating the actual job execution time
     * @return the asset lineage last known update time
     */
    private Optional<LocalDateTime> getAssetLineageLastUpdateTime(String configAssetLineageDefaultTime, Optional<Long> storedUpdateTime,
                                                                  LocalDateTime localDateTime) throws JobExecutionException {

        if (storedUpdateTime.isPresent()) {
            return Optional.of(LocalDateTime.ofInstant(Instant.ofEpochMilli(storedUpdateTime.get()), ZoneId.systemDefault()));
        } else if (StringUtils.isNotEmpty(configAssetLineageDefaultTime)) {
            try {
                return Optional.of(LocalDateTime.parse(configAssetLineageDefaultTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (DateTimeParseException exception) {
                log.error(ASSET_LINEAGE_CONFIG_DEFAULT_VALUE_ERROR, configAssetLineageDefaultTime, localDateTime);
                JobExecutionException jobExecutionException = new JobExecutionException(exception);
                jobExecutionException.setUnscheduleAllTriggers(true);
                throw jobExecutionException;
            }
        }
        log.debug(LAST_UPDATE_TIME_UNKNOWN);
        return Optional.empty();

    }
}