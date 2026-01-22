/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;

import java.util.List;

/**
 * The IntegrationConnectorProvider provides a base class for the connector provider supporting
 * Integration Connectors.
 * It extends ConnectorProviderBase, which does the creation of connector instances.  The subclasses of
 * IntegrationConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class IntegrationConnectorProvider extends OpenConnectorProviderBase
{
    /**
     * How often should the integration connector be called to refresh the metadata?
     */
    private long    refreshTimeInterval = 60L; // default to once an hour

    /**
     * Does this connector issue blocking calls?
     */
    private boolean usesBlockingCalls   = false;

    /**
     * List of the supported catalog targets describing the types of entity that the connector works with.
     */
    protected List<CatalogTargetType> catalogTargets = null;

    /**
     * The type name of the asset that the connection object for this connector should be linked to.
     */
    protected static final String supportedAssetTypeName = OpenMetadataType.INTEGRATION_CONNECTOR.typeName;

    /*
     * Default descriptive information about the connector for the connector type and audit log.
     */
    private static final int    connectorComponentId = 31;
    private static final String connectorName        = "OIF:IntegrationConnector";
    private static final String connectorDescription = "Connector that manages metadata exchange with a third party technology.";
    private static final String connectorWikiPage    = "https://egeria-project.org/concepts/integration-connector/";


    /**
     * Constructor where subclass sets up the connector provider.
     */
    public IntegrationConnectorProvider()
    {
        super();
    }


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription connector definition
     * @param connectorClassName       connector class name
     * @param recognizedConfigurationPropertyNames list of recognized configuration property names
     */
    public IntegrationConnectorProvider(OpenConnectorDefinition openConnectorDescription,
                                        String                  connectorClassName,
                                        List<String>            recognizedConfigurationPropertyNames)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames);

        /*
         * Set up the default component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        if (super.connectorComponentDescription == null)
        {
            super.connectorComponentDescription = componentDescription;
        }
    }


    /**
     * Return the recommended minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start-up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @return minute count
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the recommended minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start-up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than zero, then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @param refreshTimeInterval minute count
     */
    protected void setRefreshTimeInterval(long refreshTimeInterval)
    {
        this.refreshTimeInterval = refreshTimeInterval;
    }


    /**
     * Return if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @return boolean flag
     */
    public boolean getUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @param usesBlockingCalls boolean flag
     */
    protected void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
    }


    /**
     * Return the list of supported catalog target types for this connector.
     *
     * @return list of catalog target name to open metadata type name.  List is empty if no catalog target types are defined.
     */
    public List<CatalogTargetType> getCatalogTargets() { return catalogTargets; }
}