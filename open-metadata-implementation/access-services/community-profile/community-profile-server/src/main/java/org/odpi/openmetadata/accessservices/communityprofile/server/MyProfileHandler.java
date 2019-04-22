/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.NoProfileForUserException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * MyProfileHandler manages Person objects from the property server.  It runs server-side in the Community Profile
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
class MyProfileHandler
{
    private static final String userIdentityTypeGUID            = "fbe95779-1f3c-4ac6-aa9d-24963ff16282";
    private static final String userIdentityTypeName            = "UserIdentity";
    private static final String userIdPropertyName              = "qualifiedName";
    private static final String userIdentityProfileLinkTypeGUID = "01664609-e777-4079-b543-6baffe910ff1";

    private static final String personalDetailsTypeGUID         = "ac406bf8-e53e-49f1-9088-2af28bbbd285";
    private static final String personalDetailsTypeName         = "Person";
    private static final String qualifiedNamePropertyName       = "qualifiedName";
    private static final String fullNamePropertyName            = "fullName";
    private static final String knownNamePropertyName           = "name";
    private static final String jobTitlePropertyName            = "jobTitle";
    private static final String jobDescriptionPropertyName      = "description";
    private static final String karmaPointsPropertyName         = "karmaPoints";
    private static final String additionalPropertiesName        = "additionalProperties";

    private static final String qualifiedNameParameterName      = "qualifiedName";
    private static final String knownNameParameterName          = "knownName";

    private static final Logger log = LoggerFactory.getLogger(MyProfileHandler.class);

    private String                  serviceName;
    private OMRSRepositoryHelper    repositoryHelper    = null;
    private OMRSRepositoryValidator repositoryValidator = null;
    private String                  serverName          = null;
    private ErrorHandler            errorHandler        = null;

    /**
     * Construct the personal details handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    MyProfileHandler(String                  serviceName,
                     OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.repositoryValidator = repositoryConnector.getRepositoryValidator();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new ErrorHandler(repositoryConnector);
        }
    }


    /**
     * Create an instance properties object for a personal profile of an individual.
     *
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param karmaPoints points relating to the positive contribution made by the user.
     * @param profileProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     * @return Unique identifier for the personal profile.
     * @throws InvalidParameterException the employee number or known name is null.
     */
    private InstanceProperties createMyProfileProperties(String              methodName,
                                                         String              employeeNumber,
                                                         String              fullName,
                                                         String              knownName,
                                                         String              jobTitle,
                                                         String              jobRoleDescription,
                                                         int                 karmaPoints,
                                                         Map<String, Object> profileProperties,
                                                         Map<String, String> additionalProperties) throws InvalidParameterException
    {
        errorHandler.validateName(employeeNumber, qualifiedNameParameterName, methodName);
        errorHandler.validateName(knownName, knownNameParameterName, methodName);

        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  qualifiedNamePropertyName,
                                                                  employeeNumber,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  fullNamePropertyName,
                                                                  fullName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  knownNamePropertyName,
                                                                  knownName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  jobTitlePropertyName,
                                                                  jobTitle,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  jobDescriptionPropertyName,
                                                                  jobRoleDescription,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               karmaPointsPropertyName,
                                                               karmaPoints,
                                                               methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     additionalPropertiesName,
                                                                     additionalProperties,
                                                                     methodName);

        log.debug("Instance properties: " + properties.toString());

