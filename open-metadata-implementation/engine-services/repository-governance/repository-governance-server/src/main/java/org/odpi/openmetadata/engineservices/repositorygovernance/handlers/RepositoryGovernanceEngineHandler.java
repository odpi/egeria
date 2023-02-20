/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceCache;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;
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
     * @param serverClient client used by the engine host services to control the execution of governance action requests
     * @param repositoryGovernanceEngineClient REST client for direct REST Calls to OMRS - used by repository governance services
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public RepositoryGovernanceEngineHandler(EngineConfig                        engineConfig,
                                             String                              serverName,
                                             String                              serverUserId,
                                             GovernanceEngineConfigurationClient configurationClient,
                                             GovernanceEngineClient              serverClient,
                                             EnterpriseRepositoryServicesClient  repositoryGovernanceEngineClient,
                                             AuditLog                            auditLog,
                                             int                                 maxPageSize)
    {
        super(engineConfig,
              serverName,
              serverUserId,
              EngineServiceDescription.REPOSITORY_GOVERNANCE_OMES.getEngineServiceFullName(),
              configurationClient,
              serverClient,
              auditLog,
              maxPageSize);

        this.repositoryGovernanceEngineClient = repositoryGovernanceEngineClient;
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

        if ((governanceServiceCache != null) && (actionTargetElements != null) && (! actionTargetElements.isEmpty()))
        {
            RepositoryGovernanceServiceHandler repositoryGovernanceServiceHandler = this.getRepositoryGovernanceServiceHandler(governanceServiceCache.getGovernanceServiceName(),
                                                                                                                               governanceServiceCache.getServiceRequestType(),
                                                                                                                               governanceServiceCache.getRequestParameters(requestParameters),
                                                                                                                               requestSourceElements,
                                                                                                                               actionTargetElements,
                                                                                                                               governanceActionGUID,
                                                                                                                               governanceServiceCache);

            Thread thread = new Thread(repositoryGovernanceServiceHandler, governanceServiceCache.getGovernanceServiceName() + new Date());
            thread.start();

            return repositoryGovernanceServiceHandler;
        }

        return null;
    }


    /**
     * Create an instance of a repository governance service handler.
     *
     * @param repositoryGovernanceServiceName name of repository governance service
     * @param repositoryGovernanceRequestType type of repository governance request
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param governanceActionGUID unique identifier of the associated governance action entity
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
                                                                                     List<RequestSourceElement> requestSourceElements,
                                                                                     List<ActionTargetElement>  actionTargetElements,
                                                                                     String                     governanceActionGUID,
                                                                                     GovernanceServiceCache     governanceServiceCache) throws InvalidParameterException,
                                                                                                                                               PropertyServerException
    {
        RepositoryGovernanceServiceContext repositoryGovernanceContext = new RepositoryGovernanceServiceContext(engineUserId,
                                                                                                                repositoryGovernanceServiceName,
                                                                                                                repositoryGovernanceRequestType,
                                                                                                                requestParameters,
                                                                                                                requestSourceElements,
                                                                                                                actionTargetElements,
                                                                                                                repositoryGovernanceEngineClient);

        RepositoryGovernanceServiceHandler repositoryGovernanceServiceHandler = new RepositoryGovernanceServiceHandler(governanceEngineProperties,
                                                                                                                       governanceEngineGUID,
                                                                                                                       serverUserId,
                                                                                                                       governanceActionGUID,
                                                                                                                       serverClient,
                                                                                                                       repositoryGovernanceRequestType,
                                                                                                                       governanceServiceCache.getGovernanceServiceGUID(),
                                                                                                                       governanceServiceCache.getGovernanceServiceName(),
                                                                                                                       governanceServiceCache.getNextGovernanceService(),
                                                                                                                       repositoryGovernanceContext,
                                                                                                                       auditLog);

        repositoryGovernanceContext.setRepositoryGovernanceServiceHandler(repositoryGovernanceServiceHandler);

        return repositoryGovernanceServiceHandler;
    }
}
