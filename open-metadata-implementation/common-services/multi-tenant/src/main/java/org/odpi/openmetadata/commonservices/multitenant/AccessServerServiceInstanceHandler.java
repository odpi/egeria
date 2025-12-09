/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.registration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccessServerServiceInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceProgramAdmin class.
 */
public class AccessServerServiceInstanceHandler extends AuditableServerServiceInstanceHandler
{
    private final RESTExceptionHandler  exceptionHandler = new RESTExceptionHandler();

    private Map<String, String> accessServiceLookupTable = null;


    /**
     * Constructor
     *
     * @param serviceName a descriptive name for the OMAS
     */
    public AccessServerServiceInstanceHandler(String   serviceName)
    {
        super(serviceName);
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
        AccessServerServiceInstance instance = (AccessServerServiceInstance) super.getServerServiceInstance(userId,
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
        AccessServerServiceInstance instance = (AccessServerServiceInstance) super.getServerServiceInstance(userId,
                                                                                                            serverName,
                                                                                                            serviceOperationName);

        return instance.getMetadataCollection();
    }


    /**
     * Return the service's official name.
     *
     * @param callingServiceURLName url fragment that indicates the service name
     * @return String name
     */
    public String  getServiceName(String callingServiceURLName)
    {
        /*
         * Access services are dynamically registered.
         */
        if (accessServiceLookupTable == null)
        {
            accessServiceLookupTable = new HashMap<>();

            List<AccessServiceRegistrationEntry> accessServiceRegistrationList = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();

            for (AccessServiceRegistrationEntry registration : accessServiceRegistrationList)
            {
                accessServiceLookupTable.put(registration.getAccessServiceURLMarker(), registration.getAccessServiceName());
            }
        }

        if (accessServiceLookupTable.get(callingServiceURLName) != null)
        {
            return accessServiceLookupTable.get(callingServiceURLName);
        }
        else
        {
            /*
             * Common services are always loaded.
             */
            for (CommonServicesDescription servicesDescription : CommonServicesDescription.values())
            {
                if ((servicesDescription.getServiceURLMarker() != null) &&
                    (servicesDescription.getServiceURLMarker().equals(callingServiceURLName)))
                {
                    return servicesDescription.getServiceName();
                }
            }
        }

        /*
         * No mapping
         */
        return callingServiceURLName;
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
        AccessServerServiceInstance instance = (AccessServerServiceInstance)super.getServerServiceInstance(userId,
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
