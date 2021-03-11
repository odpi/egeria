/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.handlers;

import org.odpi.openmetadata.accessservices.communityprofile.builders.UserIdentityBuilder;
import org.odpi.openmetadata.accessservices.communityprofile.converters.UserIdentityConverter;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalProfileMapper;
import org.odpi.openmetadata.accessservices.communityprofile.mappers.UserIdentityMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * UserIdentityHandler is responsible for the management of UserIdentity type entities.
 */
public class UserIdentityHandler
{
    private String                  serviceName;
    private RepositoryErrorHandler  errorHandler;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;


    private static final Logger log = LoggerFactory.getLogger(UserIdentityHandler.class);


    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     * @param errorHandler handler for repository service errors
     */
    public UserIdentityHandler(String                  serviceName,
                               InvalidParameterHandler invalidParameterHandler,
                               OMRSRepositoryHelper    repositoryHelper,
                               RepositoryHandler       repositoryHandler,
                               RepositoryErrorHandler  errorHandler)
    {
        this.serviceName = serviceName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.errorHandler = errorHandler;
        this.repositoryHandler = repositoryHandler;
    }


    /**
     * Create a userIdentity from an entity.
     *
     * @param userIdentityEntity principle entity of the profile.
     *
     * @return UserIdentity bean or null if it does not exist.
     */
    public UserIdentity getUserIdentity(EntityDetail   userIdentityEntity)
    {
        UserIdentityConverter converter = new UserIdentityConverter(userIdentityEntity,
                                                                    repositoryHelper,
                                                                    serviceName);

        return converter.getBean();
    }



