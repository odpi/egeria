/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.handlers;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceCache;
import org.odpi.openmetadata.repositoryservices.clients.EnterpriseRepositoryServicesClient;

import java.util.*;

/**
 * The RepositoryGovernanceEngineHandler is responsible for running repository governance services on demand.  It is initialized
 * with the configuration for the repository governance services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class RepositoryGovernanceEngineHandler extends GovernanceEngineHandler
{
    private final EnterpriseRepositoryServicesClient repositoryGovernanceEngineClient;    /* Initialized in constructor */

    private static final String supportGovernanceEngineType = "RepositoryGovernanceEngine";

    /**
     * Create a client-side object for calling a repository governance engine.
     *
     * @param engineConfig the unique identifier of the repository governance engine.
     * @param serverName the name of the engine host server where the repository governance engine is running
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param engineActionClient client used by the engine host services to control the execution of governance action requests
     * @param repositoryGovernanceEngineClient REST client for direct REST Calls to OMRS - used by repository governance services
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public RepositoryGovernanceEngineHandler(EngineConfig                        engineConfig,
                                             String                              serverName,
                                             String                              serverUserId,
                                             GovernanceConfigurationClient       configurationClient,
                                             GovernanceContextClient             engineActionClient,
                                             EnterpriseRepositoryServicesClient  repositoryGovernanceEngineClient,
                                             AuditLog                            auditLog,
                                             int                                 maxPageSize)
    {
        super(engineConfig,
              serverName,
              serverUserId,
              EngineServiceDescription.REPOSITORY_GOVERNANCE_OMES.getEngineServiceFullName(),
              configurationClient,
              engineActionClient,
              auditLog,
              maxPageSize);

        this.repositoryGovernanceEngineClient = repositoryGovernanceEngineClient;
    }


    /**
     * Run an instance of a governance action service in its own thread and return the handler (for disconnect processing).
     *
     * @param engineActionGUID unique identifier of the asset to analyse
     * @param governanceRequestType governance request type to use when calling the governance engine
     * @param requesterUserId original user requesting this governance service
     * @param requestedStartDate date/time to start the governance action service
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
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

        super.validateGovernanceEngineInitialized(supportGovernanceEngineType, methodName);

        GovernanceServiceCache governanceServiceCache = super.getServiceCache(governanceRequestType);

        if ((governanceServiceCache != null) && (actionTargetElements != null) && (! actionTargetElements.isEmpty()))
        {
            RepositoryGovernanceServiceHandler repositoryGovernanceServiceHandler = this.getRepositoryGovernanceServiceHandler(governanceServiceCache.getGovernanceServiceName(),
                                                                                                                               governanceServiceCache.getServiceRequestType(),
                                                                                                                               governanceServiceCache.getRequestParameters(requestParameters),
                                                                                                                               requesterUserId,
                                                                                                                               requestSourceElements,
                                                                                                                               actionTargetElements,
                                                                                                                               engineActionGUID,
                                                                                                                               requestedStartDate,
                                                                                                                               governanceServiceCache);

            startServiceExecutionThread(engineActionGUID,
                                        repositoryGovernanceServiceHandler,
                                        governanceServiceCache.getGovernanceServiceName() + new Date());
        }
    }


    /**
     * Create an instance of a repository governance service handler.
     *
     * @param repositoryGovernanceServiceName name of repository governance service
     * @param repositoryGovernanceRequestType type of repository governance request
     * @param requestParameters name-value properties to control the governance action service
     * @param requesterUserId original user requesting this governance service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param engineActionGUID unique identifier of the associated engine action entity
     * @param requestedStartDate date/time that the governance service should start executing
     * @param governanceServiceCache factory for repository governance services
     *
     * @return unique identifier for this request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there was a problem detected by the repository governance engine.
     */
    private RepositoryGovernanceServiceHandler getRepositoryGovernanceServiceHandler(String                     repositoryGovernanceServiceName,
                                                                                     String                     repositoryGovernanceRequestType,
                                                                                     Map<String, String>        requestParameters,
                                                                                     String                     requesterUserId,
                                                                                     List<RequestSourceElement> requestSourceElements,
                                                                                     List<ActionTargetElement>  actionTargetElements,
                                                                                     String                     engineActionGUID,
                                                                                     Date                       requestedStartDate,
                                                                                     GovernanceServiceCache     governanceServiceCache) throws InvalidParameterException,
                                                                                                                                               PropertyServerException
    {
        RepositoryGovernanceServiceContext repositoryGovernanceContext = new RepositoryGovernanceServiceContext(engineUserId,
                                                                                                                repositoryGovernanceServiceName,
                                                                                                                repositoryGovernanceRequestType,
                                                                                                                requestParameters,
                                                                                                                requesterUserId,
                                                                                                                requestSourceElements,
                                                                                                                actionTargetElements,
                                                                                                                repositoryGovernanceEngineClient);

        RepositoryGovernanceServiceHandler repositoryGovernanceServiceHandler = new RepositoryGovernanceServiceHandler(governanceEngineProperties,
                                                                                                                       governanceEngineGUID,
                                                                                                                       serverUserId,
                                                                                                                       engineActionGUID,
                                                                                                                       engineActionClient,
                                                                                                                       repositoryGovernanceRequestType,
                                                                                                                       governanceServiceCache.getGovernanceServiceGUID(),
                                                                                                                       governanceServiceCache.getGovernanceServiceName(),
                                                                                                                       governanceServiceCache.getNextGovernanceService(),
                                                                                                                       repositoryGovernanceContext,
                                                                                                                       requestedStartDate,
                                                                                                                       auditLog);

        repositoryGovernanceContext.setRepositoryGovernanceServiceHandler(repositoryGovernanceServiceHandler);

        return repositoryGovernanceServiceHandler;
    }
}
