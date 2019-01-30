/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import org.odpi.openmetadata.accessservices.assetlineage.contentmanager.ReportHandler;


/**
 * AssetLineageServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetLineageServicesInstance
{
    private ReportHandler reportHandler;
    private String                 serverName;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param reportHandler link to the repository responsible for servicing the REST calls.
     */
    public AssetLineageServicesInstance(ReportHandler reportHandler,
                                           String        serverName)  {
        this.reportHandler = reportHandler;
        this.serverName    = serverName;

        AssetLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }


    /**
     * Return the report creator for this server.
     *
     * @return ReportCreator object
     */
    public ReportHandler getReportHandler()  {
        return this.reportHandler;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        AssetLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
