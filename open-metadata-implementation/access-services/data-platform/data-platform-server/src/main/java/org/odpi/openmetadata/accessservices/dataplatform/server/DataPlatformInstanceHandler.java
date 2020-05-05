/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

public class DataPlatformInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DataPlatformInstanceHandler()
    {
        super(AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName());

        DataPlatformOMASRegistration.registerAccessService();
    }


    /**
     * Return the connection used in the client to create a connector to access events from the out topic.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return connection object for client
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    Connection getOutTopicConnection(String userId,
                                     String serverName,
                                     String serviceOperationName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        DataPlatformServicesInstance instance = (DataPlatformServicesInstance)super.getServerServiceInstance(userId,
                                                                                                             serverName,
                                                                                                             serviceOperationName);

        if (instance != null)
        {
            return instance.getOutTopicConnection();
        }

        return null;
    }
}
