/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * CommunityProfileInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the CommunityProfileAdmin class.
 */
class CommunityProfileInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    CommunityProfileInstanceHandler()
    {
        super(AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName());

        CommunityProfileRegistration.registerAccessService();
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
    ReferenceableHandler<RelatedElement> getRelatedElementHandler(String userId,
                                                                  String serverName,
                                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getRelatedElementHandler();
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
    SoftwareCapabilityHandler<MetadataSourceElement> getMetadataSourceHandler(String userId,
                                                                              String serverName,
                                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getMetadataSourceHandler();
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
    ActorProfileHandler<ActorProfileElement> getActorProfileHandler(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getActorProfileHandler();
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
    ContributionRecordHandler<ContributionRecordElement> getContributionRecordHandler(String userId,
                                                                                      String serverName,
                                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getContributionRecordHandler();
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
    PersonRoleHandler<PersonRoleElement> getPersonRoleHandler(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getPersonRoleHandler();
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
    CommunityHandler<CommunityElement> getCommunityHandler(String userId,
                                                           String serverName,
                                                           String serviceOperationName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getCommunityHandler();
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
    ActorProfileHandler<PersonalProfileUniverse> getPersonalProfileHandler(String userId,
                                                                           String serverName,
                                                                           String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getPersonalProfileHandler();
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
    ContactDetailsHandler<ContactMethodElement> getContactDetailsHandler(String userId,
                                                                         String serverName,
                                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getContactDetailsHandler();
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
    UserIdentityHandler<UserIdentityElement> getUserIdentityHandler(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getUserIdentityHandler();
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
    LocationHandler<LocationElement> getLocationHandler(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getLocationHandler();
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
    GovernanceDefinitionHandler<SecurityGroupElement> getSecurityGroupHandler(String userId,
                                                                              String serverName,
                                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getSecurityGroupHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    CommentHandler<CommentElement> getCommentHandler(String userId,
                                                     String serverName,
                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {

        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getCommentHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    InformalTagHandler<InformalTagElement> getInformalTagHandler(String userId,
                                                                 String serverName,
                                                                 String serviceOperationName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getInformalTagHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    LikeHandler<LikeElement> getLikeHandler(String userId,
                                            String serverName,
                                            String serviceOperationName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getLikeHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    RatingHandler<RatingElement> getRatingHandler(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getRatingHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    ValidValuesHandler<ValidValueElement> getValidValueHandler(String userId,
                                                               String serverName,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getValidValuesHandler();
        }

        return null;
    }
}
