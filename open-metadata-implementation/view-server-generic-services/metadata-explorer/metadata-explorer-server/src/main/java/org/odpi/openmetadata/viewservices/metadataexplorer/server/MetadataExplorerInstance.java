/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.OpenMetadataStoreHandler;
import org.odpi.openmetadata.viewservices.metadataexplorer.ffdc.MetadataExplorerErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MetadataExplorerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class MetadataExplorerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.METADATA_EXPLORER;

    /*
     * This map caches clients for specific view services/access services.
     */
    private final Map<String, OpenMetadataStoreHandler> openMetadataHandlerMap = new HashMap<>();
    private final List<ViewServiceConfig> activeViewServices;

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

        this.activeViewServices = activeViewServices;
    }




    /**
     * Return the open metadata handler.
     *
     * @param viewServiceURLMarker  view service URL marker
     * @param methodName calling method
     * @return client
     */
    public OpenMetadataStoreHandler getOpenMetadataHandler(String viewServiceURLMarker,
                                                           String methodName) throws InvalidParameterException
    {
        OpenMetadataStoreHandler openMetadataHandler = null;

        if (viewServiceURLMarker != null)
        {
            /*
             * Clients are created on demand
             */
            openMetadataHandler = openMetadataHandlerMap.get(viewServiceURLMarker);

            if (openMetadataHandler == null)
            {
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceConfig.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    if (localServerUserPassword == null)
                                    {
                                        openMetadataHandler = new OpenMetadataStoreHandler(viewServiceConfig.getOMAGServerName(),
                                                                                           viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                           accessServiceDescription.getAccessServiceURLMarker(),
                                                                                           maxPageSize);
                                    }
                                    else
                                    {
                                        openMetadataHandler = new OpenMetadataStoreHandler(viewServiceConfig.getOMAGServerName(),
                                                                                           viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                           accessServiceDescription.getAccessServiceURLMarker(),
                                                                                           localServerUserId,
                                                                                           localServerUserPassword,
                                                                                           maxPageSize);
                                    }

                                    openMetadataHandlerMap.put(viewServiceURLMarker, openMetadataHandler);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (openMetadataHandler == null)
        {
            throw new InvalidParameterException(MetadataExplorerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return openMetadataHandler;
    }

}
