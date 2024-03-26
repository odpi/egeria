/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.Arrays;
import java.util.List;

/**
 * AtlasRegisteredIntegrationModuleBase defines the common classes for integration modules that are working with assets from Apache Atlas.
 */
public abstract class AtlasRegisteredIntegrationModuleBase extends AtlasIntegrationModuleBase implements RegisteredIntegrationModule
{
    private final   List<String>             supportedEntityTypes;
    private final   List<String>             listenForTypes;



    /**
     * Initialize the common properties for an integration module.
     *
     * @param connectorName name of this connector
     * @param moduleName name of this module
     * @param connectionProperties supplied connector used to configure the connector
     * @param auditLog logging destination
     * @param myContext integration context assigned to the connector
     * @param targetRootURL host name and port of Apache Atlas
     * @param atlasClient client to call Apache Atlas
     * @param embeddedConnectors any  connectors embedded in the connector (such as the secrets connector or Kafka connector)
     * @param supportedEntityTypes list of entity types that this module is maintaining
     * @param listenForTypes list of types that the module wants to receive events on
     * @throws UserNotAuthorizedException the data asset service has not been enabled.
     */
    public AtlasRegisteredIntegrationModuleBase(String                   connectorName,
                                                String                   moduleName,
                                                ConnectionProperties     connectionProperties,
                                                AuditLog                 auditLog,
                                                CatalogIntegratorContext myContext,
                                                String                   targetRootURL,
                                                ApacheAtlasRESTConnector atlasClient,
                                                List<Connector>          embeddedConnectors,
                                                String[]                 supportedEntityTypes,
                                                String[]                 listenForTypes) throws UserNotAuthorizedException
    {
        super(connectorName, moduleName, connectionProperties, auditLog, myContext, targetRootURL, atlasClient, embeddedConnectors);

        if (supportedEntityTypes == null)
        {
            this.supportedEntityTypes = null;
        }
        else
        {
            this.supportedEntityTypes = Arrays.asList(supportedEntityTypes);
        }

        if (listenForTypes == null)
        {
            this.listenForTypes = null;
        }
        else
        {
            this.listenForTypes = Arrays.asList(listenForTypes);
        }
    }


    /**
     * Return the list of entity types that this module is maintaining.
     *
     * @return list of type names
     */
    @Override
    public List<String> getSupportedEntityTypes()
    {
        return supportedEntityTypes;
    }


    /**
     * Return the list of open metadata types that this module supports events for.
     *
     * @return list of types
     */
    public List<String> getListenForTypes()
    {
        return listenForTypes;
    }


    /**
     * Return the name of this module for messages.
     *
     * @return module name
     */
    public String getModuleName()
    {
        return moduleName;
    }
}
