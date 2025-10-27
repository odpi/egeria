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
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SearchKeywordHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StewardshipManagementHandler;

import java.util.List;

/**
 * ClassificationExplorerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ClassificationExplorerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.CLASSIFICATION_EXPLORER;

    private final ViewServiceClientMap<SearchKeywordHandler>         searchKeywordMap;

    private final ViewServiceClientMap<StewardshipManagementHandler>   stewardshipManagementHandlerHashMap;


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

        this.stewardshipManagementHandlerHashMap = new ViewServiceClientMap<>(StewardshipManagementHandler.class,
                                                                              serverName,
                                                                              localServerUserId,
                                                                              localServerUserPassword,
                                                                              auditLog,
                                                                              activeViewServices,
                                                                              myDescription.getViewServiceFullName(),
                                                                              myDescription.getViewServiceURLMarker(),
                                                                              maxPageSize);

        this.searchKeywordMap = new ViewServiceClientMap<>(SearchKeywordHandler.class,
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
     * @param urlMarker optional view service URL marker
     * @param methodName calling method
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     * @return client
     */
    public StewardshipManagementHandler getStewardshipManagerHandler(String urlMarker,
                                                                     String methodName) throws InvalidParameterException,
                                                                                               PropertyServerException
    {
        return stewardshipManagementHandlerHashMap.getClient(urlMarker, methodName);
    }



    /**
     * Return the open metadata handler.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public SearchKeywordHandler getSearchKeywordHandler(String urlMarker,
                                                        String methodName) throws InvalidParameterException,
                                                                                  PropertyServerException
    {
        return searchKeywordMap.getClient(urlMarker, methodName);
    }
}
