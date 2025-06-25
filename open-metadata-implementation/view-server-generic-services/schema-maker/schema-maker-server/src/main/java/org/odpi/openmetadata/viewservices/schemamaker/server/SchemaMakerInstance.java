/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.schemamaker.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.viewservices.schemamaker.ffdc.SchemaMakerErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Map<String, SchemaTypeHandler>      schemaTypeHandlerMap      = new HashMap<>();
    private final Map<String, SchemaAttributeHandler> schemaAttributeHandlerMap = new HashMap<>();

    private final List<ViewServiceConfig> activeViewServices;



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
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.activeViewServices = activeViewServices;
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * schema type artifacts.
     *
     * @return client
     */
    public SchemaTypeHandler getSchemaTypeHandler(String viewServiceURLMarker,
                                                      String methodName) throws InvalidParameterException
    {
        SchemaTypeHandler schemaTypeHandler = null;

        if (viewServiceURLMarker != null)
        {
            schemaTypeHandler = schemaTypeHandlerMap.get(viewServiceURLMarker);

            if (schemaTypeHandler == null)
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
                                    if (localServerUserPassword != null)
                                    {
                                        schemaTypeHandler = new SchemaTypeHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.SCHEMA_MAKER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }
                                    else
                                    {
                                        schemaTypeHandler = new SchemaTypeHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      localServerUserId,
                                                                                      localServerUserPassword,
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.SCHEMA_MAKER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }

                                    schemaTypeHandlerMap.put(viewServiceURLMarker, schemaTypeHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (schemaTypeHandler == null)
        {
            throw new InvalidParameterException(SchemaMakerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return schemaTypeHandler;
    }




    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * schema attribute artifacts.
     *
     * @return client
     */
    public SchemaAttributeHandler getSchemaAttributeHandler(String viewServiceURLMarker,
                                                String methodName) throws InvalidParameterException
    {
        SchemaAttributeHandler schemaAttributeHandler = null;

        if (viewServiceURLMarker != null)
        {
            schemaAttributeHandler = schemaAttributeHandlerMap.get(viewServiceURLMarker);

            if (schemaAttributeHandler == null)
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
                                    if (localServerUserPassword != null)
                                    {
                                        schemaAttributeHandler = new SchemaAttributeHandler(serverName,
                                                                                            viewServiceConfig.getOMAGServerName(),
                                                                                            viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                            auditLog,
                                                                                            accessServiceDescription.getAccessServiceURLMarker(),
                                                                                            ViewServiceDescription.SCHEMA_MAKER.getViewServiceFullName(),
                                                                                            maxPageSize);
                                    }
                                    else
                                    {
                                        schemaAttributeHandler = new SchemaAttributeHandler(serverName,
                                                                                            viewServiceConfig.getOMAGServerName(),
                                                                                            viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                            localServerUserId,
                                                                                            localServerUserPassword,
                                                                                            auditLog,
                                                                                            accessServiceDescription.getAccessServiceURLMarker(),
                                                                                            ViewServiceDescription.SCHEMA_MAKER.getViewServiceFullName(),
                                                                                            maxPageSize);
                                    }

                                    schemaAttributeHandlerMap.put(viewServiceURLMarker, schemaAttributeHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (schemaAttributeHandler == null)
        {
            throw new InvalidParameterException(SchemaMakerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return schemaAttributeHandler;
    }
}
