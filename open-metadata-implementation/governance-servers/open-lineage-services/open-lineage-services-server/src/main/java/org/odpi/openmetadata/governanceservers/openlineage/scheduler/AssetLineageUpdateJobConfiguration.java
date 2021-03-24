package org.odpi.openmetadata.governanceservers.openlineage.scheduler;

import org.odpi.openmetadata.accessservices.assetlineage.AssetLineage;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.quartz.Job;

public class AssetLineageUpdateJobConfiguration extends JobConfiguration {

    public AssetLineageUpdateJobConfiguration(LineageGraph lineageGraph, String jobName, Class<? extends Job> jobClass,
                                              int jobInterval, AssetLineage asset, String localServerName, String localServerUserId) {

        super(lineageGraph, jobName, jobClass, jobInterval);

        jobDetail.getJobDataMap().put(JobConstants.ASSET_LINEAGE, asset);
        jobDetail.getJobDataMap().put(JobConstants.LOCAL_SERVER_NAME, localServerName);
        jobDetail.getJobDataMap().put(JobConstants.LOCAL_SERVER_USER_ID, localServerUserId);
    }
}