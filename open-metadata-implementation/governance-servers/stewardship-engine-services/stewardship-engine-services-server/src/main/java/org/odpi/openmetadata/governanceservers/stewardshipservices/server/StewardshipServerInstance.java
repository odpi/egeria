/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipservices.server;

import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.stewardshipengineservices.ffdc.StewardshipEngineServicesErrorCode;
import org.odpi.openmetadata.governanceservers.stewardshipengineservices.properties.StewardshipEngineSummary;
import org.odpi.openmetadata.governanceservers.stewardshipservices.handlers.StewardshipEngineHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * StewardshipServerInstance maintains the instance information needed to execute requests on behalf of
 * a stewardship server.
 */
public class StewardshipServerInstance extends GovernanceServerServiceInstance
{
    private Map<String, StewardshipEngineHandler> stewardshipEngineInstances;


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
     * @param stewardshipEngineInstances active stewardship engines in this server.
     */
    StewardshipServerInstance(String                              serverName,
                              String                              serviceName,
                              AuditLog                            auditLog,
                              String                              localServerUserId,
                              int                                 maxPageSize,
                              String                              accessServiceRootURL,
                              String                              accessServiceServerName,
                              Map<String, StewardshipEngineHandler> stewardshipEngineInstances)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize, accessServiceRootURL, accessServiceServerName);

        this.stewardshipEngineInstances = stewardshipEngineInstances;
    }

    /**
     * Return the stewardship engine instance requested on an stewardship engine services request.
     *
     * @return list of stewardship engine summaries.
     */
    synchronized List<StewardshipEngineSummary> getStewardshipEngineStatuses()
    {
        List<StewardshipEngineSummary> results = new ArrayList<>();

        if (stewardshipEngineInstances != null)
        {
            for (StewardshipEngineHandler  stewardshipEngineHandler : stewardshipEngineInstances.values())
            {
                if (stewardshipEngineHandler != null)
                {
                    results.add(stewardshipEngineHandler.getSummary());
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }

    }

    /**
     * Return the stewardship engine instance requested on an stewardship engine services request.
     *
     * @param stewardshipEngineName unique name of the stewardship engine
     * @return stewardship engine instance.
     * @throws InvalidParameterException the stewardship engine name is not recognized
     */
    synchronized StewardshipEngineHandler getStewardshipEngine(String   stewardshipEngineName) throws InvalidParameterException
    {
        final String  methodName        = "getStewardshipEngine";
        final String  guidParameterName = "stewardshipEngineName";

        StewardshipEngineHandler instance = stewardshipEngineInstances.get(stewardshipEngineName);

        if (instance == null)
        {
            throw new InvalidParameterException(StewardshipEngineServicesErrorCode.UNKNOWN_STEWARDSHIP_ENGINE.getMessageDefinition(serverName,
                                                                                                                               stewardshipEngineName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }

        return instance;
    }


    /**
     * Shutdown the engines
     */
    @Override
    public void shutdown()
    {
        if (stewardshipEngineInstances != null)
        {
            for (StewardshipEngineHandler  handler : stewardshipEngineInstances.values())
            {
                if (handler != null)
                {
                    // handler.terminate();
                }
            }
        }

        super.shutdown();
    }
}
