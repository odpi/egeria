/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;


import org.odpi.openmetadata.adminservices.registration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistrationEntry;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMASServiceInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceProgramAdmin class.
 */
public class OMASServiceInstanceHandler extends AuditableServerServiceInstanceHandler
{
    private final RESTExceptionHandler  exceptionHandler = new RESTExceptionHandler();

    private Map<String, String> accessServiceLookupTable = null;


    /**
     * Constructor
     *
     * @param serviceName a descriptive name for the OMAS
     */
    public OMASServiceInstanceHandler(String   serviceName)
    {
        super(serviceName);
    }


    /**
     * Retrieve the repository connector for the access service.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return repository connector for exclusive use by the requested instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the repository connector is
     *                                 not available - indicating a logic error
     */
    public OMRSRepositoryConnector getRepositoryConnector(String userId,
                                                          String serverName,
                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId,
                                                                                            serverName,
                                                                                            serviceOperationName);

        return instance.getRepositoryConnector();
    }


    /**
     * Retrieve the repository helper for the access service.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return repository connector for exclusive use by the requested instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the repository connector is
     *                                 not available - indicating a logic error
     */
    public OMRSRepositoryHelper getRepositoryHelper(String userId,
                                                    String serverName,
                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId,
                                                                                            serverName,
                                                                                            serviceOperationName);

        return instance.getRepositoryHelper();
    }


    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return metadata collection for exclusive use by the requested instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public OMRSMetadataCollection getMetadataCollection(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId,
                                                                                            serverName,
                                                                                            serviceOperationName);

        return instance.getMetadataCollection();
    }


    /**
     * Retrieve the repository handler for the access service. Provides an advanced API for the
     * repository services.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return repository handler
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public RepositoryHandler getRepositoryHandler(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId,
                                                                                            serverName,
                                                                                            serviceOperationName);

        return instance.getRepositoryHandler();
    }


    /**
     * Retrieve the handler for managing errors from the repository services.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return repository error handler
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public RepositoryErrorHandler getErrorHandler(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId,
                                                                                            serverName,
                                                                                            serviceOperationName);

        return instance.getErrorHandler();
    }


    /**
     * Return the service's official name.
     *
     * @param callingServiceURLName url fragment that indicates the service name
     * @return String name
     */
    public String  getServiceName(String callingServiceURLName)
    {

        if (accessServiceLookupTable == null)
        {
            accessServiceLookupTable = new HashMap<>();

            List<AccessServiceRegistrationEntry> accessServiceRegistrationList = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();

            for (AccessServiceRegistrationEntry registration : accessServiceRegistrationList)
            {
                accessServiceLookupTable.put(registration.getAccessServiceURLMarker(), registration.getAccessServiceFullName());
            }
        }

        if (accessServiceLookupTable.get(callingServiceURLName) != null)
        {
            return accessServiceLookupTable.get(callingServiceURLName);
        }
        else
        {
            return callingServiceURLName;
        }
    }


    /**
     * Get the instance for a specific service.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return specific service instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    private OMASServiceInstance getCallingServiceInstance(String userId,
                                                          String serverName,
                                                          String callingServiceURLName,
                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return (OMASServiceInstance)platformInstanceMap.getServiceInstance(userId,
                                                                           serverName,
                                                                           this.getServiceName(callingServiceURLName),
                                                                           serviceOperationName);
    }


    /**
     * Get the supportedZones for a specific service. This is used in services that are shared by different
     * access services.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public List<String> getSupportedZones(String  userId,
                                          String  serverName,
                                          String  callingServiceURLName,
                                          String  serviceOperationName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        OMASServiceInstance callingServiceInstance = this.getCallingServiceInstance(userId,
                                                                                    serverName,
                                                                                    callingServiceURLName,
                                                                                    serviceOperationName);
        if (callingServiceInstance != null)
        {
            return callingServiceInstance.getSupportedZones();
        }

        return null;
    }


    /**
     * Get the defaultZones for a specific service.  This is used in services that are shared by different
     * access services.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public List<String> getDefaultZones(String  userId,
                                        String  serverName,
                                        String  callingServiceURLName,
                                        String  serviceOperationName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        OMASServiceInstance callingServiceInstance = this.getCallingServiceInstance(userId,
                                                                                    serverName,
                                                                                    callingServiceURLName,
                                                                                    serviceOperationName);
        if (callingServiceInstance != null)
        {
            return callingServiceInstance.getDefaultZones();
        }

        return null;
    }


    /**
     * Get the publishZones for a specific service.  This is used in services that are shared by different
     * access services.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public List<String> getPublishZones(String  userId,
                                        String  serverName,
                                        String  callingServiceURLName,
                                        String  serviceOperationName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        OMASServiceInstance callingServiceInstance = this.getCallingServiceInstance(userId,
                                                                                    serverName,
                                                                                    callingServiceURLName,
                                                                                    serviceOperationName);
        if (callingServiceInstance != null)
        {
            return callingServiceInstance.getPublishZones();
        }

        return null;
    }



    /**
     * Retrieve the supported zones set up for this service instance.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public List<String> getSupportedZones(String userId,
                                          String serverName,
                                          String serviceOperationName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId,
                                                                                            serverName,
                                                                                            serviceOperationName);

        return instance.getSupportedZones();
    }


    /**
     * Retrieve the default zones set up for this service instance.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public List<String> getDefaultZones(String userId,
                                        String serverName,
                                        String serviceOperationName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId,
                                                                                            serverName,
                                                                                            serviceOperationName);

        return instance.getDefaultZones();
    }


    /**
     * Retrieve the publishZones set up for this service instance.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public List<String> getPublishZones(String userId,
                                        String serverName,
                                        String serviceOperationName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId,
                                                                                            serverName,
                                                                                            serviceOperationName);

        return instance.getPublishZones();
    }



    /**
     * Return the connection used in the client to create a connector to access events from the out topic.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @param callerId unique identifier of the caller
     * @return connection object for client
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public Connection getInTopicConnection(String userId,
                                           String serverName,
                                           String serviceOperationName,
                                           String callerId) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                           serverName,
                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getInTopicClientConnection(callerId);
        }

        return null;
    }



    /**
     * Return the connection used in the client to create a connector to access events from the out topic.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @param callerId unique identifier of the caller
     * @return connection object for client
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public Connection getOutTopicConnection(String userId,
                                            String serverName,
                                            String serviceOperationName,
                                            String callerId) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                           serverName,
                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getOutTopicClientConnection(callerId);
        }

        return null;
    }


    /**
     * Retrieve the exception handler that can package up common exceptions and pack them into
     * a REST Response.
     *
     * @return exception handler object
     */
    public RESTExceptionHandler getExceptionHandler() { return exceptionHandler; }
}
