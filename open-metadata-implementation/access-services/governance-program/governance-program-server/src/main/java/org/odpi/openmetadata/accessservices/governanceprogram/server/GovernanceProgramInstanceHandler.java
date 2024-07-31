/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.handlers.AppointmentHandler;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

/**
 * GovernanceProgramInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceProgramAdmin class.
 */
class GovernanceProgramInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    GovernanceProgramInstanceHandler()
    {
        super(AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceFullName());

        GovernanceProgramRegistration.registerAccessService();
    }



    /**
     * Retrieve the specific converter for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return converter for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ReferenceableHandler<RelatedElement> getRelatedElementHandler(String userId,
                                                                  String serverName,
                                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getRelatedElementHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific converter for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return converter for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ReferenceableHandler<ElementStub> getElementStubHandler(String userId,
                                                            String serverName,
                                                            String serviceOperationName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getElementStubHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific converter for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return converter for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    AssetHandler<RelatedElement> getRelatedAssetHandler(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getRelatedAssetHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific converter for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return converter for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    AssetHandler<ElementStub> getAssetHandler(String userId,
                                              String serverName,
                                              String serviceOperationName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getAssetHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific converter for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return converter for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ElementStubConverter<ElementStub> getElementStubConverter(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getElementStubConverter();
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
    GovernanceZoneHandler<GovernanceZoneElement> getGovernanceZoneHandler(String userId,
                                                                          String serverName,
                                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceZoneHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ExternalReferenceHandler<ExternalReferenceElement> getExternalReferencesHandler(String userId,
                                                                                    String serverName,
                                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getExternalReferencesHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceDefinitionHandler<GovernanceDefinitionElement> getGovernanceDefinitionHandler(String userId,
                                                                                            String serverName,
                                                                                            String serviceOperationName) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceDefinitionHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceDefinitionHandler<GovernanceDefinitionGraph> getGovernanceDefinitionGraphHandler(String userId,
                                                                                               String serverName,
                                                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                                                   UserNotAuthorizedException,
                                                                                                                                   PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceDefinitionGraphHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    LicenseHandler<LicenseTypeElement> getLicenseTypeHandler(String userId,
                                                             String serverName,
                                                             String serviceOperationName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getLicenseTypeHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    CertificationHandler<CertificationTypeElement> getCertificationTypeHandler(String userId,
                                                                               String serverName,
                                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getCertificationTypeHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceDomainHandler<GovernanceDomainElement> getGovernanceDomainHandler(String userId,
                                                                                String serverName,
                                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceDomainHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceMetricHandler<GovernanceMetricElement> getGovernanceMetricHandler(String userId,
                                                                                String serverName,
                                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceMetricHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceMetricHandler<GovernanceMetricImplementation> getGovernanceMetricImplementationHandler(String userId,
                                                                                                     String serverName,
                                                                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                                                                         UserNotAuthorizedException,
                                                                                                                                         PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceMetricImplementationHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    CollectionHandler<GovernanceDomainSetElement> getGovernanceDomainSetHandler(String userId,
                                                                                String serverName,
                                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceDomainSetHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    PersonRoleHandler<GovernanceRoleElement> getGovernanceRoleHandler(String userId,
                                                                      String serverName,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceRoleHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    AppointmentHandler getAppointmentHandler(String userId,
                                             String serverName,
                                             String serviceOperationName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getAppointmentHandler();
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
    SubjectAreaHandler<SubjectAreaElement> getSubjectAreaHandler(String userId,
                                                                 String serverName,
                                                                 String serviceOperationName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        GovernanceProgramServicesInstance instance = (GovernanceProgramServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getSubjectAreaHandler();
        }

        return null;
    }
}
