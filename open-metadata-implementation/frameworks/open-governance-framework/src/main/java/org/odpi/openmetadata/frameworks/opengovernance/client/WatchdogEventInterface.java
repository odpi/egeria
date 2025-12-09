/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.client;

import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.opengovernance.WatchdogGovernanceListener;

import java.util.List;

/**
 * WatchDogEventInterface defines the interface for receiving watchdog events.
 */
public interface WatchdogEventInterface
{
    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata store.
     * There can be only one registered listener.  If this method is called more than once, the new parameters
     * replace the existing parameters.  This means the watchdog governance action service can change the
     * listener and the parameters that control the types of events received while it is running.
     * <br><br>
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     * <br><br>
     * If specific instance, interestingEventTypes or interestingMetadataTypes are null, it defaults to "any".
     * If the listener parameter is null, no more events are passed to the listener.
     *
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types
     * @param specificInstance unique identifier of a specific instance to watch for
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    void registerListener(WatchdogGovernanceListener  listener,
                          List<OpenMetadataEventType> interestingEventTypes,
                          List<String>                interestingMetadataTypes,
                          String                      specificInstance) throws InvalidParameterException;


    /**
     * Unregister the listener from the event infrastructure.
     */
    void disconnectListener();
}