        return properties;
    }


    /**
     * Extract a profile from the returned Person entity.
     *
     * @param entity entity details object
     * @param methodName calling method
     * @return my profile object
     */
    private PersonalProfile getMyProfileFromEntity(EntityDetail    entity,
                                                   String          methodName)
    {
        PersonalProfile myProfile = null;

        if (entity != null)
        {
            myProfile = new PersonalProfile();

            myProfile.setGUID(entity.getGUID());

            InstanceType instanceType = entity.getType();
            if (instanceType != null)
            {
                myProfile.setTypeName(instanceType.getTypeDefName());
                myProfile.setTypeDescription(instanceType.getTypeDefDescription());
            }
            else
            {
                myProfile.setTypeName(personalDetailsTypeName);
            }

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * profile properties.
                 */
                myProfile.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, qualifiedNamePropertyName, instanceProperties, methodName));
                myProfile.setFullName(repositoryHelper.removeStringProperty(serviceName, fullNamePropertyName, instanceProperties, methodName));
                myProfile.setName(repositoryHelper.removeStringProperty(serviceName, knownNamePropertyName, instanceProperties, methodName));
                myProfile.setJobTitle(repositoryHelper.removeStringProperty(serviceName, jobTitlePropertyName, instanceProperties, methodName));
                myProfile.setDescription(repositoryHelper.removeStringProperty(serviceName, jobDescriptionPropertyName, instanceProperties, methodName));
                myProfile.setKarmaPoints(repositoryHelper.removeIntProperty(serviceName, karmaPointsPropertyName, instanceProperties, methodName));
                myProfile.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, additionalPropertiesName, instanceProperties, methodName));
                myProfile.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }

            log.debug("MyProfile: " + myProfile.toString());
        }
        else
        {
            log.debug("MyProfile: <null>");
        }

        return myProfile;
    }


    /**
     * Create a personal profile for the requesting individual.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param profileProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     * @throws InvalidParameterException the employee number or known name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    private void   createMyProfile(String              userId,
                                   String              employeeNumber,
                                   String              fullName,
                                   String              knownName,
                                   String              jobTitle,
                                   String              jobRoleDescription,
                                   Map<String, Object> profileProperties,
                                   Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String        methodName = "createMyProfile";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        InstanceProperties      properties = createMyProfileProperties(methodName,
                                                                       employeeNumber,
                                                                       fullName,
                                                                       knownName,
                                                                       jobTitle,
                                                                       jobRoleDescription,
                                                                       0,
                                                                       profileProperties,
                                                                       additionalProperties);

        try
        {
            EntityDetail personalDetailsEntity = metadataCollection.addEntity(userId,
                                                                              personalDetailsTypeGUID,
                                                                              properties,
                                                                              null,
                                                                              null);

            if (personalDetailsEntity != null)
            {
                log.debug("New personal details entity: " + personalDetailsEntity.getGUID());
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }
    }


    /**
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param employeeNumber personnel/serial/unique employee number of the individual. Used to verify the profileGUID.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param karmaPoints points relating to the positive contribution made by the user.
     * @param profileProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     * @throws InvalidParameterException the full name is null or the employeeNumber does not match the profileGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    private void updateMyProfile(String              userId,
                                 String              profileGUID,
                                 String              employeeNumber,
                                 String              fullName,
                                 String              knownName,
                                 String              jobTitle,
                                 String              jobRoleDescription,
                                 int                 karmaPoints,
                                 Map<String, Object> profileProperties,
                                 Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String        methodName = "updateMyProfile";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        InstanceProperties      properties = createMyProfileProperties(methodName,
                                                                       employeeNumber,
                                                                       fullName,
                                                                       knownName,
                                                                       jobTitle,
                                                                       jobRoleDescription,
                                                                       karmaPoints,
                                                                       profileProperties,
                                                                       additionalProperties);

        try
        {
            metadataCollection.updateEntityProperties(userId, profileGUID, properties);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        log.debug("Update of my profile successful: " + profileGUID);
    }


    /**
     * Return the UserIdentity entity for a UserId.
     *
     * @param userId userId of the user.
     * @param methodName calling method.
     *
     * @return unique identifier of the user identity or null if it does not exist.
     *
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String   getUserIdentity(String userId,
                                     String methodName) throws PropertyServerException,
                                                               UserNotAuthorizedException
    {
        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     userIdPropertyName,
                                                                                     userId,
                                                                                     methodName);

        try
        {
            List<EntityDetail>  findResults = metadataCollection.findEntitiesByProperty(userId,
                                                                                        userIdentityTypeGUID,
                                                                                        properties,
                                                                                        MatchCriteria.ANY,
                                                                                        0,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        5);

            repositoryValidator.validateAtMostOneEntityResult(findResults, userIdentityTypeName, serviceName, methodName);

            if (findResults == null)
            {
                return addUserIdentity(userId);
            }
            else
            {
                EntityDetail userIdentityEntity = findResults.get(0);

                if (userIdentityEntity != null)
                {
                    log.debug("User Identity entity found for: " +  userId);
                    return userIdentityEntity.getGUID();
                }
                else
                {
                    log.debug("Null User Identity entity found for: " +  userId);
                    return null;
                }
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        log.debug("Unreachable statement for: " +  userId);
        return null;
    }


    /**
     * Add the UserIdentity entity for a UserId.
     *
     * @param userId userId of the user.
     *
     * @return unique identifier of the user identity or null if it does not exist.
     *
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String   addUserIdentity(String userId) throws PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String        methodName = "addUserIdentity";

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     userIdPropertyName,
                                                                                     userId,
                                                                                     methodName);

        try
        {
            EntityDetail  userIdentity = metadataCollection.addEntity(userId,
                                                                      userIdentityTypeGUID,
                                                                      properties,
                                                                      null,
                                                                      null);

            if (userIdentity != null)
            {
                return userIdentity.getGUID();
            }
            else
            {
                CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.UNABLE_TO_CREATE_USER_IDENTITY;
                String                 errorMessage = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(userId);

                throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction());
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        log.debug("Unreachable statement for: " +  userId);
        return null;
    }


    /**
     * Return the profile of a user.  The userId is stored as a UserIdentity entity and linked to the profile
     * with a ProfileIdentity relationship.
     *
     * @param userId name of user.
     * @param methodName calling method.
     *
     * @return profile properties of the user.
     *
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private PersonalProfile   getProfile(String userId,
                                         String methodName) throws PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        String  userIdentityGUID = this.getUserIdentity(userId, methodName);

        if (userIdentityGUID != null)
        {
            OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

            try
            {
                List<Relationship> userProfiles = metadataCollection.getRelationshipsForEntity(userId,
                                                                                               userIdentityGUID,
                                                                                               userIdentityProfileLinkTypeGUID,
                                                                                               0,
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               0);

                repositoryValidator.validateAtMostOneRelationshipResult(userProfiles, userIdentityTypeName, serviceName, methodName);


                if ((userProfiles == null) || (userProfiles.isEmpty()))
                {
                    return null;
                }
                else
                {
                    /*
                     * There should only be profile for a user identity returned.  We have the relationship
                     * to it now, so just need to retrieve the entity that is stored at end 1.
                     */
                    return getMyProfileByGUID(userId,
                                              repositoryHelper.getEnd1EntityGUID(userProfiles.get(0)),
                                              methodName);
                }
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId,
                                                    methodName,
                                                    serverName,
                                                    serviceName);
            }
            catch (Throwable  error)
            {
                errorHandler.handleRepositoryError(error,
                                                   methodName,
                                                   serverName,
                                                   serviceName);
            }
        }

        return null;
    }


    /**
     * Retrieve the profile entity by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param methodName calling method.
     *
     * @return my profile object.
     *
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    private PersonalProfile getMyProfileByGUID(String        userId,
                                               String        profileGUID,
                                               String        methodName) throws PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            return this.getMyProfileFromEntity(metadataCollection.getEntityDetail(userId, profileGUID), methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        log.debug("Null return from method: " + methodName);
        return null;
    }


    /**
     * Add a Karma Point to the profile of the user that has been contributing metadata.
     *
     * @param userId userId of the user being rewarded.
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   addKarmaPoint(String userId) throws InvalidParameterException,
                                               PropertyServerException,
                                               UserNotAuthorizedException
    {
        final String methodName = "addKarmaPoint";

        PersonalProfile profile = getProfile(userId, methodName);

        if (profile != null)
        {
            int karmaPoints = profile.getKarmaPoints();
            karmaPoints++;

            profile.setKarmaPoints(karmaPoints);

            updateMyProfile(userId,
                            profile.getGUID(),
                            profile.getQualifiedName(),
                            profile.getFullName(),
                            profile.getName(),
                            profile.getJobTitle(),
                            profile.getDescription(),
                            profile.getKarmaPoints(),
                            profile.getExtendedProperties(),
                            profile.getAdditionalProperties());
        }
    }


    /**
     * Return the profile for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return profile object
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    PersonalProfile getMyProfile(String userId) throws InvalidParameterException,
                                                       NoProfileForUserException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String   methodName = "getMyProfile";

        errorHandler.validateUserId(userId, methodName);

        PersonalProfile  profile = getProfile(userId, methodName);

        if (profile == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NO_PROFILE_FOR_USER;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(userId);

            throw new NoProfileForUserException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                userId);
        }

        return profile;
    }


    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param profileProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void updateMyProfile(String              userId,
                         String              employeeNumber,
                         String              fullName,
                         String              knownName,
                         String              jobTitle,
                         String              jobRoleDescription,
                         Map<String, Object> profileProperties,
                         Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String   methodName = "updateMyProfile";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateName(userId, qualifiedNameParameterName, methodName);

        /*
         * The profile may already exist.
         */
        PersonalProfile  profile = getProfile(userId, methodName);

        if (profile == null)
        {
            createMyProfile(userId, employeeNumber, fullName, knownName, jobTitle, jobRoleDescription, profileProperties, additionalProperties);
        }
        else
        {
            int karmaPoints = profile.getKarmaPoints();
            karmaPoints ++;

            updateMyProfile(userId,
                            profile.getGUID(),
                            employeeNumber,
                            fullName,
                            knownName,
                            jobTitle,
                            jobRoleDescription,
                            karmaPoints,
                            profileProperties,
                            additionalProperties);
        }
    }
}
