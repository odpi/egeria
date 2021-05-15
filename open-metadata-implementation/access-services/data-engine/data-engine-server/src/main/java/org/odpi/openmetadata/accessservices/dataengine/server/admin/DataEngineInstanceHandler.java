/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCollectionHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

/**
 * DataEngineInstanceHandler retrieves information from the instance map for the access service instances.
 * The instance map is thread-safe. Instances are added and removed by the DataEngineAdmin class.
 */
public class DataEngineInstanceHandler extends OCFOMASServiceInstanceHandler {

    /**
     * Default constructor registers the access service
     */
    public DataEngineInstanceHandler() {
        super(AccessServiceDescription.DATA_ENGINE_OMAS.getAccessServiceName() + " OMAS");

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
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DataEngineProcessHandler getProcessHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                     InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        return instance.getProcessHandler();
    }


    public DataEngineCollectionHandler getCollectionHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                           InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getDataEngineCollecttionHandler();
    }

    /**
     * Retrieve the registration handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of called operation
     *
     * @return handler for use by the requested instance
     *
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DataEngineRegistrationHandler getRegistrationHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                               InvalidParameterException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getDataEngineRegistrationHandler();
    }

    /**
     * Retrieve the common handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of called operation
     *
     * @return handler for use by the requested instance
     *
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DataEngineCommonHandler getCommonHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                    InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getDataEngineCommonHandler();
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
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DataEngineSchemaTypeHandler getDataEngineSchemaTypeHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                                     InvalidParameterException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

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
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DataEnginePortHandler getPortHandler(String userId, String serverName, String serviceOperationName) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getPortHandler();
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
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DataEngineRelationalDataHandler getRelationalDataHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                                   InvalidParameterException,
                                                                                                                                   UserNotAuthorizedException,
                                                                                                                                   PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getDataEngineRelationalDataHandler();
    }

    /**
     * Return the connection used in the client to create a connector to access events to the input topic.
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @return connection object for client
     *
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public Connection getInTopicConnection(String userId, String serverName, String serviceOperationName) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null) {
            return instance.getInTopicConnection();
        }

        return null;
    }

    /**
     * Retrieve the DataFile handler for the access service
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @return handler for use by the requested instance
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public DataEngineDataFileHandler getDataFileHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);
        return instance.getDataEngineDataFileHandler();
    }
}


