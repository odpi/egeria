/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.viewservices.metadataexplorer.ffdc.MetadataExplorerErrorCode;
import org.odpi.openmetadata.viewservices.metadataexplorer.handlers.OpenMetadataHandler;

import java.util.HashMap;
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
    private final Map<String, OpenMetadataHandler> openMetadataHandlerMap = new HashMap<>();

    /**
     * Set up the Metadata Explorer OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public MetadataExplorerInstance(String       serverName,
                                    AuditLog     auditLog,
                                    String       localServerUserId,
                                    int          maxPageSize,
                                    String       remoteServerName,
                                    String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);


        /*
         * Set up the default handler.
         */
        OpenMetadataHandler collaborationManagerHandler = new OpenMetadataHandler(AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceURLMarker(),
                                                                                  remoteServerName,
                                                                                  remoteServerURL,
                                                                                  maxPageSize);

        openMetadataHandlerMap.put(AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceURLMarker(), collaborationManagerHandler);

        openMetadataHandlerMap.put(myDescription.getViewServiceURLMarker(), collaborationManagerHandler);
    }




    /**
     * Return the open metadata handler.
     *
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param methodName calling method
     * @return client
     */
    public OpenMetadataHandler getOpenMetadataHandler(String viewServiceURLMarker,
                                                      String accessServiceURLMarker,
                                                      String methodName) throws InvalidParameterException
    {
        OpenMetadataHandler openMetadataHandler = null;

        if (viewServiceURLMarker != null)
        {
            openMetadataHandler = openMetadataHandlerMap.get(viewServiceURLMarker);

            if (openMetadataHandler == null)
            {
                for (ViewServiceDescription viewServiceDescription : ViewServiceDescription.values())
                {
                    if (viewServiceDescription.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceDescription.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    openMetadataHandler = new OpenMetadataHandler(accessServiceDescription.getAccessServiceURLMarker(),
                                                                                  remoteServerName,
                                                                                  remoteServerURL,
                                                                                  maxPageSize);

                                    openMetadataHandlerMap.put(accessServiceDescription.getAccessServiceURLMarker(), openMetadataHandler);
                                    openMetadataHandlerMap.put(viewServiceURLMarker, openMetadataHandler);
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
            }
        }

        if (openMetadataHandler == null)
        {
            openMetadataHandler = openMetadataHandlerMap.get(accessServiceURLMarker);

            if (openMetadataHandler == null)
            {
                for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                {
                    if (accessServiceDescription.getAccessServiceURLMarker().equals(accessServiceURLMarker))
                    {
                        openMetadataHandler = new OpenMetadataHandler(accessServiceURLMarker,
                                                                      remoteServerName,
                                                                      remoteServerURL,
                                                                      maxPageSize);

                        openMetadataHandlerMap.put(accessServiceURLMarker, openMetadataHandler);
                    }
                }
            }
        }

        if (openMetadataHandler == null)
        {
            throw new InvalidParameterException(MetadataExplorerErrorCode.INVALID_URL_MARKER.getMessageDefinition(accessServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "accessServiceURLMarker");
        }

        return openMetadataHandler;
    }

}
