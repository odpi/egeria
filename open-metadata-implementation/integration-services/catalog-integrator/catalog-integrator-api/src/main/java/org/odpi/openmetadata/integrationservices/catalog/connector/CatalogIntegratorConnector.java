/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.List;

/**
 * CatalogIntegratorConnector is the base class for an integration connector that is managed by the
 * Catalog Integrator OMIS.
 */
public abstract class CatalogIntegratorConnector extends IntegrationConnectorBase implements CatalogIntegratorOMISConnector
{
    private CatalogIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(CatalogIntegratorContext context)
    {
        super.setContext(context);
        this.context = context;
    }


    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @return context for this connector's private use.
     * @throws ConnectorCheckedException internal issue setting up context
     */
    public synchronized CatalogIntegratorContext getContext() throws ConnectorCheckedException
    {
        final String methodName = "getContext";

        if (context != null)
        {
            return this.context;
        }
        else
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName, CatalogIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(CatalogIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Return a list of requested catalog targets for the connector.  These are extracted from the metadata store.
     *
     * @param catalogTargetEventProcessor the integration component that will process each catalog target
     * @param event event to process
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    protected void passEventToCatalogTargets(CatalogTargetEventProcessor catalogTargetEventProcessor,
                                             AssetManagerOutTopicEvent   event) throws ConnectorCheckedException
    {
        final String methodName = "passEventToCatalogTargets";

        List<RequestedCatalogTarget> requestedCatalogTargets = catalogTargetsManager.refreshKnownCatalogTargets(integrationContext,
                                                                                                                catalogTargetEventProcessor);

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
                            catalogTargetEventProcessor.passEventToCatalogTarget(requestedCatalogTarget, event);
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
}
