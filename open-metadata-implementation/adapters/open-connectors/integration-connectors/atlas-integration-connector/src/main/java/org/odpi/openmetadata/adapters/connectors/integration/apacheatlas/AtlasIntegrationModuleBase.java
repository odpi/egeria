/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas;

import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AtlasIntegrationModuleBase defines the interface that classes that support the synchronization of particular types of metadata with Apache Atlas.
 */
public abstract class AtlasIntegrationModuleBase
{
    protected final static String egeriaGUIDPropertyName  = "egeriaGUID";
    protected final static String egeriaOwnedPropertyName = "egeriaOwned";

    protected final AuditLog              auditLog;
    protected final String                connectorName;
    protected final ConnectionProperties  connectionProperties;
    protected final CatalogIntegratorContext    myContext;
    protected final List<Connector>       embeddedConnectors;
    protected final   ApacheAtlasRESTClient atlasClient;
    private final   List<String>          supportedTypes;

    protected final String targetRootURL;

    public AtlasIntegrationModuleBase(String                   connectorName,
                                      ConnectionProperties     connectionProperties,
                                      AuditLog                 auditLog,
                                      CatalogIntegratorContext myContext,
                                      String                   targetRootURL,
                                      ApacheAtlasRESTClient    atlasClient,
                                      List<Connector>          embeddedConnectors,
                                      String[]                 supportedTypes)
    {
        this.auditLog = auditLog;
        this.connectorName = connectorName;
        this.connectionProperties = connectionProperties;
        this.myContext = myContext;
        this.targetRootURL = targetRootURL;
        this.atlasClient = atlasClient;
        this.embeddedConnectors = embeddedConnectors;
        this.supportedTypes = Arrays.asList(supportedTypes);
    }


    /**
     * Return the list of open metadata types that this module supports.
     *
     * @return list of types
     */
    public List<String> getSupportedTypes()
    {
        return supportedTypes;
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    public abstract void refresh() throws ConnectorCheckedException;


    /**
     * Process an event that was published by the Asset Manager OMAS.  The listener is only registered if metadata is flowing
     * from the open metadata ecosystem to Apache Atlas.
     *
     * @param event event object
     */
    public abstract void processEvent(AssetManagerOutTopicEvent event);
}
