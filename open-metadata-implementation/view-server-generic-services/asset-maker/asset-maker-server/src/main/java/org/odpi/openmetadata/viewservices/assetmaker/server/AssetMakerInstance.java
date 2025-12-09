/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetmaker.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;

import java.util.List;

/**
 * AssetMakerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class AssetMakerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.ASSET_MAKER;

    /*
     * These maps cache clients for specific view services/access services.
     */
    private final ViewServiceClientMap<AssetHandler>      assetHandlerMap;
    private final ViewServiceClientMap<SchemaAttributeHandler> schemaAttributeHandlerMap;


    /**
     * Set up the Asset Maker OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public AssetMakerInstance(String                  serverName,
                              AuditLog                auditLog,
                              String                  localServerUserId,
                              int                     maxPageSize,
                              String                  remoteServerName,
                              String                  remoteServerURL,
                              List<ViewServiceConfig> activeViewServices)
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.assetHandlerMap = new ViewServiceClientMap<>(AssetHandler.class,
                                                          serverName,
                                                          auditLog,
                                                          activeViewServices,
                                                          myDescription.getViewServiceFullName(),
                                                          myDescription.getViewServiceURLMarker(),
                                                          maxPageSize);

        this.schemaAttributeHandlerMap = new ViewServiceClientMap<>(SchemaAttributeHandler.class,
                                                                    serverName,
                                                                    auditLog,
                                                                    activeViewServices,
                                                                    myDescription.getViewServiceFullName(),
                                                                    myDescription.getViewServiceURLMarker(),
                                                                    maxPageSize);
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * asset artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public AssetHandler getAssetHandler(String urlMarker,
                                        String methodName) throws InvalidParameterException,
                                                                  PropertyServerException
    {
        return assetHandlerMap.getClient(urlMarker, methodName);
    }




    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * schema attribute artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public SchemaAttributeHandler getSchemaAttributeHandler(String urlMarker,
                                                            String methodName) throws InvalidParameterException, PropertyServerException
    {
        return schemaAttributeHandlerMap.getClient(urlMarker, methodName);
    }
}
