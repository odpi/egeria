/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.collectionmanager.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;

import java.util.List;

/**
 * CollectionManagerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class CollectionManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.COLLECTION_MANAGER;

    private final ViewServiceClientMap<CollectionHandler> viewServiceClientMap;


    /**
     * Set up the Collection Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public CollectionManagerInstance(String                  serverName,
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

        this.viewServiceClientMap = new ViewServiceClientMap<>(CollectionHandler.class,
                                                               serverName,
                                                               auditLog,
                                                               activeViewServices,
                                                               myDescription.getViewServiceFullName(),
                                                               myDescription.getViewServiceURLMarker(),
                                                               maxPageSize);
    }


    /**
     * Return the collection management client.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public CollectionHandler getCollectionHandler(String urlMarker,
                                                  String methodName) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        return viewServiceClientMap.getClient(urlMarker, methodName);
    }
}
