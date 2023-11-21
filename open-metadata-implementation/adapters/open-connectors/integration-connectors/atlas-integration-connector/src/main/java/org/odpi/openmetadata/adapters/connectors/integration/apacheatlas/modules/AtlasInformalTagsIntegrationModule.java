/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules;


import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.List;

/**
 * AtlasInformalTagsIntegrationModule synchronizes InformalTags from the open metadata ecosystem to Apache Atlas.
 * It is called after the registered integration modules have established the key assets into the open metadata ecosystem.
 */
public class AtlasInformalTagsIntegrationModule extends AtlasIntegrationModuleBase
{
    /**
     * Unique name of this module for messages.
     */
    private static final String informalTagsModuleName = "Apache Atlas Informal Tags Integration Module";

    private final String informalTagsMappingPolicy;

    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionProperties connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @param informalTagsMappingPolicy determines what type of mapping to perform for informal tags
     * @throws UserNotAuthorizedException security problem
     */
    public AtlasInformalTagsIntegrationModule(String                   connectorName,
                                              ConnectionProperties     connectionProperties,
                                              AuditLog                 auditLog,
                                              CatalogIntegratorContext myContext,
                                              String                   targetRootURL,
                                              ApacheAtlasRESTConnector atlasClient,
                                              List<Connector>          embeddedConnectors,
                                              String                   informalTagsMappingPolicy) throws UserNotAuthorizedException
    {
        super(connectorName,
              informalTagsModuleName,
              connectionProperties,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors);

        this.informalTagsMappingPolicy = informalTagsMappingPolicy;
    }


    /**
     * Look for InformalTags attached to the open metadata ecosystem version of the Atlas entities and decide
     * how they should be represented in Apache Atlas.  Choice is as classifications, labels or entities.
     */
    public void synchronizeInformalTags()
    {

        // todo
    }
}