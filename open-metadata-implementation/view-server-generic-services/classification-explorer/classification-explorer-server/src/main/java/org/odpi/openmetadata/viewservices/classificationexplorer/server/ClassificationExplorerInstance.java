/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.classificationexplorer.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StewardshipManagementHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.EgeriaOpenMetadataStoreHandler;

import java.util.List;

/**
 * ClassificationExplorerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ClassificationExplorerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.CLASSIFICATION_EXPLORER;


    private final ViewServiceClientMap<StewardshipManagementHandler>   stewardshipManagementHandlerHashMap;
    private final ViewServiceClientMap<EgeriaOpenMetadataStoreHandler> openMetadataHandlerMap;


    /**
     * Set up the Classification Explorer OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public ClassificationExplorerInstance(String                  serverName,
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
                                                                 maxPageSize);

        this.stewardshipManagementHandlerHashMap = new ViewServiceClientMap<>(StewardshipManagementHandler.class,
                                                                              serverName,
                                                                              localServerUserId,
                                                                              localServerUserPassword,
                                                                              auditLog,
                                                                              activeViewServices,
                                                                              myDescription.getViewServiceFullName(),
                                                                              maxPageSize);
    }


    /**
     * Return the collaboration manager handler.
     *
     * @param viewServiceURLMarker optional view service URL marker
     * @param methodName calling method
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     * @return client
     */
    public StewardshipManagementHandler getStewardshipManagerHandler(String viewServiceURLMarker,
                                                                     String methodName) throws InvalidParameterException,
                                                                                               PropertyServerException
    {
        return stewardshipManagementHandlerHashMap.getClient(viewServiceURLMarker, methodName);
    }


    /**
     * Return the open metadata handler.
     *
     * @param viewServiceURLMarker  view service URL marker
     * @param methodName calling method
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     * @return client
     */
    public EgeriaOpenMetadataStoreHandler getOpenMetadataStoreHandler(String viewServiceURLMarker,
                                                                      String methodName) throws InvalidParameterException,
                                                                                                PropertyServerException
    {
        return openMetadataHandlerMap.getClient(viewServiceURLMarker, methodName);
    }
}
