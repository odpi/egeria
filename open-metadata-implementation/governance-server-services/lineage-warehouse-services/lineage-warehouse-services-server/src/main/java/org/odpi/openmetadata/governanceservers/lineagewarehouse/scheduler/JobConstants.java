/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.scheduler;

public class JobConstants {
    public static final String OPEN_LINEAGE_GRAPH_STORE = "openLineageGraphStore";
    public static final String ASSET_LINEAGE_CLIENT = "assetLineageClient";
    public static final String ASSET_LINEAGE_SERVER_NAME = "assetLineageServerName";
    public static final String LOCAL_SERVER_USER_ID = "localServerUserId";
    public static final String ASSET_LINEAGE_UPDATE_JOB = "AssetLineageUpdateJob";
    public static final String OPEN_LINEAGE_STORAGE_SERVICE = "OpenLineageStorageService";
    public static final String CONFIG_ASSET_LINEAGE_LAST_UPDATE_TIME = "configAssetLineageLastUpdateTime";
    public static final String LINEAGE_GRAPH_JOB = "LineageGraphJob";
    public static final int DEFAULT_JOB_INTERVAL_IN_SECONDS = 120;

    private JobConstants() {
    }
}
