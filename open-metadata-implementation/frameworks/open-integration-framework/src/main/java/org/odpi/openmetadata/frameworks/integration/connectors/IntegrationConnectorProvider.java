/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The IntegrationConnectorProvider provides a base class for the connector provider supporting
 * Integration Connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * IntegrationConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class IntegrationConnectorProvider extends ConnectorProviderBase
{
    private long    refreshTimeInterval = 60L; // default to once an hour
    private boolean usesBlockingCalls   = false;

    /*
     * The type name of the asset that this connector supports.
     */
    protected static final String supportedAssetTypeName = "IntegrationConnector";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final int    connectorComponentId   = 31;
    private static final String connectorName        = "OIF:IntegrationConnector";
    private static final String connectorDescription = "Connector that manages metadata exchange with a third party technology.";
    private static final String connectorWikiPage    = "https://egeria-project.org/concepts/integration-connector/";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public IntegrationConnectorProvider()
    {
        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }


    /**
     * Return the recommended number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @return minute count
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the recommended number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @param refreshTimeInterval minute count
     */
    protected void setRefreshTimeInterval(long refreshTimeInterval)
    {
        this.refreshTimeInterval = refreshTimeInterval;
    }


    /**
     * Return if the connector should be started in its own thread to allow it is block on a listening call.
     *
     * @return boolean flag
     */
    public boolean getUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up if the connector should be started in its own thread to allow it is block on a listening call.
     *
     * @param usesBlockingCalls boolean flag
     */
    protected void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
    }
}