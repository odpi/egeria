/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.handlers;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogContext;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceListenerManager;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceCache;
import org.odpi.openmetadata.governanceservers.enginehostservices.api.WatchdogEventSupportingEngine;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The WatchdogActionEngineHandler runs watchdog action services on demand.  It is initialized
 * with the configuration for the watchdog action services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class WatchdogActionEngineHandler extends GovernanceEngineHandler implements WatchdogEventSupportingEngine
{
    private final GovernanceListenerManager governanceListenerManager; /* Initialized in constructor */
    private final GovernanceContextClient   governanceContextClient; /* Initialized in constructor */

    private final OpenMetadataClient        openMetadataClient; /* Initialized in constructor */

    private final PropertyHelper       propertyHelper   = new PropertyHelper();

    /**
     * Create a client-side object for calling a watchdog action engine.
     *
     * @param engineConfig the unique identifier of the governance action engine.
     * @param localServerName the name of the engine host server where the governance action engine is running
     * @param serverUserId user id for the server to use
     * @param openMetadataClient access to the open metadata store
     * @param configurationClient client to retrieve the configuration
     * @param engineActionClient client to control the execution of governance action requests
     * @param governanceContextClient REST client for calls made by the governance action services
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public WatchdogActionEngineHandler(EngineConfig                  engineConfig,
                                       String                        localServerName,
                                       String                        serverUserId,
                                       OpenMetadataClient            openMetadataClient,
                                       GovernanceConfigurationClient configurationClient,
                                       GovernanceContextClient       engineActionClient,
                                       GovernanceContextClient       governanceContextClient,
                                       AuditLog                      auditLog,
                                       int                           maxPageSize)
    {
        super(engineConfig,
              localServerName,
              serverUserId,
              EngineServiceDescription.WATCHDOG_ACTION_OMES.getEngineServiceFullName(),
              configurationClient,
              engineActionClient,
              auditLog,
              maxPageSize);

        this.openMetadataClient        = openMetadataClient;
        this.governanceListenerManager = new GovernanceListenerManager(auditLog, engineConfig.getEngineQualifiedName());
        this.governanceContextClient   = governanceContextClient;

        governanceContextClient.setListenerManager(governanceListenerManager, engineConfig.getEngineQualifiedName());
    }


    /**
     * Pass on the watchdog event to any governance service that supports them.
     *
     * @param watchdogGovernanceEvent element describing the changing metadata data.
     *
     * @throws InvalidParameterException Vital fields of the governance action are not filled out
     */
    @Override
    public void publishWatchdogEvent(OpenMetadataOutTopicEvent watchdogGovernanceEvent) throws InvalidParameterException
    {
        governanceListenerManager.processEvent(watchdogGovernanceEvent);
    }


    /**
     * Run an instance of a watchdog action service in its own thread and return the handler (for disconnect processing).
     *
     * @param engineActionGUID unique identifier of engine action to activate
     * @param governanceRequestType governance request type to use when calling the governance engine
     * @param requesterUserId original user requesting this governance service
     * @param requestedStartDate date/time to start the governance service
     * @param requestParameters name-value properties to control the governance service
     * @param requestSourceElements metadata elements associated with the request to the governance service
     * @param actionTargetElements metadata elements that need to be worked on by the governance service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the governance engine.
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

        super.validateGovernanceEngineInitialized(OpenMetadataType.WATCHDOG_ACTION_ENGINE.typeName, methodName);

        GovernanceServiceCache governanceServiceCache = super.getServiceCache(governanceRequestType);

        if (governanceServiceCache != null)
        {
            WatchdogActionServiceHandler watchdogActionServiceHandler = this.getWatchdogActionServiceHandler(governanceRequestType,
                                                                                                             requestParameters,
                                                                                                             actionTargetElements,
                                                                                                             engineActionGUID,
                                                                                                             requesterUserId,
                                                                                                             requestedStartDate,
                                                                                                             governanceServiceCache);

            super.startServiceExecutionThread(engineActionGUID,
                                              watchdogActionServiceHandler,
                                              governanceServiceCache.getGovernanceServiceName() + engineActionGUID + new Date());
        }
        else
        {
            throw new InvalidParameterException(WatchdogActionErrorCode.NULL_REQUEST.getMessageDefinition(engineActionGUID),
                                                this.getClass().getName(),
                                                methodName,
                                                "governanceServiceCache");
        }
    }


    /**
     * Create an instance of a watchdog action service handler.
     *
     * @param requestType type of monitor request from caller
     * @param requestParameters parameters for the watchdog
     * @param actionTargetElements the elements for the service to work on
     * @param engineActionGUID unique identifier of the associated engine action entity
     * @param requesterUserId original user requesting this governance service
     * @param requestedStartDate date/time that the governance service should start executing
     * @param governanceServiceCache factory for watchdog action services
     *
     * @return unique identifier for this request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException the caller is not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the watchdog action engine.
     */
    private WatchdogActionServiceHandler getWatchdogActionServiceHandler(String                    requestType,
                                                                         Map<String, String>       requestParameters,
                                                                         List<ActionTargetElement> actionTargetElements,
                                                                         String                    engineActionGUID,
                                                                         String                    requesterUserId,
                                                                         Date                      requestedStartDate,
                                                                         GovernanceServiceCache    governanceServiceCache) throws InvalidParameterException,
                                                                                                                                  UserNotAuthorizedException,
                                                                                                                                  PropertyServerException
    {
        WatchdogContext watchdogContext = new WatchdogContext(serverName,
                                                              engineServiceName,
                                                              null,
                                                              null,
                                                              engineActionGUID,
                                                              governanceServiceCache.getGovernanceServiceName(),
                                                              engineUserId,
                                                              governanceServiceCache.getGovernanceServiceGUID(),
                                                              governanceServiceCache.getGenerateIntegrationReport(),
                                                              openMetadataClient,
                                                              engineActionClient,
                                                              engineActionClient,
                                                              governanceContextClient,
                                                              auditLog,
                                                              maxPageSize,
                                                              governanceServiceCache.getDeleteMethod(),
                                                              engineActionGUID,
                                                              requestType,
                                                              governanceServiceCache.getRequestParameters(requestParameters),
                                                              actionTargetElements,
                                                              governanceServiceCache.getGovernanceServiceName(),
                                                              requesterUserId);

        return new WatchdogActionServiceHandler(governanceEngineProperties,
                                                governanceEngineGUID,
                                                engineUserId,
                                                engineActionGUID,
                                                engineActionClient,
                                                governanceServiceCache.getServiceRequestType(),
                                                governanceServiceCache.getGovernanceServiceGUID(),
                                                governanceServiceCache.getGovernanceServiceName(),
                                                governanceServiceCache.getNextGovernanceService(),
                                                watchdogContext,
                                                requestedStartDate,
                                                auditLog);
    }
}
