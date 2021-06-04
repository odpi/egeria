/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.odpi.openmetadata.accessservices.assetlineage.AssetLineage;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.quartz.Job;

/**
 * Using the JobConfiguration as parent, this class adds more elements to the data map of the job: the asset lineage
 * client and the server where it runs, the local user ID.
 */
public class AssetLineageUpdateJobConfiguration extends JobConfiguration {

    /**
     * Instantiates a new Asset lineage update job configuration.
     *
     * @param lineageGraph          the lineage graph used to store the job's last run time
     * @param jobName               the job name
     * @param jobClass              the job class
     * @param jobInterval           the job interval
     * @param assetLineageClient    the Asset Lineage client
     * @param serverName            the server name where Asset Lineage OMAS runs
     * @param localServerUserId     the local server user ID
     */
    public AssetLineageUpdateJobConfiguration(LineageGraph lineageGraph, String jobName, Class<? extends Job> jobClass,
                                              int jobInterval, String configAssetLineageUpdateTime, AssetLineage assetLineageClient,
                                              String serverName, String localServerUserId) {

        super(lineageGraph, jobName, jobClass, jobInterval);

        jobDetail.getJobDataMap().put(JobConstants.CONFIG_ASSET_LINEAGE_LAST_UPDATE_TIME, configAssetLineageUpdateTime);
        jobDetail.getJobDataMap().put(JobConstants.ASSET_LINEAGE_CLIENT, assetLineageClient);
        jobDetail.getJobDataMap().put(JobConstants.ASSET_LINEAGE_SERVER_NAME, serverName);
        jobDetail.getJobDataMap().put(JobConstants.LOCAL_SERVER_USER_ID, localServerUserId);
    }
}