/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.ExternalIdentifierHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ValidValuesHandler;
import org.odpi.openmetadata.commonservices.multitenant.AccessServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValueDetail;
import org.odpi.openmetadata.frameworkservices.omf.handlers.MetadataElementHandler;

/**
 * omfMetadataManagementInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceEngineAdmin class.
 */
public class OMFServicesInstanceHandler extends AccessServerServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    public OMFServicesInstanceHandler()
    {
        super(AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName());

        OMFServicesRegistration.registerAccessService();
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    MetadataElementHandler<OpenMetadataElement> getMetadataElementHandler(String userId,
                                                                          String serverName,
                                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        OMFServicesInstance instance = (OMFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getMetadataElementHandler();
        }

        return null;
    }



    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ReferenceableHandler<MetadataElementSummary> getMetadataElementSummaryHandler(String userId,
                                                                                  String serverName,
                                                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        OMFServicesInstance instance = (OMFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getMetadataElementSummaryHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> getExternalIdentifierHandler(String userId,
                                                                                                     String serverName,
                                                                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                                                                         UserNotAuthorizedException,
                                                                                                                                         PropertyServerException
    {
        OMFServicesInstance instance = (OMFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getExternalIdentifierHandler();
        }

        return null;
    }



    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ValidValuesHandler<ValidMetadataValue> getValidMetadataValuesHandler(String userId,
                                                                         String serverName,
                                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        OMFServicesInstance instance = (OMFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getValidMetadataValuesHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ValidValuesHandler<ValidMetadataValueDetail> getValidMetadataValuesDetailHandler(String userId,
                                                                                     String serverName,
                                                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         PropertyServerException
    {
        OMFServicesInstance instance = (OMFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getValidMetadataValuesDetailHandler();
        }

        return null;
    }
}
