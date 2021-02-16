/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.server;

import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstance;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.governanceaction.handlers.GovernanceActionEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * GovernanceActionInstance maintains the instance information needed to execute requests on behalf of
 * a engine host server.
 */
public class GovernanceActionInstance extends OMESServiceInstance
{
    private Map<String, GovernanceActionEngineHandler> governanceActionEngineInstances;


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
     * @param governanceActionEngineInstances active governance action engines in this server.
     */
    public GovernanceActionInstance(String                                     serverName,
                                    String                                     serviceName,
                                    AuditLog                                   auditLog,
                                    String                                     localServerUserId,
                                    int                                        maxPageSize,
                                    String                                     accessServiceRootURL,
                                    String                                     accessServiceServerName,
                                    Map<String, GovernanceActionEngineHandler> governanceActionEngineInstances)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize, accessServiceRootURL, accessServiceServerName);

        this.governanceActionEngineInstances = governanceActionEngineInstances;
    }
}
