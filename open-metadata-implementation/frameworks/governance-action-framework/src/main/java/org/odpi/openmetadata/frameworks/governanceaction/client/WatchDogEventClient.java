/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenGovernanceClient provides services to support the governance context.  It is implemented by a
 * metadata repository provider. In Egeria, this class is implemented partly by the GAF metadata management
 * services and partly by the Governance Action OMES running in the engine host OMAG Server.
 */
public abstract class WatchDogEventClient implements GovernanceCompletionInterface,
                                                     SpecialGovernanceActionInterface
{
    protected final String serverName;               /* Initialized in constructor */
    protected final String serviceURLMarker;         /* Initialized in constructor */
    protected final String serverPlatformURLRoot;    /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     */
    public WatchDogEventClient(String serviceURLMarker,
                               String serverName,
                               String serverPlatformURLRoot)
    {
        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
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
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types
     * @param specificInstance unique identifier of a specific instance to watch for
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    public abstract void registerListener(WatchdogGovernanceListener listener,
                                          List<WatchdogEventType>    interestingEventTypes,
                                          List<String>               interestingMetadataTypes,
                                          String                     specificInstance) throws InvalidParameterException;


    /**
     * Unregister the listener from the event infrastructure.
     */
    public abstract void disconnectListener();


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataClient{" +
                       "serverPlatformURLRoot='" + serverPlatformURLRoot + '\'' +
                       ", serverName='" + serverName + '\'' +
                       '}';
    }
}
