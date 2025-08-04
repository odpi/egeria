/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenGovernanceAuditCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GovernanceListenerManager manages the watchdog listeners through a single registration to the Governance Engine OMAS's
 * out topic.  This approach is used to enable the rapid changing list of watchdog listeners and their listening specification
 * without pushing that churn to the event bus.
 */
public class GovernanceListenerManager
{
    private final Map<String, WatchdogListener> listenerMap = new HashMap<>();

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private final AuditLog auditLog;
    private final String   governanceEngineName;

    /**
     * Create a governance listener manager for watchdog listeners.
     *
     * @param auditLog audit log for the listener manager
     * @param governanceEngineName engine name for messages
     */
    public GovernanceListenerManager(AuditLog auditLog,
                                     String   governanceEngineName)
    {
        this.auditLog = auditLog;
        this.governanceEngineName = governanceEngineName;
    }


    /**
     * Process a watchdog event that was published by the Governance Engine OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     * @throws InvalidParameterException the event is incomplete
     */
    public synchronized void processEvent(OpenMetadataOutTopicEvent event) throws InvalidParameterException
    {
        final String actionDescription = "Process Watchdog Event";

        if (event != null)
        {
            for (String connectorId : listenerMap.keySet())
            {
                if (connectorId != null)
                {
                    WatchdogListener watchdogListener = listenerMap.get(connectorId);

                    if (watchdogListener != null)
                    {
                        try
                        {
                            watchdogListener.processWatchdogEvent(event);
                        }
                        catch (Exception error)
                        {
                            auditLog.logException(actionDescription,
                                                  OpenGovernanceAuditCode.WATCHDOG_EVENT_FAILURE.getMessageDefinition(
                                                          error.getClass().getName(),
                                                          connectorId,
                                                          event.getEventType().toString(),
                                                          error.getMessage()),
                                                  event.toString(),
                                                  error);
                        }
                    }
                }
            }
        }
    }


    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata store.
     * There can be only one registered listener.  If this method is called more than once, the new parameters
     * replace the existing parameters.  This means the watchdog governance action service can change the
     * listener and the parameters that control the types of events received while it is running.
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     * If specific instance, interestingEventTypes or interestingMetadataTypes are null, it defaults to "any".
     * If the listener parameter is null, no more events are passed to the listener.
     *
     * @param connectorId unique identifier of this instance of the connector - used on each call.
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types.
     * @param specificInstance unique identifier of a specific instance to watch for
     */
    synchronized void registerListener(String                      connectorId,
                                       WatchdogGovernanceListener  listener,
                                       List<OpenMetadataEventType> interestingEventTypes,
                                       List<String>                interestingMetadataTypes,
                                       String                      specificInstance)
    {
        WatchdogListener watchdogListener = listenerMap.get(connectorId);

        if (watchdogListener == null)
        {
            watchdogListener = new WatchdogListener();
        }

        watchdogListener.setListenerSpec(listener, interestingEventTypes, interestingMetadataTypes, specificInstance);

        listenerMap.put(connectorId, watchdogListener);
    }


    /**
     * Remove the listener for a specific governance service because this service has been disconnected.
     *
     * @param connectorId unique id of governance service instance
     */
    public synchronized void removeListener(String connectorId)
    {
        listenerMap.remove(connectorId);
    }


    /**
     * Inner class to manage a specific listener
     */
    private class WatchdogListener
    {
        private WatchdogGovernanceListener  listener                 = null;
        private List<OpenMetadataEventType> interestingEventTypes    = null;
        private List<String>                interestingMetadataTypes = null;
        private String                      specificInstance         = null;

        WatchdogListener()
        {
        }


        void setListenerSpec(WatchdogGovernanceListener  listener,
                             List<OpenMetadataEventType> interestingEventTypes,
                             List<String>                interestingMetadataType,
                             String                      specificInstance)
        {
            this.listener = listener;
            this.interestingEventTypes = interestingEventTypes;
            this.interestingMetadataTypes = interestingMetadataType;
            this.specificInstance = specificInstance;
        }


        /**
         * Process the watchdog event that was published by the Governance Engine OMAS for a specific listener.
         *
         * @param event event object - call getEventType to find out what type of event.
         * @throws InvalidParameterException the event is incomplete
         */
        void processWatchdogEvent(OpenMetadataOutTopicEvent event) throws InvalidParameterException
        {
            final String methodName             = "WatchdogListener.processWatchdogEvent";
            final String eventParameterName     = "event";
            final String eventTypeParameterName = "eventType";

            invalidParameterHandler.validateObject(event, eventParameterName, methodName);
            invalidParameterHandler.validateEnum(event.getEventType(), eventTypeParameterName, methodName);

            if (listener != null)
            {
                if ((interestingEventTypes == null) || (interestingEventTypes.contains(event.getEventType())))
                {
                    if (this.elementIsInteresting(event.getElementHeader().getGUID(),
                                                  event.getElementHeader().getType().getTypeName(),
                                                  event.getElementHeader().getType().getSuperTypeNames()))
                    {
                        this.callListener(event);
                    }
                }
            }
        }


        /**
         * Determine if the element(s) passed in the event is interesting to this listener.
         *
         * @param elementGUID unique identifier of principle metadata element
         * @param elementTypeName type name of event subject
         * @param elementSuperTypeNames super type names of event subject (or null)
         * @return boolean flag
         */
        private boolean elementIsInteresting(String       elementGUID,
                                             String       elementTypeName,
                                             List<String> elementSuperTypeNames)
        {
            if ((specificInstance != null) && (specificInstance.equals(elementGUID)))
            {
                return true;
            }
            else if ((interestingMetadataTypes == null) || (interestingMetadataTypes.contains(elementTypeName)))
            {
                return true;
            }
            else if ((elementSuperTypeNames != null) && (! elementSuperTypeNames.isEmpty()))
            {
                for (String typeName : elementSuperTypeNames)
                {
                    if (typeName != null)
                    {
                        if (interestingMetadataTypes.contains(typeName))
                        {
                            return true;
                        }
                    }
                }
            }

            return false;
        }


        /**
         * The event is interesting, call the listener.  Exceptions are logged.
         *
         * @param event event to publish to the watchdog listener
         */
        private void callListener(OpenMetadataOutTopicEvent event)
        {
            try
            {
                listener.processEvent(event);
            }
            catch (Exception error)
            {
                final String actionDescription = "publish watchdog event to listener";

                auditLog.logMessage(actionDescription,
                                    OpenGovernanceAuditCode.WATCHDOG_LISTENER_EXCEPTION.getMessageDefinition(governanceEngineName,
                                                                                                             error.getClass().getName(),
                                                                                                             error.getMessage()),
                                    event.toString());
            }
        }
    }
}
