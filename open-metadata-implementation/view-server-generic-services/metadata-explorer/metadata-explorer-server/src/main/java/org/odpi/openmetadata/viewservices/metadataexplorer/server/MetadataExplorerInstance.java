/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.EgeriaOpenMetadataStoreHandler;

import java.util.List;

/**
 * MetadataExplorerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class MetadataExplorerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.METADATA_EXPLORER;

    private final ViewServiceClientMap<EgeriaOpenMetadataStoreHandler> openMetadataHandlerMap;


    /**
     * Set up the view service instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public MetadataExplorerInstance(String                  serverName,
                                    AuditLog                auditLog,
                                    String                  localServerUserId,
                                    String                  localServerUserPassword,
                                    int                     maxPageSize,
                                    String                  remoteServerName,
                                    String                  remoteServerURL,
                                    List<ViewServiceConfig> activeViewServices)
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.openMetadataHandlerMap = new ViewServiceClientMap<>(EgeriaOpenMetadataStoreHandler.class,
                                                                 serverName,
                                                                 localServerUserId,
                                                                 localServerUserPassword,
                                                                 auditLog,
                                                                 activeViewServices,
                                                                 myDescription.getViewServiceFullName(),
                                                                 myDescription.getViewServiceURLMarker(),
                                                                 maxPageSize);
    }


    /**
     * Return the open metadata handler.
     *
     * @param urlMarker  view service URL marker
     * @param methodName calling method
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     * @return client
     */
    public OpenMetadataClient getOpenMetadataHandler(String urlMarker,
                                                     String methodName) throws InvalidParameterException,
                                                                               PropertyServerException
    {
        return openMetadataHandlerMap.getClient(urlMarker, methodName);
    }
}
