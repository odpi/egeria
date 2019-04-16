/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.server;

import org.odpi.openmetadata.discoveryserver.ffdc.DiscoveryServerErrorCode;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;

import java.util.Map;

/**
 * DiscoveryServerInstance maintains the instance information needed to execute requests on behalf of
 * a discovery server.
 */
public class DiscoveryServerInstance
{
    private String                               discoveryServerName;
    private Map<String, DiscoveryEngineInstance> discoveryEngineInstances;

    /**
     * Initialize the map of guids to discovery engines for this server.
     *
     * @param discoveryEngineInstances active discovery engines in this server.
     */
    public DiscoveryServerInstance(String                               discoveryServerName,
                                   Map<String, DiscoveryEngineInstance> discoveryEngineInstances)
    {
        this.discoveryServerName = discoveryServerName;
        this.discoveryEngineInstances = discoveryEngineInstances;
    }


    /**
     * Return the discovery engine instance requested on an discovery engine services request.
     *
     * @param discoveryEngineGUID unique identifier of the discovery engine
     * @return discovery engine instance.
     * @throws DiscoveryEngineException the discovery engine guid is not recognized
     */
    public synchronized DiscoveryEngineInstance getDiscoveryEngine(String   discoveryEngineGUID) throws DiscoveryEngineException
    {
        DiscoveryEngineInstance instance = discoveryEngineInstances.get(discoveryEngineGUID);

        if (instance == null)
        {
            final String             methodName   = "getDiscoveryEngine";
            DiscoveryServerErrorCode errorCode    = DiscoveryServerErrorCode.UNKNOWN_DISCOVERY_ENGINE;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(discoveryServerName,
                                                                                                                       discoveryEngineGUID);

            throw new DiscoveryEngineException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction());
        }

        return instance;
    }
}
