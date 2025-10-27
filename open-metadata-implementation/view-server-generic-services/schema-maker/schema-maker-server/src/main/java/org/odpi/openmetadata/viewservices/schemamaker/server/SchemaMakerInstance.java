/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.schemamaker.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaTypeHandler;

import java.util.List;

/**
 * SchemaMakerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SchemaMakerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.SCHEMA_MAKER;

    /*
     * These maps cache clients for specific view services/access services.
     */
    private final ViewServiceClientMap<SchemaTypeHandler>      schemaTypeHandlerMap;
    private final ViewServiceClientMap<SchemaAttributeHandler> schemaAttributeHandlerMap;


    /**
     * Set up the Schema Maker OMVS instance
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
    public SchemaMakerInstance(String                  serverName,
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

        this.schemaTypeHandlerMap = new ViewServiceClientMap<>(SchemaTypeHandler.class,
                                                               serverName,
                                                               localServerUserId,
                                                               localServerUserPassword,
                                                               auditLog,
                                                               activeViewServices,
                                                               myDescription.getViewServiceFullName(),
                                                               myDescription.getViewServiceURLMarker(),
                                                               maxPageSize);

        this.schemaAttributeHandlerMap = new ViewServiceClientMap<>(SchemaAttributeHandler.class,
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
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * schema type artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public SchemaTypeHandler getSchemaTypeHandler(String urlMarker,
                                                  String methodName) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        return schemaTypeHandlerMap.getClient(urlMarker, methodName);
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
