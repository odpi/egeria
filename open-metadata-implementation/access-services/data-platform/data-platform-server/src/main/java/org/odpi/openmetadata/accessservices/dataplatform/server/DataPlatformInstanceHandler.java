/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.accessservices.dataplatform.handlers.DeployedDatabaseSchemaAssetHandler;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.RegistrationHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

public class DataPlatformInstanceHandler extends OCFOMASServiceInstanceHandler {

    /**
     * Default constructor registers the access service
     */
    public DataPlatformInstanceHandler() {
        super(AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName());
        DataPlatformOMASRegistration.registerAccessService();
    }

    /**
     * Retrieve the registration handler for the access service
     *
     * @param userId     calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public RegistrationHandler getRegistrationHandler(String userId, String serverName, String serviceOperationName) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {

        DataPlatformServicesInstance instance = (DataPlatformServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        if (instance != null) { return instance.getRegistrationHandler(); }

        return null;
    }

    /**
     * Retrieve the DeployedDatabaseSchemaAssetHandler for the access service
     *
     * @param userId     calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DeployedDatabaseSchemaAssetHandler getDeployedDatabaseSchemaAssetHandler(String userId, String serverName, String serviceOperationName) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {

        DataPlatformServicesInstance instance = (DataPlatformServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        if (instance != null) { return instance.getDeployedDatabaseSchemaAssetHandler(); }

        return null;
    }
}
