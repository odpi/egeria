/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;

import java.util.List;

/**
 * IntegrationConnectorBase is the base class for an integration connector.  It manages the storing of the audit log for the connector
 * and provides a default implementation for the abstract engage() method.  This method only needs to be overridden by connectors
 * that need to make blocking calls to wait for new metadata.
 */
public abstract class IntegrationConnectorBase extends ConnectorBase implements IntegrationConnector,
                                                                                AuditLoggingComponent,
                                                                                VirtualConnectorExtension
{
    protected AuditLog                 auditLog                    = null;
    protected String                   connectorName               = null;
    protected IntegrationContext       integrationContext          = null;

    protected final PropertyHelper propertyHelper = new PropertyHelper();

    protected RequestedCatalogTargetsManager catalogTargetsManager = null;


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up the connector name for logging.
     *
     * @param connectorName connector name from the configuration
     */
    @Override
    public void setConnectorName(String connectorName)
    {
        this.connectorName = connectorName;
    }


    /**
     * Set up the standard integration context for the connector.
     *
     * @param integrationContext integration context.
     */
    public void setContext(IntegrationContext integrationContext)
    {
        this.integrationContext = integrationContext;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();

        catalogTargetsManager = new RequestedCatalogTargetsManager(connectionProperties.getConfigurationProperties(),
                                                                   connectorName,
                                                                   auditLog);
    }


    /**
     * Retrieve the endpoint from the asset connection.
     *
     * @param assetConnector asset connector
     * @return endpoint or null
     */
    protected String getNetworkAddress(Connector assetConnector)
    {
        ConnectionProperties assetConnection = assetConnector.getConnection();

        if (assetConnection != null)
        {
            EndpointProperties endpointProperties = assetConnection.getEndpoint();

            if (endpointProperties != null)
            {
                return endpointProperties.getAddress();
            }
        }

        return null;
    }


    /**
     * Add a new listener for changes to this connector's catalog targets.
     *
     * @param listener listener to register
     */
    protected void registerCatalogTargetChangeListener(CatalogTargetChangeListener listener)
    {
        this.catalogTargetsManager.registerCatalogTargetChangeListener(listener);
    }


    /**
     * Return a list of requested catalog targets for the connector.  These are extracted from the metadata store.
     *
     * @param catalogTargetIntegrator the integration component that will process each catalog target
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    protected void refreshCatalogTargets(CatalogTargetIntegrator catalogTargetIntegrator) throws ConnectorCheckedException
    {
        final String methodName = "refreshCatalogTargets";

        List<RequestedCatalogTarget> requestedCatalogTargets = catalogTargetsManager.refreshKnownCatalogTargets(integrationContext,
                                                                                                                catalogTargetIntegrator);

        if ((requestedCatalogTargets == null) || (requestedCatalogTargets.isEmpty()))
        {
            auditLog.logMessage(methodName, OIFAuditCode.NO_CATALOG_TARGETS.getMessageDefinition(connectorName));
        }
        else
        {
            try
            {
                for (RequestedCatalogTarget requestedCatalogTarget : requestedCatalogTargets)
                {
                    boolean savedExternalSourceIsHome        = integrationContext.getExternalSourceIsHome();
                    String  savedMetadataSourceQualifiedName = integrationContext.getMetadataSourceQualifiedName();

                    if ((requestedCatalogTarget != null) && (super.isActive()))
                    {
                        try
                        {
                            if (requestedCatalogTarget.getMetadataSourceQualifiedName() == null)
                            {
                                integrationContext.setExternalSourceIsHome(false);
                            }
                            else
                            {
                                integrationContext.setMetadataSourceQualifiedName(requestedCatalogTarget.getMetadataSourceQualifiedName());
                                integrationContext.setExternalSourceIsHome(true);
                            }

                            auditLog.logMessage(methodName,
                                                OIFAuditCode.REFRESHING_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                                            requestedCatalogTarget.getCatalogTargetName()));
                            catalogTargetIntegrator.integrateCatalogTarget(requestedCatalogTarget);
                        }
                        catch (Exception error)
                        {
                            auditLog.logMessage(methodName,
                                                OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName + ":" + requestedCatalogTarget.getCatalogTargetName(),
                                                                                                       error.getMessage()));
                        }
                    }

                    integrationContext.setExternalSourceIsHome(savedExternalSourceIsHome);
                    integrationContext.setMetadataSourceQualifiedName(savedMetadataSourceQualifiedName);
                }
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                           error.getClass().getName(),
                                                                                           methodName,
                                                                                           error.getMessage()));
            }

            auditLog.logMessage(methodName, OIFAuditCode.REFRESHED_CATALOG_TARGETS.getMessageDefinition(connectorName,
                                                                                                        Integer.toString(requestedCatalogTargets.size())));
        }
    }


    /**
     * This method is for blocking calls to wait for new metadata.  It is called from its own thread iff
     * the connector is configured to have its own thread.  It is recommended that the implementation
     * returns when each blocking call completes.  The integration daemon will pause a second and then
     * call engage() again.  This pattern enables the calling thread to detect the shutdown of the integration
     * daemon.
     * This method should be overridden if the connector needs to issue calls that wait for new metadata.
     * If this specific implementation is called a message is logged in the audit log because there is probably
     * a mismatch between the configuration and the connector implementation.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void engage() throws ConnectorCheckedException
    {
        final String actionDescription = "Calling default engage() method";

        throw new ConnectorCheckedException(OIFErrorCode.ENGAGE_IMPLEMENTATION_MISSING.getMessageDefinition(connectorName),
                                            this.getClass().getName(),
                                            actionDescription);

    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        if (catalogTargetsManager != null)
        {
            catalogTargetsManager.disconnect();
        }

        super.disconnect();
    }
}
