/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

public class DataManagerInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DataManagerInstanceHandler()
    {
        super(AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceFullName());

        DataManagerOMASRegistration.registerAccessService();
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
        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                             serverName,
                                                                                                             serviceOperationName);

        if (instance != null)
        {
            return instance.getOutTopicConnection();
        }

        return null;
    }
}
