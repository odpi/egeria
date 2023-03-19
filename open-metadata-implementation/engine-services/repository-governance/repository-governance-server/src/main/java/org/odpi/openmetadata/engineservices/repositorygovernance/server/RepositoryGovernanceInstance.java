/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.server;

import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstance;
import org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.repositorygovernance.handlers.RepositoryGovernanceEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * RepositoryGovernanceInstance maintains the instance information needed to execute requests on behalf of
 * a engine host server.
 */
public class RepositoryGovernanceInstance extends OMESServiceInstance
{
    private Map<String, RepositoryGovernanceEngineHandler> repositoryGovernanceEngineInstances;


    /**
     * Constructor where REST Services used.
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog link to the repository responsible for servicing the REST calls.
     * @param localServerUserId userId to use for local server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param accessServiceRootURL URL root for server platform where the access service is running.
     * @param accessServiceServerName name of the server where the access service is running.
     * @param repositoryGovernanceEngineInstances active repository governance engines in this server.
     */
    public RepositoryGovernanceInstance(String                                         serverName,
                                        String                                         serviceName,
                                        AuditLog                                       auditLog,
                                        String                                         localServerUserId,
                                        int                                            maxPageSize,
                                        String                                         accessServiceRootURL,
                                        String                                         accessServiceServerName,
                                        Map<String, RepositoryGovernanceEngineHandler> repositoryGovernanceEngineInstances)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize, accessServiceRootURL, accessServiceServerName);

        this.repositoryGovernanceEngineInstances = repositoryGovernanceEngineInstances;
    }



    /**
     * Return the repository governance engine instance requested on an repository governance engine services request.
     *
     * @param repositoryGovernanceEngineName unique name of the repository governance engine
     * @return repositoryGovernance engine instance.
     * @throws InvalidParameterException the repository governance engine name is not recognized
     */
    synchronized RepositoryGovernanceEngineHandler getRepositoryGovernanceEngine(String   repositoryGovernanceEngineName) throws InvalidParameterException
    {
        final String  methodName        = "getRepositoryGovernanceEngine";
        final String  guidParameterName = "archiveEngineName";

        RepositoryGovernanceEngineHandler instance = repositoryGovernanceEngineInstances.get(repositoryGovernanceEngineName);

        if (instance == null)
        {
            throw new InvalidParameterException(RepositoryGovernanceErrorCode.UNKNOWN_REPOSITORY_GOVERNANCE_ENGINE.getMessageDefinition(serverName,
                                                                                                                     repositoryGovernanceEngineName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }

        return instance;
    }
}
