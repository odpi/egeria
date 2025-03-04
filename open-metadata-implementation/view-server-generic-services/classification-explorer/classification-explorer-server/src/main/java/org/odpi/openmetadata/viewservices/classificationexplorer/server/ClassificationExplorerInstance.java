/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.classificationexplorer.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.viewservices.classificationexplorer.ffdc.ClassificationExplorerErrorCode;
import org.odpi.openmetadata.viewservices.classificationexplorer.handler.OpenMetadataStoreHandler;
import org.odpi.openmetadata.viewservices.classificationexplorer.handler.StewardshipManagementHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassificationExplorerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ClassificationExplorerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.CLASSIFICATION_EXPLORER;

    /*
     * This map caches clients for specific view services/access services.
     */
    private final Map<String, StewardshipManagementHandler> stewardshipManagementHandlerHashMap = new HashMap<>();

    /*
     * This map caches clients for specific view services/access services.
     */
    private final Map<String, OpenMetadataStoreHandler> openMetadataHandlerMap = new HashMap<>();

    private final List<ViewServiceConfig> activeViewServices;

    /**
     * Set up the Classification Explorer OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public ClassificationExplorerInstance(String                  serverName,
                                          AuditLog                auditLog,
                                          String                  localServerUserId,
                                          int                     maxPageSize,
                                          String                  remoteServerName,
                                          String                  remoteServerURL,
                                          List<ViewServiceConfig> activeViewServices) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.activeViewServices = activeViewServices;
    }


    /**
     * Return the collaboration manager handler.
     *
     * @param viewServiceURLMarker optional view service URL marker
     * @param methodName calling method
     * @return client
     */
    public StewardshipManagementHandler getStewardshipManagerHandler(String viewServiceURLMarker,
                                                                     String methodName) throws InvalidParameterException
    {

        StewardshipManagementHandler collaborationManagerHandler = null;

        if (viewServiceURLMarker != null)
        {
            collaborationManagerHandler = stewardshipManagementHandlerHashMap.get(viewServiceURLMarker);

            if (collaborationManagerHandler == null)
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
                                    collaborationManagerHandler = new StewardshipManagementHandler(serverName,
                                                                                                   viewServiceConfig.getOMAGServerName(),
                                                                                                   viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                                   auditLog,
                                                                                                   accessServiceDescription.getAccessServiceURLMarker(),
                                                                                                   maxPageSize);

                                    stewardshipManagementHandlerHashMap.put(viewServiceURLMarker,
                                                                            collaborationManagerHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (collaborationManagerHandler == null)
        {
            throw new InvalidParameterException(ClassificationExplorerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return collaborationManagerHandler;
    }




    /**
     * Return the open metadata handler.
     *
     * @param viewServiceURLMarker  view service URL marker
     * @param methodName calling method
     * @return client
     */
    public OpenMetadataStoreHandler getOpenMetadataStoreHandler(String viewServiceURLMarker,
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
                                    openMetadataHandler = new OpenMetadataStoreHandler(accessServiceDescription.getAccessServiceURLMarker(),
                                                                                       viewServiceConfig.getOMAGServerName(),
                                                                                       viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                       maxPageSize);

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
            throw new InvalidParameterException(ClassificationExplorerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return openMetadataHandler;
    }

}
