/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.client.*;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.engineservices.governanceaction.context.GovernanceListenerManager;
import org.odpi.openmetadata.engineservices.governanceaction.properties.EngineSummary;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
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
    private GovernanceEngineClient      governanceEngineClient;    /* Initialized in constructor */
    private GovernanceListenerManager   governanceListenerManager; /* Initialized in constructor */

    private String partnerURLRoot;             /* Initialized in constructor */
    private String partnerServerName;          /* Initialized in constructor */

    private static final String supportGovernanceEngineType = "GovernanceActionEngine";


    /**
     * Create a client-side object for calling a governance action engine.
     *
     * @param engineConfig the unique identifier of the governance action engine.
     * @param localServerName the name of the engine host server where the governance action engine is running
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param governanceEngineClient REST client for direct REST Calls
     * @param governanceListenerManager client for managing event listening
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public GovernanceActionEngineHandler(EngineConfig                        engineConfig,
                                         String                              localServerName,
                                         String                              partnerServerName,
                                         String                              partnerURLRoot,
                                         String                              serverUserId,
                                         GovernanceEngineConfigurationClient configurationClient,
                                         GovernanceEngineClient              governanceEngineClient,
                                         GovernanceListenerManager           governanceListenerManager,
                                         AuditLog                            auditLog,
                                         int                                 maxPageSize)
    {
        super(engineConfig, localServerName, serverUserId, configurationClient, auditLog, maxPageSize);

        this.governanceEngineClient = governanceEngineClient;
        this.governanceListenerManager = governanceListenerManager;
        this.partnerServerName = partnerServerName;
        this.partnerURLRoot = partnerURLRoot;
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

        if (governanceServiceCache != null)
        {
            GovernanceActionServiceHandler governanceActionServiceHandler = new GovernanceActionServiceHandler(governanceEngineProperties,
                                                                                                               governanceEngineGUID,
                                                                                                               serverUserId,
                                                                                                               governanceActionGUID,
                                                                                                               requestType,
                                                                                                               requestParameters,
                                                                                                               requestSourceElements,
                                                                                                               actionTargetElements,
                                                                                                               governanceServiceCache.getGovernanceServiceName(),
                                                                                                               governanceServiceCache.getNextGovernanceService(),
                                                                                                               partnerServerName,
                                                                                                               partnerURLRoot,
                                                                                                               governanceEngineClient,
                                                                                                               governanceListenerManager,
                                                                                                               auditLog);

            Thread thread = new Thread(governanceActionServiceHandler, governanceServiceCache.getGovernanceServiceName() + governanceActionGUID + new Date().toString());
            thread.start();

            return governanceActionServiceHandler;
        }

        return null;
    }


    /**
     * Return details of the governance action engines running in this server.
     *
     * @return list of summaries
     */
    public List<EngineSummary> getLocalEngines()
    {
        return null;
    }
}
