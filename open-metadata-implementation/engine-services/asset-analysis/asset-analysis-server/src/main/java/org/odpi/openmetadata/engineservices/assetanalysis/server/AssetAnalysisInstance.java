/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.server;

import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstance;
import org.odpi.openmetadata.engineservices.assetanalysis.ffdc.AssetAnalysisErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.assetanalysis.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.engineservices.assetanalysis.properties.DiscoveryEngineSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AssetAnalysisInstance maintains the instance information needed to execute requests on behalf of
 * a discovery server.
 */
public class AssetAnalysisInstance extends OMESServiceInstance
{
    private Map<String, DiscoveryEngineHandler> discoveryEngineInstances;


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
     * @param discoveryEngineInstances active discovery engines in this server.
     */
    public AssetAnalysisInstance(String                              serverName,
                                 String                              serviceName,
                                 AuditLog                            auditLog,
                                 String                              localServerUserId,
                                 int                                 maxPageSize,
                                 String                              accessServiceRootURL,
                                 String                              accessServiceServerName,
                                 Map<String, DiscoveryEngineHandler> discoveryEngineInstances)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize, accessServiceRootURL, accessServiceServerName);

        this.discoveryEngineInstances = discoveryEngineInstances;
    }


    /**
     * Return the discovery engine instance requested on an discovery engine services request.
     *
     * @return list of discovery engine summaries.
     */
    synchronized List<DiscoveryEngineSummary> getDiscoveryEngineStatuses()
    {
        List<DiscoveryEngineSummary> results = new ArrayList<>();

        if (discoveryEngineInstances != null)
        {
            for (DiscoveryEngineHandler  discoveryEngineHandler : discoveryEngineInstances.values())
            {
                if (discoveryEngineHandler != null)
                {
                    results.add(discoveryEngineHandler.getSummary());
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
     * Return the discovery engine instance requested on an discovery engine services request.
     *
     * @param discoveryEngineName unique name of the discovery engine
     * @return discovery engine instance.
     * @throws InvalidParameterException the discovery engine name is not recognized
     */
    synchronized DiscoveryEngineHandler getDiscoveryEngine(String   discoveryEngineName) throws InvalidParameterException
    {
        final String  methodName        = "getDiscoveryEngine";
        final String  guidParameterName = "discoveryEngineName";

        DiscoveryEngineHandler instance = discoveryEngineInstances.get(discoveryEngineName);

        if (instance == null)
        {
            throw new InvalidParameterException(AssetAnalysisErrorCode.UNKNOWN_DISCOVERY_ENGINE.getMessageDefinition(serverName,
                                                                                                                     discoveryEngineName),
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
        if (discoveryEngineInstances != null)
        {
            for (DiscoveryEngineHandler  handler : discoveryEngineInstances.values())
            {
                if (handler != null)
                {
                    handler.terminate();
                }
            }
        }

        super.shutdown();
    }
}
