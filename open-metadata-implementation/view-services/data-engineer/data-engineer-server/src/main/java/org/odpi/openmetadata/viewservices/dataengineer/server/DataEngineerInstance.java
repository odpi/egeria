/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataengineer.server;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.EgeriaConnectedAssetClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

/**
 * DataEngineerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class DataEngineerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.DATA_ENGINEER;

    private final ConnectedAssetClient            connectedAssetClient;


    /**
     * Set up the Data Engineer OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public DataEngineerInstance(String   serverName,
                                AuditLog auditLog,
                                String   localServerUserId,
                                String   localServerSecretsStoreProvider,
                                String   localServerSecretsStoreLocation,
                                String   localServerSecretsStoreCollection,
                                int      maxPageSize,
                                String   remoteServerName,
                                String   remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        connectedAssetClient = new EgeriaConnectedAssetClient(remoteServerName,
                                                              remoteServerURL,
                                                              localServerSecretsStoreProvider,
                                                              localServerSecretsStoreLocation,
                                                              localServerSecretsStoreCollection,
                                                              maxPageSize,
                                                              auditLog);
    }




    /**
     * Return the connected asset client.  This client is from Open Connector Framework (OCF) and is for retrieving information about
     * assets and creating connectors.
     *
     * @return client
     */
    public ConnectedAssetClient getConnectedAssetClient()
    {
        return connectedAssetClient;
    }

}
