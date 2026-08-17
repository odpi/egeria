/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.opengovernance.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.opengovernance.client.WatchdogEventInterface;

import java.util.List;

/**
 * GovernanceContextClientBase sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is, however, shared by all the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public class GovernanceContextClientBase extends OpenGovernanceClientBase implements WatchdogEventInterface
{
    /**
     * Manages registered listeners
     */
    protected  GovernanceListenerManager governanceListenerManager = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize           pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public GovernanceContextClientBase(String   serverName,
                                       String   serverPlatformURLRoot,
                                       String   localServerSecretsStoreProvider,
                                       String   localServerSecretsStoreLocation,
                                       String   localServerSecretsStoreCollection,
                                       int      maxPageSize,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, maxPageSize, auditLog);
    }


    /**
     * Set up the listener manager.  Called once for each governance engine handler.
     *
     * @param governanceListenerManager aggregates listeners from governance services
     */
    public void setListenerManager(GovernanceListenerManager governanceListenerManager)
    {
        this.governanceListenerManager = governanceListenerManager;
    }


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
     * @param listenerId             identifier used to maintain topic event pointer in event manager
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    @Override
    public  void registerListener(String                      listenerId,
                                  WatchdogGovernanceListener  listener,
                                  List<OpenMetadataEventType> interestingEventTypes,
                                  List<String>                interestingMetadataTypes,
                                  String                      specificInstance) throws InvalidParameterException
    {
        governanceListenerManager.registerListener(listenerId, listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }


    /**
     * Called during the disconnect processing of the watchdog governance action service.
     *
     * @param listenerId             identifier used to maintain topic event pointer in event manager
     */
    @Override
    public void disconnectListener(String listenerId)
    {
        governanceListenerManager.removeListener(listenerId);
    }
}
