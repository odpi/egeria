/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.context;

import org.odpi.openmetadata.accessservices.governanceengine.api.GovernanceEngineEventListener;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineEventClient;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceEngineEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GovernanceListenerManager manages the watchdog listeners through a single registration to the Governance Engine OMAS's
 * out topic.  This approach is used to enable the rapid changing list of watchdog listeners and their listening specification
 * without pushing that churn to the event bus.
 */
public class GovernanceListenerManager extends GovernanceEngineEventListener
{
    private GovernanceEngineEventClient eventClient;

    private volatile Map<String, WatchdogListener> listenerMap = new HashMap<>();


    /**
     * Create a governance listener manager for watchdog listeners.
     *
     * @param eventClient access to the out topic
     */
    public GovernanceListenerManager(GovernanceEngineEventClient eventClient)
    {
        this.eventClient = eventClient;
    }


    /**
     * Process an event that was published by the Governance Engine OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public synchronized void processEvent(GovernanceEngineEvent event)
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
                        watchdogListener.processEvent(event);
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
        private WatchdogGovernanceListener listener = null;
        private List<WatchdogEventType>    interestingEventTypes = null;
        private List<String>               interestingMetadataType = null;
        private String                     specificInstance = null;

        public WatchdogListener()
        {
        }


        public void setListenerSpec(WatchdogGovernanceListener listener,
                                    List<WatchdogEventType>    interestingEventTypes,
                                    List<String>               interestingMetadataType,
                                    String                     specificInstance)
        {
            this.listener = listener;
            this.interestingEventTypes = interestingEventTypes;
            this.interestingMetadataType = interestingMetadataType;
            this.specificInstance = specificInstance;
        }


        /**
         * Process an event that was published by the Governance Engine OMAS for a specific listener.
         *
         * @param event event object - call getEventType to find out what type of event.
         */
        public void processEvent(GovernanceEngineEvent event)
        {
            if (listener != null)
            {
                // todo
            }
        }
    }


    /**
     * Ensure the topic connector is disconnected
     */
    public void shutdown()
    {
        // todo
    }
}
