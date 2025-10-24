/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceListenerManager;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RequestSourceElement;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceCache;

import java.util.*;

/**
 * The GovernanceActionEngineHandler is responsible for running governance action services on demand.  It is initialized
 * with the configuration for the governance action services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class GovernanceActionEngineHandler extends GovernanceEngineHandler
{
    private final GovernanceContextClient   governanceContextClient;    /* Initialized in constructor */
    private final GovernanceListenerManager governanceListenerManager; /* Initialized in constructor */

    private final OpenMetadataClient   openMetadataClient; /* Initialized in constructor */
    private final ConnectedAssetClient connectedAssetClient; /* Initialized in constructor */


    /**
     * Create a client-side object for calling a governance action engine.  Notice there are two instances of the
     * GovernanceContextClient.  It is possible that they are pointing at different metadata server instances so do not
     * consolidate them into one client (even if IntelliJ begs you to :)).
     *
     * @param engineConfig the unique identifier of the governance action engine.
     * @param localServerName the name of the engine host server where the governance action engine is running
     * @param partnerServerName name of partner server
     * @param partnerURLRoot partner platform
     * @param serverUserId user id for the server to use
     * @param openMetadataClient access to the open metadata store
     * @param connectedAssetClient access to the open metadata store
     * @param configurationClient client to retrieve the configuration
     * @param engineActionClient client to control the execution of governance action requests
     * @param governanceContextClient REST client for calls made by the governance action services
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public GovernanceActionEngineHandler(EngineConfig                  engineConfig,
                                         String                        localServerName,
                                         String                        partnerServerName,
                                         String                        partnerURLRoot,
                                         String                        serverUserId,
                                         OpenMetadataClient            openMetadataClient,
                                         ConnectedAssetClient          connectedAssetClient,
                                         GovernanceConfigurationClient configurationClient,
                                         GovernanceContextClient       engineActionClient,
                                         GovernanceContextClient       governanceContextClient,
                                         AuditLog                      auditLog,
                                         int                           maxPageSize)
    {
        super(engineConfig,
              localServerName,
              serverUserId,
              EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceFullName(),
              configurationClient,
              engineActionClient,
              auditLog,
              maxPageSize);

        this.openMetadataClient        = openMetadataClient;
        this.connectedAssetClient      = connectedAssetClient;
        this.governanceListenerManager = new GovernanceListenerManager(auditLog, engineConfig.getEngineQualifiedName());
        this.governanceContextClient   = governanceContextClient;

        this.governanceContextClient.setListenerManager(governanceListenerManager, engineConfig.getEngineQualifiedName());
    }


    /**
     * Pass on the watchdog event to any governance service that supports them.
     *
     * @param watchdogGovernanceEvent element describing the changing metadata data.
     *
     * @throws InvalidParameterException Vital fields of the governance action are not filled out
     */
    public void publishWatchdogEvent(OpenMetadataOutTopicEvent watchdogGovernanceEvent) throws InvalidParameterException
    {
        governanceListenerManager.processEvent(watchdogGovernanceEvent);
    }


    /**
     * Run an instance of a governance action service in its own thread and return the handler (for disconnect processing).
     *
     * @param engineActionGUID unique identifier of the engin action to run
     * @param governanceRequestType governance request type to use when calling the governance engine
     * @param requesterUserId original user requesting this governance service
     * @param requestedStartDate date/time to start the governance action service
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException access permissions problem
     * @throws PropertyServerException there was a problem detected by the governance action engine.
     */
    @Override
    public void runGovernanceService(String                     engineActionGUID,
                                     String                     governanceRequestType,
                                     String                     requesterUserId,
                                     Date                       requestedStartDate,
                                     Map<String, String>        requestParameters,
                                     List<RequestSourceElement> requestSourceElements,
                                     List<ActionTargetElement>  actionTargetElements) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "runGovernanceService";

        super.validateGovernanceEngineInitialized(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName, methodName);

        GovernanceServiceCache governanceServiceCache = super.getServiceCache(governanceRequestType);

        if (governanceServiceCache != null)
        {
            /*
             * Need to combine the request parameters from the SupportedGovernanceService relationship with any from the caller.
             * The caller's request parameters take precedence.  This is done in the governanceServiceCache.
             */
            GovernanceActionServiceHandler governanceActionServiceHandler = new GovernanceActionServiceHandler(serverName,
                                                                                                               governanceEngineProperties,
                                                                                                               governanceEngineGUID,
                                                                                                               serverUserId,
                                                                                                               engineActionGUID,
                                                                                                               engineActionClient,
                                                                                                               governanceServiceCache.getServiceRequestType(),
                                                                                                               governanceServiceCache.getRequestParameters(requestParameters),
                                                                                                               governanceServiceCache.getGenerateIntegrationReport(),
                                                                                                               governanceServiceCache.getDeleteMethod(),
                                                                                                               requesterUserId,
                                                                                                               requestSourceElements,
                                                                                                               actionTargetElements,
                                                                                                               governanceServiceCache.getGovernanceServiceGUID(),
                                                                                                               governanceServiceCache.getGovernanceServiceName(),
                                                                                                               governanceServiceCache.getNextGovernanceService(),
                                                                                                               openMetadataClient,
                                                                                                               connectedAssetClient,
                                                                                                               governanceContextClient,
                                                                                                               configurationClient,
                                                                                                               requestedStartDate,
                                                                                                               auditLog,
                                                                                                               maxPageSize);

            super.startServiceExecutionThread(engineActionGUID,
                                              governanceActionServiceHandler,
                                              governanceServiceCache.getGovernanceServiceName() + engineActionGUID + new Date());
        }
    }
}
