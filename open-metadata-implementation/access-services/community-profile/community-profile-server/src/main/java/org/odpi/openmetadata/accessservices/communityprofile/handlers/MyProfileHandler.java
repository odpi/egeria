/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.handlers;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.NoProfileForUserException;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * MyProfileHandler manages Person objects from the property server.  It runs server-side in the Community Profile
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class MyProfileHandler
{
    private static final String userIdentityTypeGUID            = "fbe95779-1f3c-4ac6-aa9d-24963ff16282";
    private static final String userIdentityTypeName            = "UserIdentity";
    private static final String userIdPropertyName              = "qualifiedName";
    private static final String userIdentityProfileLinkTypeGUID = "01664609-e777-4079-b543-6baffe910ff1";
    private static final String userIdentityProfileLinkTypeName = "ProfileIdentity";

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
    private String                  serverName;
    private RepositoryErrorHandler  errorHandler;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     * @param errorHandler handler for repository service errors
     */
    public MyProfileHandler(String                  serviceName,
                            String                  serverName,
                            InvalidParameterHandler invalidParameterHandler,
                            OMRSRepositoryHelper    repositoryHelper,
                            RepositoryHandler       repositoryHandler,
                            RepositoryErrorHandler  errorHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.errorHandler = errorHandler;
        this.repositoryHandler = repositoryHandler;
    }


    /**
     * Create an instance properties object for a personal profile of an individual.
     *
     * @param methodName calling method
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param karmaPoints points relating to the positive contribution made by the user.
     * @param extendedProperties  properties about the individual for a new type that is the subclass of Person.
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
                                                         Map<String, Object> extendedProperties,
                                                         Map<String, String> additionalProperties) throws InvalidParameterException
    {
        invalidParameterHandler.validateName(employeeNumber, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(knownName, knownNameParameterName, methodName);

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

        // todo need to add extended properties

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

        invalidParameterHandler.validateUserId(userId, methodName);

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
            String personalDetailsGUID = repositoryHandler.createEntity(userId,
                                                                        personalDetailsTypeGUID,
                                                                        personalDetailsTypeName,
                                                                        properties,
                                                                        methodName);

            if (personalDetailsGUID != null)
            {
                log.debug("New personal details entity: " + personalDetailsGUID);
            }
        }
        catch (UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error, methodName);
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

        invalidParameterHandler.validateUserId(userId, methodName);

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
            repositoryHandler.updateEntity(userId, profileGUID, personalDetailsTypeGUID, personalDetailsTypeName, properties, methodName);
        }
        catch (UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error, methodName);
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
        final String  nameParameter = "userId";

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     userIdPropertyName,
                                                                                     userId,
                                                                                     methodName);

        try
        {
            EntityDetail  userEntity = repositoryHandler.getUniqueEntityByName(userId,
                                                                                userId,
                                                                                nameParameter,
                                                                                properties,
                                                                                userIdentityTypeGUID,
                                                                                userIdentityTypeName,
                                                                                methodName);

            if (userEntity == null)
            {
                return addUserIdentity(userId);
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
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error, methodName);
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

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     userIdPropertyName,
                                                                                     userId,
                                                                                     methodName);

        try
        {
            String  userIdentityGUID = repositoryHandler.createEntity(userId,
                                                                      userIdentityTypeGUID,
                                                                      userIdentityTypeName,
                                                                      properties,
                                                                      methodName);

            if (userIdentityGUID != null)
            {
                return userIdentityGUID;
            }
            else
            {
                CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.UNABLE_TO_CREATE_USER_IDENTITY;
                String                    errorMessage = errorCode.getErrorMessageId()
                                                       + errorCode.getFormattedErrorMessage(userId);

                throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction());
            }
        }
        catch (PropertyServerException | UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error, methodName);
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
            try
            {
                EntityDetail profileEntity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                                            userIdentityGUID,
                                                                                            userIdentityTypeName,
                                                                                            userIdentityProfileLinkTypeGUID,
                                                                                            userIdentityProfileLinkTypeName,
                                                                                            methodName);



                if (profileEntity == null)
                {
                    return null;
                }
                else
                {
                    return this.getMyProfileFromEntity(profileEntity, methodName);

                }
            }
            catch (PropertyServerException | UserNotAuthorizedException error)
            {
                throw error;
            }
            catch (Throwable  error)
            {
                errorHandler.handleRepositoryError(error, methodName);
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
     * @throws InvalidParameterException null or invalid parameters.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    private PersonalProfile getMyProfileByGUID(String        userId,
                                               String        profileGUID,
                                               String        methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String guidParameterName = "profileGUID";

        try
        {
            return this.getMyProfileFromEntity(repositoryHandler.getEntityByGUID(userId, profileGUID, guidParameterName, personalDetailsTypeName, methodName), methodName);
        }
        catch (UserNotAuthorizedException | PropertyServerException | InvalidParameterException error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
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
    public PersonalProfile getMyProfile(String userId) throws InvalidParameterException,
                                                              NoProfileForUserException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String   methodName = "getMyProfile";

        invalidParameterHandler.validateUserId(userId, methodName);

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
     * @param extendedProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateMyProfile(String              userId,
                                String              employeeNumber,
                                String              fullName,
                                String              knownName,
                                String              jobTitle,
                                String              jobRoleDescription,
                                Map<String, Object> extendedProperties,
                                Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "updateMyProfile";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(userId, qualifiedNameParameterName, methodName);

        /*
         * The profile may already exist.
         */
        PersonalProfile  profile = getProfile(userId, methodName);

        if (profile == null)
        {
            createMyProfile(userId, employeeNumber, fullName, knownName, jobTitle, jobRoleDescription, extendedProperties, additionalProperties);
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
                            extendedProperties,
                            additionalProperties);
        }
    }
}
