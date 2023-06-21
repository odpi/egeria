/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCollectionHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineConnectionAndEndpointHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineEventTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFindHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFolderHierarchyHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTopicHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

/**
 * DataEngineInstanceHandler retrieves information from the instance map for the access service instances.
 * The instance map is thread-safe. Instances are added and removed by the DataEngineAdmin class.
 */
public class DataEngineInstanceHandler extends OMASServiceInstanceHandler {

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


    /**
     * Retrieve the collection handler for the access service
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
    public DataEngineCollectionHandler getCollectionHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                           InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getDataEngineCollectionHandler();
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

        return instance.getDataEnginePortHandler();
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
    public DataEngineDataFileHandler getDataFileHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);
        return instance.getDataEngineDataFileHandler();
    }

    /**
     * Retrieve the folder hierarchy handler for the access service
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
    public DataEngineFolderHierarchyHandler getFolderHierarchyHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);
        return instance.getDataEngineFolderHierarchyHandler();
    }

    /**
     * Retrieve the connection and endpoint handler for the access service
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
    public DataEngineConnectionAndEndpointHandler getConnectionAndEndpointHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);
        return instance.getDataEngineConnectionAndEndpointHandler();
    }

    /**
     * Retrieve the find handler for the access service
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
    public DataEngineFindHandler getFindHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);
        return instance.getDataEngineFindHandler();
    }

    /**
     * Retrieve the topic handler for the access service
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
    public DataEngineTopicHandler getTopicHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                 InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);
        return instance.getDataEngineTopicHandler();
    }

    /**
     * Retrieve the event type handler for the access service
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
    public DataEngineEventTypeHandler getEventTypeHandler(String userId, String serverName, String serviceOperationName) throws
                                                                                                                 InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException {
        DataEngineServicesInstance instance = (DataEngineServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);
        return instance.getDataEngineEventTypeHandler();
    }
}


