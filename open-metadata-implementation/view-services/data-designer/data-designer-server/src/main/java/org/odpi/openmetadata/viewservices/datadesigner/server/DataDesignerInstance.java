/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataValueSpecificationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataFieldHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataStructureHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

/**
 * DataDesignerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class DataDesignerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.DATA_DESIGNER;

    private final DataValueSpecificationHandler dataValueSpecificationHandler;
    private final DataFieldHandler              dataFieldHandler;
    private final DataStructureHandler dataStructureHandler;
    private final AssetHandler         assetHandler;



    /**
     * Set up the Data Designer OMVS instance
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
    public DataDesignerInstance(String   serverName,
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

        OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName,
                                                                                  remoteServerURL,
                                                                                  localServerSecretsStoreProvider,
                                                                                  localServerSecretsStoreLocation,
                                                                                  localServerSecretsStoreCollection,
                                                                                  maxPageSize,
                                                                                  auditLog);

        dataValueSpecificationHandler = new DataValueSpecificationHandler(serverName,
                                                                          auditLog,
                                                                          myDescription.getViewServiceFullName(),
                                                                          openMetadataClient);

        dataFieldHandler = new DataFieldHandler(serverName,
                                                auditLog,
                                                myDescription.getViewServiceFullName(),
                                                openMetadataClient);

        dataStructureHandler = new DataStructureHandler(serverName,
                                                auditLog,
                                                myDescription.getViewServiceFullName(),
                                                openMetadataClient);

        assetHandler = new AssetHandler(serverName,
                                        auditLog,
                                        myDescription.getViewServiceFullName(),
                                        openMetadataClient);
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * data design artifacts.
     *
     * @return client
     */
    public DataValueSpecificationHandler getDataValueSpecificationHandler()
    {
        return dataValueSpecificationHandler;
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * data design artifacts.
     *
     * @return client
     */
    public DataFieldHandler getDataFieldHandler()
    {
        return dataFieldHandler;
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * data design artifacts.
     *
     * @return client
     */
    public DataStructureHandler getDataStructureHandler()
    {
        return dataStructureHandler;
    }


    /**
     * Return the handler for working with reports, report types and results sets.
     *
     * @return handler
     */
    public AssetHandler getAssetHandler()
    {
        return assetHandler;
    }
}
