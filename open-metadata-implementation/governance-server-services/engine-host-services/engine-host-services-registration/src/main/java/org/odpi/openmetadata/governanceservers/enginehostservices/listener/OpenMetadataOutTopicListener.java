/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.listener;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.api.WatchdogEventSupportingEngine;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;

import java.util.List;


/**
 * OpenMetadataOutTopicListener is a class that is registered to listen on the open metadata framework (OMF)'s
 * out topic to receive any changes to the governance engine's configuration, or new engine actions, or watchdog events.
 */
public class OpenMetadataOutTopicListener implements OpenMetadataEventListener
{
    private final EngineConfig        engineConfig;
    private final GovernanceEngineMap governanceEngineHandlers;
    private final AuditLog            auditLog;
    private final PropertyHelper      propertyHelper = new PropertyHelper();


    /**
     * Constructor for the listener.  Its job is to receive events and pass the information received on to the
     * appropriate governance engine handler.
     *
     * @param engineConfig the specific engine that this listener is focused on
     * @param governanceEngineHandlers these are the handlers for all the governance engines that are hosted by this
     *                                engine host server.
     * @param auditLog logging destination
     */
    public OpenMetadataOutTopicListener(EngineConfig        engineConfig,
                                        GovernanceEngineMap governanceEngineHandlers,
                                        AuditLog            auditLog)
    {
        this.engineConfig = engineConfig;
        this.governanceEngineHandlers = governanceEngineHandlers;
        this.auditLog = auditLog;
    }


    /**
     * Process an event that was published by the Open Metadata Services.
     * This method only needs to pass on the information to those engine hosted in this server.
     * There i
     * Events relating to other integration groups can be ignored.
     * So can events that are for capabilities not supported by these integration daemon services.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {

        if ((event != null) && event.getElementHeader() != null)
        {
            GovernanceEngineHandler governanceEngineHandler = governanceEngineHandlers.getGovernanceEngineHandler(engineConfig);

            if (governanceEngineHandler == null)
            {
                return;
            }

            /*
             * Check for engine host work
             */
            if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.GOVERNANCE_ENGINE.typeName))
            {
                final String actionDescription = "processGovernanceEngineEvent";

                try
                {
                    governanceEngineHandler.refreshConfig(event.getElementHeader().getGUID());
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(governanceEngineHandler.getGovernanceEngineName(),
                                                                                                                       error.getClass().getName(),
                                                                                                                       error.getMessage()),
                                          event.toString(),
                                          error);
                }
            }
            else if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.ENGINE_ACTION.typeName))
            {
                final String actionDescription = "processEngineActionEvent";

                try
                {
                    governanceEngineHandler.executeEngineAction(event.getElementHeader().getGUID());
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.ENGINE_ACTION_FAILED.getMessageDefinition(governanceEngineHandler.getGovernanceEngineName(),
                                                                                                                error.getClass().getName(),
                                                                                                                error.getMessage()),
                                          event.toString(),
                                          error);
                }
            }
            else if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName))
            {
                final String actionDescription = "processGovernanceServiceEvent";

                try
                {
                    governanceEngineHandler.refreshServiceConfig(event.getEndOneElementHeader().getGUID(),
                                                                 event.getEndTwoElementHeader().getGUID(),
                                                                 propertyHelper.getStringProperty(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                                                                  OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                                                                  event.getElementProperties(),
                                                                                                  actionDescription));
                }
                catch (Exception error)
                {
                    String eventProperties = "<null>";

                    if (event.getElementProperties() != null)
                    {
                        eventProperties = event.getElementProperties().toString();
                    }
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.GOVERNANCE_SERVICE_NO_CONFIG.getMessageDefinition(event.getEndOneElementHeader().getGUID(),
                                                                                                                        event.getEndTwoElementHeader().getGUID(),
                                                                                                                        eventProperties,
                                                                                                                        error.getClass().getName(),
                                                                                                                        error.getMessage()),
                                          event.toString(),
                                          error);
                }
            }

            /*
             * Pass on events to listening governance services
             */
            if (governanceEngineHandler instanceof WatchdogEventSupportingEngine watchdogEventSupportingEngine)
            {
                final String actionDescription = "processWatchdogEvent";

                try
                {
                    watchdogEventSupportingEngine.publishWatchdogEvent(event);
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceEngineHandler.getClass().getName(),
                                                                                                                engineConfig.getEngineQualifiedName(),
                                                                                                                error.getClass().getName(),
                                                                                                                actionDescription,
                                                                                                                error.getMessage()),
                                          event.toString(),
                                          error);
                }
            }
        }
    }
}
