/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.archivemanager.handlers;

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
 * The ArchiveEngineHandler is responsible for running archive services on demand.  It is initialized
 * with the configuration for the archive services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class ArchiveEngineHandler extends GovernanceEngineHandler
{
    private EnterpriseRepositoryServicesClient archiveEngineClient;    /* Initialized in constructor */

    private static final String supportGovernanceEngineType = "ArchiveEngine";

    /**
     * Create a client-side object for calling a archive engine.
     *
     * @param engineConfig the unique identifier of the archive engine.
     * @param serverName the name of the engine host server where the archive engine is running
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param serverClient client used by the engine host services to control the execution of governance action requests
     * @param archiveEngineClient REST client for direct REST Calls to Archive Engine OMAS - used by archive services
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public ArchiveEngineHandler(EngineConfig                        engineConfig,
                                String                              serverName,
                                String                              serverUserId,
                                GovernanceEngineConfigurationClient configurationClient,
                                GovernanceEngineClient              serverClient,
                                EnterpriseRepositoryServicesClient  archiveEngineClient,
                                AuditLog                            auditLog,
                                int                                 maxPageSize)
    {
        super(engineConfig,
              serverName,
              serverUserId,
              EngineServiceDescription.ARCHIVE_MANAGER_OMES.getEngineServiceFullName(),
              configurationClient,
              serverClient,
              auditLog,
              maxPageSize);

        this.archiveEngineClient = archiveEngineClient;
    }


    /**
     * Run an instance of a governance action service in its own thread and return the handler (for disconnect processing).
     *
     * @param governanceActionGUID unique identifier of the asset to analyse
     * @param requestType unique identifier of the asset that the annotations should be attached to
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
                                                         String                     requestType,
                                                         Map<String, String>        requestParameters,
                                                         List<RequestSourceElement> requestSourceElements,
                                                         List<ActionTargetElement>  actionTargetElements) throws InvalidParameterException,
                                                                                                                 PropertyServerException
    {
        final String methodName = "runGovernanceService";

        super.validateGovernanceEngineInitialized(supportGovernanceEngineType, methodName);

        GovernanceServiceCache governanceServiceCache = super.getServiceCache(requestType);

        if ((governanceServiceCache != null) && (actionTargetElements != null) && (! actionTargetElements.isEmpty()))
        {
            ArchiveServiceHandler archiveServiceHandler = this.getArchiveServiceHandler(governanceServiceCache.getGovernanceServiceName(),
                                                                                        requestType,
                                                                                        requestParameters,
                                                                                        requestSourceElements,
                                                                                        actionTargetElements,
                                                                                        governanceActionGUID,
                                                                                        governanceServiceCache);

            Thread thread = new Thread(archiveServiceHandler, governanceServiceCache.getGovernanceServiceName() + new Date().toString());
            thread.start();

            return archiveServiceHandler;
        }

        return null;
    }


    /**
     * Create an instance of a archive service handler.
     *
     * @param archiveServiceName name of archive service
     * @param archiveRequestType type of archive request
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param governanceActionGUID unique identifier of the associated governance action entity
     * @param governanceServiceCache factory for archive services
     *
     * @return unique identifier for this request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there was a problem detected by the archive engine.
     */
    private ArchiveServiceHandler getArchiveServiceHandler(String                     archiveServiceName,
                                                           String                     archiveRequestType,
                                                           Map<String, String>        requestParameters,
                                                           List<RequestSourceElement> requestSourceElements,
                                                           List<ActionTargetElement>  actionTargetElements,
                                                           String                     governanceActionGUID,
                                                           GovernanceServiceCache     governanceServiceCache) throws InvalidParameterException,
                                                                                                                     PropertyServerException
    {
        ArchiveServiceContext archiveContext = new ArchiveServiceContext(engineUserId,
                                                                         archiveServiceName,
                                                                         archiveRequestType,
                                                                         requestParameters,
                                                                         requestSourceElements,
                                                                         actionTargetElements,
                                                                         archiveEngineClient);

        ArchiveServiceHandler archiveServiceHandler = new ArchiveServiceHandler(governanceEngineProperties,
                                                                                governanceEngineGUID,
                                                                                serverUserId,
                                                                                governanceActionGUID,
                                                                                serverClient,
                                                                                archiveRequestType,
                                                                                governanceServiceCache.getGovernanceServiceGUID(),
                                                                                governanceServiceCache.getGovernanceServiceName(),
                                                                                governanceServiceCache.getNextGovernanceService(),
                                                                                archiveContext,
                                                                                auditLog);

        archiveContext.setArchiveServiceHandler(archiveServiceHandler);

        return archiveServiceHandler;
    }
}
