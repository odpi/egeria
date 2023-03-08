/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;
import org.odpi.openmetadata.engineservices.assetanalysis.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * AssetAnalysisInstanceHandler retrieves information from the instance map for the
 * asset analysis engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetAnalysisAdmin class.
 */
class AssetAnalysisInstanceHandler extends OMESServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    AssetAnalysisInstanceHandler()
    {
        super(EngineServiceDescription.ASSET_ANALYSIS_OMES.getEngineServiceName());

        AssetAnalysisRegistration.registerEngineService();
    }


    /**
     * Retrieve the specific handler for the discovery engine.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param discoveryEngineName unique name of the discovery engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    DiscoveryEngineHandler getDiscoveryEngineHandler(String userId,
                                                     String serverName,
                                                     String discoveryEngineName,
                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        AssetAnalysisInstance instance = (AssetAnalysisInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getDiscoveryEngine(discoveryEngineName);
        }

        return null;
    }
}
