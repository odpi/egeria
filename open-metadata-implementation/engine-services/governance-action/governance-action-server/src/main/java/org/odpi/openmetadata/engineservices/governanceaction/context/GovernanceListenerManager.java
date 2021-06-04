/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.context;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.governanceaction.events.*;

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
    private volatile Map<String, WatchdogListener> listenerMap = new HashMap<>();

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private AuditLog auditLog;
    private String   governanceEngineName;

    /**
     * Create a governance listener manager for watchdog listeners.
     *
     * @param auditLog audit log for the listener manager
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
    public synchronized void processEvent(WatchdogGovernanceEvent event) throws InvalidParameterException
    {
        if ((listenerMap != null) && (event != null))
        {
            for (String connectorId : listenerMap.keySet())
            {
                if (connectorId != null)
                {
                    WatchdogListener watchdogListener = listenerMap.get(connectorId);

                    if (watchdogListener != null)
                    {
                        watchdogListener.processWatchdogEvent(event);
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
     *
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     *
     * If specific instance, interestingEventTypes or interestingMetadataTypes are null, it defaults to "any".
     * If the listener parameter is null, no more events are passed to the listener.
     *
     * @param connectorId unique identifier of this instance of the connector - used on each call.
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types.
     * @param specificInstance unique identifier of a specific instance to watch for
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    synchronized void registerListener(String                     connectorId,
                                       WatchdogGovernanceListener listener,
                                       List<WatchdogEventType>    interestingEventTypes,
                                       List<String>               interestingMetadataTypes,
                                       String                     specificInstance) throws InvalidParameterException
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
        private WatchdogGovernanceListener listener                 = null;
        private List<WatchdogEventType>    interestingEventTypes    = null;
        private List<String>               interestingMetadataTypes = null;
        private String                     specificInstance         = null;

        WatchdogListener()
        {
        }


        void setListenerSpec(WatchdogGovernanceListener listener,
                             List<WatchdogEventType>    interestingEventTypes,
                             List<String>               interestingMetadataType,
                             String                     specificInstance)
        {
            this.listener = listener;
            this.interestingEventTypes = interestingEventTypes;
            this.interestingMetadataTypes = interestingMetadataType;
            this.specificInstance = specificInstance;
        }


        /**
         * Process a the watchdog event that was published by the Governance Engine OMAS for a specific listener.
         *
         * @param event event object - call getEventType to find out what type of event.
         * @throws InvalidParameterException the event is incomplete
         */
        void processWatchdogEvent(WatchdogGovernanceEvent event) throws InvalidParameterException
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
                    /*
                     * The event type is of interest, cast the event to its specific type and call the registered listener if
                     * the event is interesting.
                     */
                    if (event instanceof WatchdogClassificationEvent)
                    {
                        final String elementParameterName = "watchdogClassificationEvent.getMetadataElement()";
                        final String classificationParameterName = "watchdogClassificationEvent.getChangedClassification()";
                        final String elementGUIDParameterName = "watchdogClassificationEvent.getMetadataElement().getElementGUID()";
                        final String typeNameParameterName    = "watchdogClassificationEvent.getChangedClassification().getClassificationName()";

                        WatchdogClassificationEvent watchdogClassificationEvent = (WatchdogClassificationEvent)event;

                        invalidParameterHandler.validateObject(watchdogClassificationEvent.getMetadataElement(), elementParameterName, methodName);
                        invalidParameterHandler.validateObject(watchdogClassificationEvent.getChangedClassification(), classificationParameterName, methodName);
                        invalidParameterHandler.validateGUID(watchdogClassificationEvent.getMetadataElement().getElementGUID(), elementGUIDParameterName, methodName);
                        invalidParameterHandler.validateName(watchdogClassificationEvent.getChangedClassification().getClassificationName(), typeNameParameterName, methodName);

                        if (this.elementIsInteresting(watchdogClassificationEvent.getMetadataElement().getElementGUID(),
                                                      watchdogClassificationEvent.getChangedClassification().getClassificationName(),
                                                      null))
                        {
                            this.callListener(event);
                        }
                    }
                    else if (event instanceof WatchdogMetadataElementEvent)
                    {
                        final String elementParameterName     = "watchdogMetadataElementEvent.getMetadataElement()";
                        final String elementTypeParameterName = "watchdogMetadataElementEvent.getElementType()";
                        final String elementGUIDParameterName = "watchdogMetadataElementEvent.getMetadataElement().getElementGUID()";
                        final String typeNameParameterName    = "watchdogMetadataElementEvent.getMetadataElement().getElementType().getElementTypeName()";

                        WatchdogMetadataElementEvent watchdogMetadataElementEvent = (WatchdogMetadataElementEvent)event;

                        invalidParameterHandler.validateObject(watchdogMetadataElementEvent.getMetadataElement(), elementParameterName, methodName);
                        invalidParameterHandler.validateObject(watchdogMetadataElementEvent.getMetadataElement().getElementType(), elementTypeParameterName, methodName);
                        invalidParameterHandler.validateGUID(watchdogMetadataElementEvent.getMetadataElement().getElementGUID(), elementGUIDParameterName, methodName);
                        invalidParameterHandler.validateName(watchdogMetadataElementEvent.getMetadataElement().getElementType().getElementTypeName(), typeNameParameterName, methodName);

                        if (this.elementIsInteresting(watchdogMetadataElementEvent.getMetadataElement().getElementGUID(),
                                                      watchdogMetadataElementEvent.getMetadataElement().getElementType().getElementTypeName(),
                                                      watchdogMetadataElementEvent.getMetadataElement().getElementType().getElementSuperTypeNames()))
                        {
                            this.callListener(event);
                        }
                    }
                    else if (event instanceof WatchdogRelatedElementsEvent)
                    {
                        WatchdogRelatedElementsEvent relatedElementsEvent = (WatchdogRelatedElementsEvent)event;

                        if (this.elementIsInteresting(relatedElementsEvent.getRelatedMetadataElements().getRelationshipGUID(),
                                                      relatedElementsEvent.getRelatedMetadataElements().getRelationshipType().getElementTypeName(),
                                                      relatedElementsEvent.getRelatedMetadataElements().getRelationshipType().getElementSuperTypeNames()))
                        {
                            this.callListener(event);
                        }
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
        private void callListener(WatchdogGovernanceEvent event)
        {
            try
            {
                listener.processEvent(event);
            }
            catch (Exception error)
            {
                final String actionDescription = "publish watchdog event to listener";

                auditLog.logMessage(actionDescription,
                                    GovernanceActionAuditCode.WATCHDOG_LISTENER_EXCEPTION.getMessageDefinition(governanceEngineName,
                                                                                                               error.getClass().getName(),
                                                                                                               error.getMessage()),
                                    event.toString());
            }
        }
    }
}
