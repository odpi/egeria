/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.lineagelinker.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.LineageHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaTypeHandler;

import java.util.List;

/**
 * LineageLinkerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class LineageLinkerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.LINEAGE_LINKER;

    /*
     * These maps cache clients for specific view services.
     */
    private final ViewServiceClientMap<LineageHandler> lineageHandlerMap;


    /**
     * Set up the Lineage Linker OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerUserPassword  password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public LineageLinkerInstance(String                  serverName,
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

        this.lineageHandlerMap = new ViewServiceClientMap<>(LineageHandler.class,
                                                            serverName,
                                                            localServerUserId,
                                                            localServerUserPassword,
                                                            auditLog,
                                                            activeViewServices,
                                                            myDescription.getViewServiceFullName(),
                                                            maxPageSize);
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
    public LineageHandler getLineageHandler(String urlMarker,
                                            String methodName) throws InvalidParameterException,
                                                                      PropertyServerException
    {
        return lineageHandlerMap.getClient(urlMarker, methodName);
    }
}