    /**
     * Return the UserIdentity entity GUID.
     *
     * @param userId userId of the calling user.
     * @param userIdentityQualifiedName userId of the UserIdentity entity.
     * @param methodName calling method.
     *
     * @return unique identifier of the user identity or null if it does not exist.
     *
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String getUserIdentityGUID(String userId,
                               String userIdentityQualifiedName,
                               String methodName) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String  nameParameter = "userIdentityQualifiedName";
        final String  localMethodName = "getUserIdentityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(userIdentityQualifiedName, nameParameter, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     UserIdentityMapper.USER_ID_PROPERTY_NAME,
                                                                                     userId,
                                                                                     methodName);

        try
        {
            EntityDetail userEntity = repositoryHandler.getUniqueEntityByName(userId,
                                                                              userIdentityQualifiedName,
                                                                              nameParameter,
                                                                              properties,
                                                                              UserIdentityMapper.USER_IDENTITY_TYPE_GUID,
                                                                              UserIdentityMapper.USER_IDENTITY_TYPE_NAME,
                                                                              methodName);

            if (userEntity == null)
            {
                return addUserIdentity(userId, userIdentityQualifiedName, methodName);
            }
            else
            {
                return userEntity.getGUID();
            }
        }
        catch (PropertyServerException | UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Exception  error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        log.debug("Unreachable statement for: " +  userId);
        return null;
    }


    /**
     * Add the UserIdentity entity for a PersonalProfile.
     *
     * @param userId userId of the calling user.
     * @param profileUserId userId of the profile owner.
     * @param methodName calling method.
     *
     * @return unique identifier of the user identity or null if it does not exist.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String   addUserIdentity(String userId,
                                    String profileUserId,
                                    String methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String  nameParameter = "profileUserId";
        final String  localMethodName = "addUserIdentity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(profileUserId, nameParameter, methodName);

        UserIdentityBuilder builder = new UserIdentityBuilder(profileUserId, repositoryHelper, serviceName);

        InstanceProperties properties = builder.getEntityProperties();

        try
        {
            String  userIdentityGUID = repositoryHandler.createEntity(userId,
                                                                      UserIdentityMapper.USER_IDENTITY_TYPE_GUID,
                                                                      UserIdentityMapper.USER_IDENTITY_TYPE_NAME,
                                                                      null,
                                                                      null,
                                                                      properties,
                                                                      methodName);

            if (userIdentityGUID != null)
            {
                return userIdentityGUID;
            }
            else
            {
               throw new PropertyServerException(CommunityProfileErrorCode.UNABLE_TO_CREATE_USER_IDENTITY.getMessageDefinition(userId),
                                                 this.getClass().getName(),
                                                 methodName);
            }
        }
        catch (PropertyServerException | UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Exception  error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        log.debug("Unreachable statement for: " +  userId);
        return null;
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID the profile to add the identity to.
     * @param newIdentity additional userId for the profile.
     * @param methodName calling method.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void      addIdentityToProfile(String              userId,
                                          String              profileGUID,
                                          String              newIdentity,
                                          String              methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String  nameParameter = "newIdentity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(newIdentity, nameParameter, methodName);

        String userIdentityGUID = this.getUserIdentityGUID(userId, newIdentity, methodName);

        if (userIdentityGUID == null)
        {
            userIdentityGUID = this.addUserIdentity(userId, newIdentity, methodName);
        }

        repositoryHandler.createRelationship(userId,
                                             PersonalProfileMapper.PROFILE_IDENTITY_TYPE_GUID,
                                             null,
                                             null,
                                             profileGUID,
                                             userIdentityGUID,
                                             null,
                                             methodName);
    }


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
     *
     * @param userId the name of the calling user.
     * @param profileGUID profile to remove it from.
     * @param obsoleteIdentity user identity to remove.
     * @param methodName calling method.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void      removeIdentityFromProfile(String              userId,
                                               String              profileGUID,
                                               String              obsoleteIdentity,
                                               String              methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String  nameParameter = "obsoleteIdentity";
        final String  guidParameter = "profileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameter, methodName);
        invalidParameterHandler.validateName(obsoleteIdentity, nameParameter, methodName);

        List<UserIdentity>  associatedUserIds = this.getAssociatedUserIds(userId, profileGUID, methodName);

        if (associatedUserIds != null)
        {
            String  userIdentityGUID = null;
            boolean anotherIdentity = false;

            for (UserIdentity userIdentity : associatedUserIds)
            {
                if (userIdentity != null)
                {
                    if (obsoleteIdentity.equals(userIdentity.getUserId()))
                    {
                        userIdentityGUID = userIdentity.getGUID();
                    }
                    else
                    {
                        anotherIdentity = true;
                    }
                }
            }

            if (userIdentityGUID != null)
            {
                if (anotherIdentity)
                {
                    repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                                        null,
                                                                        null,
                                                                        PersonalProfileMapper.PROFILE_IDENTITY_TYPE_GUID,
                                                                        PersonalProfileMapper.PROFILE_IDENTITY_TYPE_NAME,
                                                                        profileGUID,
                                                                        PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
                                                                        userIdentityGUID,
                                                                        methodName);
                }
                else
                {
                    throw new InvalidParameterException(CommunityProfileErrorCode.NO_OTHER_IDENTITY.getMessageDefinition(obsoleteIdentity, profileGUID),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        nameParameter);
                }
            }
            else
            {
                throw new InvalidParameterException(CommunityProfileErrorCode.UNKNOWN_IDENTITY.getMessageDefinition(obsoleteIdentity, profileGUID),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    nameParameter);
            }
        }
        else
        {
            throw new PropertyServerException(CommunityProfileErrorCode.NO_IDENTITY_FOR_PROFILE.getMessageDefinition(profileGUID),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Remove a user identity object.  This will fail if a profile would be left without an
     * associated user identity.
     *
     * @param userId the name of the calling user.
     * @param obsoleteIdentity user identity to remove.
     * @param methodName calling method.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void      removeUserIdentity(String              userId,
                                        String              obsoleteIdentity,
                                        String              methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String guidParameterName = "profileGUID";
        final String nameParameter = "userIdentityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(obsoleteIdentity, nameParameter, methodName);

        String userIdentityGUID = this.getUserIdentityGUID(userId, obsoleteIdentity, methodName);

        if (userIdentityGUID != null)
        {
            EntityDetail connectedProfileEntity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                                                 userIdentityGUID,
                                                                                                 UserIdentityMapper.USER_IDENTITY_TYPE_NAME,
                                                                                                 PersonalProfileMapper.PROFILE_IDENTITY_TYPE_GUID,
                                                                                                 PersonalProfileMapper.PROFILE_IDENTITY_TYPE_NAME,
                                                                                                 methodName);

            if (connectedProfileEntity != null)
            {
                this.removeIdentityFromProfile(userId, connectedProfileEntity.getGUID(), obsoleteIdentity, methodName);
            }

            repositoryHandler.removeEntity(userId,
                                           null,
                                           null,
                                           userIdentityGUID,
                                           guidParameterName,
                                           UserIdentityMapper.USER_IDENTITY_TYPE_GUID,
                                           UserIdentityMapper.USER_IDENTITY_TYPE_NAME,
                                           nameParameter,
                                           obsoleteIdentity,
                                           methodName);
        }
    }


    /**
     * Return all of the user identities associated with the identified profile.
     *
     * @param userId calling user
     * @param profileGUID unique identifier of the profile
     * @param methodName calling method.
     *
     * @return list of user identity beans
     * @throws InvalidParameterException the userId of the guid is null
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<UserIdentity>  getAssociatedUserIds(String   userId,
                                             String   profileGUID,
                                             String   methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String guidParameterName = "profileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);

        List<Relationship> userIdentityRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                profileGUID,
                                                                                                PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
                                                                                                PersonalProfileMapper.PROFILE_IDENTITY_TYPE_GUID,
                                                                                                PersonalProfileMapper.PROFILE_IDENTITY_TYPE_NAME,
                                                                                                methodName);
        if (userIdentityRelationships != null)
        {
            List<UserIdentity>  userIdentities = new ArrayList<>();

            for (Relationship relationship : userIdentityRelationships)
            {
                if (relationship != null)
                {
                    EntityProxy entityProxy = relationship.getEntityTwoProxy();

                    if (entityProxy != null)
                    {
                        final String entityProxyName = "relationship.end2.entityProxy";

                        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                entityProxy.getGUID(),
                                                                                entityProxyName,
                                                                                UserIdentityMapper.USER_IDENTITY_TYPE_NAME,
                                                                                methodName);
                        UserIdentityConverter converter = new UserIdentityConverter(entity,
                                                                                    repositoryHelper,
                                                                                    serviceName);

                        UserIdentity bean = converter.getBean();

                        if (bean != null)
                        {
                            userIdentities.add(bean);
                        }
                    }
                }
            }

            if (userIdentities.isEmpty())
            {
                throw new PropertyServerException(CommunityProfileErrorCode.NO_IDENTITY_FOR_PROFILE.getMessageDefinition(profileGUID),
                                                  this.getClass().getName(),
                                                  methodName);
            }
            else
            {
                return userIdentities;
            }
        }

        return null;
    }
}
