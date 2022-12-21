/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.client.*;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.engineservices.governanceaction.context.GovernanceListenerManager;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogGovernanceEvent;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceCache;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.*;

/**
 * The GovernanceActionEngineHandler is responsible for running governance action services on demand.  It is initialized
 * with the configuration for the governance action services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class GovernanceActionEngineHandler extends GovernanceEngineHandler
{
    private final GovernanceEngineClient      governanceEngineClient;    /* Initialized in constructor */
    private final GovernanceListenerManager   governanceListenerManager; /* Initialized in constructor */

    private final String partnerURLRoot;             /* Initialized in constructor */
    private final String partnerServerName;          /* Initialized in constructor */

    private static final String supportGovernanceEngineType = "GovernanceActionEngine";


    /**
     * Create a client-side object for calling a governance action engine.  Notice there are two instances of the
     * GovernanceEngineClient.  It is possible that they are pointing at different metadata server instances so do not
     * consolidate them into one client (even if IntelliJ begs you to :)).
     *
     * @param engineConfig the unique identifier of the governance action engine.
     * @param localServerName the name of the engine host server where the governance action engine is running
     * @param partnerServerName name of partner server
     * @param partnerURLRoot partner platform
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param serverClient client to control the execution of governance action requests
     * @param governanceEngineClient REST client for calls made by the governance action services
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public GovernanceActionEngineHandler(EngineConfig                        engineConfig,
                                         String                              localServerName,
                                         String                              partnerServerName,
                                         String                              partnerURLRoot,
                                         String                              serverUserId,
                                         GovernanceEngineConfigurationClient configurationClient,
                                         GovernanceEngineClient              serverClient,
                                         GovernanceEngineClient              governanceEngineClient,
                                         AuditLog                            auditLog,
                                         int                                 maxPageSize)
    {
        super(engineConfig,
              localServerName,
              serverUserId,
              EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceFullName(),
              configurationClient,
              serverClient,
              auditLog,
              maxPageSize);

        this.governanceEngineClient = governanceEngineClient;
        this.partnerServerName = partnerServerName;
        this.partnerURLRoot = partnerURLRoot;

        this.governanceListenerManager = new GovernanceListenerManager(auditLog, engineConfig.getEngineQualifiedName());
    }


    /**
     * Pass on the watchdog event to any governance service that supports them.
     *
     * @param watchdogGovernanceEvent element describing the changing metadata data.
     *
     * @throws InvalidParameterException Vital fields of the governance action are not filled out
     */
    public void publishWatchdogEvent(WatchdogGovernanceEvent watchdogGovernanceEvent) throws InvalidParameterException
    {
        governanceListenerManager.processEvent(watchdogGovernanceEvent);
    }


    /**
     * Run an instance of a governance action service in its own thread and return the handler (for disconnect processing).
     *
     * @param governanceActionGUID unique identifier of the asset to analyse
     * @param governanceRequestType governance request type to use when calling the governance engine
     * @param startDate date/time to start the governance action service
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     *
     * @return service handler for this request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there was a problem detected by the governance action engine.
     */
    @Override
    public GovernanceServiceHandler runGovernanceService(String                     governanceActionGUID,
                                                         String                     governanceRequestType,
                                                         Date                       startDate,
                                                         Map<String, String>        requestParameters,
                                                         List<RequestSourceElement> requestSourceElements,
                                                         List<ActionTargetElement>  actionTargetElements) throws InvalidParameterException,
                                                                                                                 PropertyServerException
    {
        final String methodName = "runGovernanceService";

        super.validateGovernanceEngineInitialized(supportGovernanceEngineType, methodName);

        GovernanceServiceCache governanceServiceCache = super.getServiceCache(governanceRequestType);

        if (governanceServiceCache != null)
        {
            /*
             * Need to combine the request parameters from the SupportedGovernanceService relationship with any from the caller.
             * The caller's request parameters take precedence.  This is done in the governanceServiceCache.
             */

            GovernanceActionServiceHandler governanceActionServiceHandler = new GovernanceActionServiceHandler(governanceEngineProperties,
                                                                                                               governanceEngineGUID,
                                                                                                               serverUserId,
                                                                                                               governanceActionGUID,
                                                                                                               serverClient,
                                                                                                               governanceServiceCache.getServiceRequestType(),
                                                                                                               governanceServiceCache.getRequestParameters(requestParameters),
                                                                                                               requestSourceElements,
                                                                                                               actionTargetElements,
                                                                                                               governanceServiceCache.getGovernanceServiceGUID(),
                                                                                                               governanceServiceCache.getGovernanceServiceName(),
                                                                                                               governanceServiceCache.getNextGovernanceService(),
                                                                                                               partnerServerName,
                                                                                                               partnerURLRoot,
                                                                                                               governanceEngineClient,
                                                                                                               governanceListenerManager,
                                                                                                               auditLog);

            Thread thread = new Thread(governanceActionServiceHandler, governanceServiceCache.getGovernanceServiceName() + governanceActionGUID + new Date());
            thread.start();

            return governanceActionServiceHandler;
        }

        return null;
    }
}
