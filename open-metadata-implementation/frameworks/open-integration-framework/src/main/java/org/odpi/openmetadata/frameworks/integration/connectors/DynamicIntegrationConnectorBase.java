/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.connectors;


import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;


/**
 * DynamicIntegrationConnectorBase supports the common methods for a connector that works with catalog targets.
 * The open metadata ecosystem is the home copy so its values will be pushed to the database. The database design matches the
 * beans returned by theOpen Metadata Store.
 */
public abstract class DynamicIntegrationConnectorBase extends IntegrationConnectorBase implements CatalogTargetFactory
{
    protected Date lastRefreshCompleteTime = null;


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        catalogTargetsManager.refreshCatalogTargets(integrationContext, this);

        /*
         * Once the connector has completed a single refresh, it registered a listener with open metadata
         * to handle updates.  The delay in registering the listener is for efficiency-sake in that it
         * reduces the number of events coming in from updates to the open metadata ecosystem when the connector
         * is performing its first synchronization to the database.
         *
         * A listener is registered only if the connector implementation implements OpenMetadataEventListener
         * indicating that it is interested in open metadata events and the metadata is synchronizing
         * to the third party.
         */
        if (this instanceof OpenMetadataEventListener openMetadataEventListener)
        {
            if ((integrationContext.noListenerRegistered()) &&
                    (integrationContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
                    (integrationContext.getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
            {
                try
                {
                    /*
                     * This request registers this connector to receive events from the open metadata ecosystem.  When an event occurs,
                     * the processEvent() method is called.
                     */
                    integrationContext.registerListener(openMetadataEventListener);
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              OIFAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            error.getMessage()),
                                              error);
                    }

                    throw new ConnectorCheckedException(OIFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                          error.getClass().getName(),
                                                                                                                          methodName,
                                                                                                                          error.getMessage()),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        error);
                }
            }
        }


        lastRefreshCompleteTime = new Date();
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException there is a problem with setting up the catalog target.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public abstract RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                                CatalogTargetContext catalogTargetContext,
                                                                                Connector            connectorToTarget) throws ConnectorCheckedException,
                                                                                                                               UserNotAuthorizedException;


    /**
     * Process an event that was published by the OMF Services.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        /*
         * Only process events if refresh() is not running because the refresh() process creates lots of events
         * and proceeding with event processing at this time causes elements to be processed multiple times.
         */
        if (integrationContext.noRefreshInProgress())
        {
            try
            {
                catalogTargetsManager.passEventToCatalogTargets(integrationContext, event);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                             error.getClass().getName(),
                                                                                             methodName,
                                                                                             error.getMessage()),
                                      error);
            }
        }
    }
}
