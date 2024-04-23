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
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;

import java.util.*;

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
    protected List<Connector>          embeddedConnectors          = null;

    private final PropertyHelper propertyHelper = new PropertyHelper();

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
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly, for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
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
     * Return a list of requested catalog targets for the connector.  These are extracted from the metadata store.
     *
     * @param catalogTargetIntegrator the integration component that will process each catalog target
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    protected void refreshCatalogTargets(CatalogTargetIntegrator catalogTargetIntegrator) throws ConnectorCheckedException
    {
        final String methodName = "refreshCatalogTargets";

        try
        {
            int startFrom = 0;

            int catalogTargetCount = 0;
            List<CatalogTarget> catalogTargetList = integrationContext.getCatalogTargets(startFrom,
                                                                                         integrationContext.getMaxPageSize());


            while (catalogTargetList != null)
            {
                for (CatalogTarget catalogTarget : catalogTargetList)
                {
                    boolean savedExternalSourceIsHome = integrationContext.getExternalSourceIsHome();
                    String  savedMetadataSourceQualifiedName = integrationContext.getMetadataSourceQualifiedName();

                    if ((catalogTarget != null) && (super.isActive()))
                    {
                        catalogTargetCount = catalogTargetCount + 1;

                        if (catalogTarget.getMetadataSourceQualifiedName() == null)
                        {
                            integrationContext.setExternalSourceIsHome(false);
                        }
                        else
                        {
                            integrationContext.setMetadataSourceQualifiedName(catalogTarget.getMetadataSourceQualifiedName());
                            integrationContext.setExternalSourceIsHome(true);
                        }

                        RequestedCatalogTarget requestedCatalogTarget = new RequestedCatalogTarget(catalogTarget);

                        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

                        if (catalogTarget.getConfigurationProperties() != null)
                        {
                            if (configurationProperties == null)
                            {
                                configurationProperties = new HashMap<>();
                            }

                            configurationProperties.putAll(catalogTarget.getConfigurationProperties());
                        }

                        requestedCatalogTarget.setConfigurationProperties(configurationProperties);

                        if (propertyHelper.isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.ASSET.typeName))
                        {
                            requestedCatalogTarget.setCatalogTargetConnector(integrationContext.getConnectedAssetContext().getConnectorToAsset(catalogTarget.getCatalogTargetElement().getGUID()));
                        }

                        auditLog.logMessage(methodName,
                                            OIFAuditCode.REFRESHING_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                                        requestedCatalogTarget.getCatalogTargetName()));
                        catalogTargetIntegrator.integrateCatalogTarget(requestedCatalogTarget);
                    }

                    integrationContext.setExternalSourceIsHome(savedExternalSourceIsHome);
                    integrationContext.setMetadataSourceQualifiedName(savedMetadataSourceQualifiedName);
                }

                startFrom = startFrom + integrationContext.getMaxPageSize();
                catalogTargetList = integrationContext.getCatalogTargets(startFrom, integrationContext.getMaxPageSize());

                if (catalogTargetCount == 0)
                {
                    auditLog.logMessage(methodName, OIFAuditCode.NO_CATALOG_TARGETS.getMessageDefinition(connectorName));
                }
                else
                {
                    auditLog.logMessage(methodName, OIFAuditCode.REFRESHED_CATALOG_TARGETS.getMessageDefinition(connectorName,
                                                                                                                Integer.toString(catalogTargetCount)));
                }
            }
        }
        catch (ConnectorCheckedException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  OIFAuditCode.GET_CATALOG_TARGET_EXCEPTION.getMessageDefinition(exception.getClass().getName(),
                                                                                                 connectorName,
                                                                                                 exception.getMessage()),
                                  exception);
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
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        super.disconnectConnectors(this.embeddedConnectors);
        super.disconnect();
    }
}
