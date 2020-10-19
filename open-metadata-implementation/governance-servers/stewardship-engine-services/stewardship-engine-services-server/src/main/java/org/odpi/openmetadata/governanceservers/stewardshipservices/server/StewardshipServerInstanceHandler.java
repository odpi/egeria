/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.stewardshipengineservices.properties.StewardshipEngineSummary;
import org.odpi.openmetadata.governanceservers.stewardshipservices.handlers.StewardshipEngineHandler;

import java.util.List;

/**
 * StewardshipEngineServiceInstanceHandler retrieves information from the instance map for the
 * stewardship server instances.  The instance map is thread-safe.  Instances are added
 * and removed by the StewardshipServerOperationalServices class.
 */
class StewardshipServerInstanceHandler extends GovernanceServerServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    StewardshipServerInstanceHandler()
    {
        super(GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceName());
    }


    /**
     * Retrieve the status of each assigned stewardship engines.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of stewardship engine statuses
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<StewardshipEngineSummary> getStewardshipEngineStatuses(String userId,
                                                                String serverName,
                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        StewardshipServerInstance instance = (StewardshipServerInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getStewardshipEngineStatuses();
        }

        return null;
    }



    /**
     * Retrieve the specific handler for the stewardship engine.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param stewardshipEngineName unique name of the stewardship engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    StewardshipEngineHandler getStewardshipEngineHandler(String userId,
                                                         String serverName,
                                                         String stewardshipEngineName,
                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        StewardshipServerInstance instance = (StewardshipServerInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getStewardshipEngine(stewardshipEngineName);
        }

        return null;
    }
}
