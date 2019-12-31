/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.ProcessHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * DataEngineInstanceHandler retrieves information from the instance map for the access service instances.
 * The instance map is thread-safe. Instances are added and removed by the DataEngineAdmin class.
 */
public class DataEngineInstanceHandler extends OCFOMASServiceInstanceHandler {

    /**
     * Default constructor registers the access service
     */
    public DataEngineInstanceHandler() {
        super(AccessServiceDescription.DATA_ENGINE_OMAS.getAccessServiceName());

        DataEngineRegistration.registerAccessService();
    }

    /**
     * Retrieve the process handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @return handler for use by the requested instance
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public ProcessHandler getProcessHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                           InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        return instance.getProcessHandler();
    }

    /**
     * Retrieve the registration handler for the access service
     *
     * @param userId     calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of called operation
     *
     * @return handler for use by the requested instance
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public DataEngineRegistrationHandler getRegistrationHandler(String userId, String serverName,
                                                                String serviceOperationName) throws
                                                                                                 InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        return instance.getDataEngineRegistrationHandler();
    }

    /**
     * Retrieve the data engine schema type handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @return handler for use by the requested instance
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public DataEngineSchemaTypeHandler getDataEngineSchemaTypeHandler(String userId, String serverName,
                                                                      String serviceOperationName) throws
                                                                                                   InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        return instance.getDataEngineSchemaTypeHandler();
    }

    /**
     * Retrieve the port handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @return handler for use by the requested instance
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public PortHandler getPortHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                     InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        return instance.getPortHandler();
    }
}


