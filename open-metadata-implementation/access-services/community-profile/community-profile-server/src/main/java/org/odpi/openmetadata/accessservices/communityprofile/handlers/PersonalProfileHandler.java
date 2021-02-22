/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.handlers;



import org.odpi.openmetadata.accessservices.communityprofile.builders.PersonalProfileBuilder;
import org.odpi.openmetadata.accessservices.communityprofile.converters.PersonalProfileConverter;
import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalProfileMapper;
import org.odpi.openmetadata.accessservices.communityprofile.mappers.UserIdentityMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethod;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * PersonalProfileHandler manages Person objects from the property server.  It runs server-side in the Community Profile
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class PersonalProfileHandler
{
    private static final Logger log = LoggerFactory.getLogger(PersonalProfileHandler.class);

    private String                  serviceName;
    private String                  serverName;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private UserIdentityHandler     userIdentityHandler;
    private ContactMethodHandler    contactMethodHandler;


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
    public PersonalProfileHandler(String                  serviceName,
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
        this.repositoryHandler = repositoryHandler;

        this.userIdentityHandler = new UserIdentityHandler(serviceName,
                                                           invalidParameterHandler,
                                                           repositoryHelper,
                                                           repositoryHandler,
                                                           errorHandler);

        this.contactMethodHandler = new ContactMethodHandler(serviceName,
                                                             serverName,
                                                             invalidParameterHandler,
                                                             repositoryHelper,
                                                             repositoryHandler,
                                                             errorHandler);
    }


    /**
     * Create a personal profile from an entity.
     *
     * @param userId calling userId
     * @param personalProfileEntity principle entity of the profile.
     * @param methodName calling method
     *
     * @return PersonalProfile bean or null if it does not exist.
     * @throws InvalidParameterException one of the userIds is null
     * @throws PropertyServerException the metadata repository is not available
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public PersonalProfile getPersonalProfile(String         userId,
                                              EntityDetail   personalProfileEntity,
                                              String         methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        List<UserIdentity> associatedUserIds = userIdentityHandler.getAssociatedUserIds(userId,
                                                                                        personalProfileEntity.getGUID(),
                                                                                        methodName);

        List<ContactMethod> contactMethods = contactMethodHandler.getContactMethods(userId,
                                                                                    personalProfileEntity.getGUID(),
                                                                                    methodName);

        PersonalProfileConverter converter = new PersonalProfileConverter(personalProfileEntity,
                                                                          associatedUserIds,
                                                                          contactMethods,
                                                                          repositoryHelper,
                                                                          serviceName);

        return converter.getBean();
    }


    /**
     * Retrieve a personal profile for a userId.
     *
     * @param userId calling userId
     * @param profileUserId userId associated with the profile.
     * @param methodName calling method
     *
     * @return PersonalProfile bean or null if it does not exist.
     * @throws InvalidParameterException one of the userIds is null
     * @throws PropertyServerException the metadata repository is not available
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public PersonalProfile getPersonalProfile(String   userId,
                                              String   profileUserId,
                                              String   methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String guidParameterName = "profileUserId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileUserId, guidParameterName, methodName);

        String userIdentityGUID = userIdentityHandler.getUserIdentityGUID(userId,
                                                                          profileUserId,
                                                                          methodName);

        if (userIdentityGUID != null)
        {
            EntityDetail personalProfileEntity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                                                userIdentityGUID,
                                                                                                UserIdentityMapper.USER_IDENTITY_TYPE_NAME,
                                                                                                PersonalProfileMapper.PROFILE_IDENTITY_TYPE_GUID,
                                                                                                PersonalProfileMapper.PROFILE_IDENTITY_TYPE_NAME,
                                                                                                methodName);

            if (personalProfileEntity != null)
            {
                return this.getPersonalProfile(userId, personalProfileEntity, methodName);
            }
        }

        return null;
    }


    /**
     * Create a personal profile for an individual.  This profile is linked to a UserIdentity entity.  If the
     * UserIdentity does not exist then it is created as well.
     *
     * @param userId the name of the calling user.
     * @param profileUserId userId of the individual whose profile this is.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     * @param methodName calling method
     *
     * @return Unique identifier for the personal profile.
     * @throws InvalidParameterException the qualified name or known name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public String createPersonalProfile(String              userId,
                                        String              profileUserId,
                                        String              employeeNumber,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, String> additionalProperties,
                                        String              methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        PersonalProfileBuilder builder = new PersonalProfileBuilder(employeeNumber,
                                                                    additionalProperties,
                                                                    null,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        InstanceProperties properties = builder.getPersonEntityProperties(knownName,
                                                                          fullName,
                                                                          jobTitle,
                                                                          jobRoleDescription);

        String profileGUID =  repositoryHandler.createEntity(userId,
                                                             PersonalProfileMapper.PERSONAL_PROFILE_TYPE_GUID,
                                                             PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
                                                             null,
                                                             null,
                                                             properties,
                                                             methodName);

        userIdentityHandler.addIdentityToProfile(userId, profileGUID, profileUserId, methodName);

        log.debug("Creation of personal details successful: " + profileGUID);

        return profileGUID;
    }


    /**
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param qualifiedName personnel/serial/unique employee number of the individual. Used to verify the profileGUID.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param extendedProperties  subtype properties.
     * @param additionalProperties  additional properties about the individual.
     * @param methodName calling method
     *
     * @throws InvalidParameterException the full name is null or the qualifiedName does not match the profileGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   updatePersonalProfile(String              userId,
                                        String              profileGUID,
                                        String              qualifiedName,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, Object> extendedProperties,
                                        Map<String, String> additionalProperties,
                                        String              methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        PersonalProfileBuilder builder = new PersonalProfileBuilder(qualifiedName,
                                                                    additionalProperties,
                                                                    extendedProperties,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        InstanceProperties      properties = builder.getPersonEntityProperties(knownName,
                                                                               fullName,
                                                                               jobTitle,
                                                                               jobRoleDescription);

        repositoryHandler.updateEntityProperties(userId,
                                                 null,
                                                 null,
                                                 profileGUID,
                                                 PersonalProfileMapper.PERSONAL_PROFILE_TYPE_GUID,
                                                 PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
                                                 properties,
                                                 methodName);

        log.debug("Update of personal details successful: " + profileGUID);
    }


    /**
     * Delete the personal profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @param methodName calling method
     *
     * @throws InvalidParameterException the employee number or full name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   deletePersonalProfile(String        userId,
                                        String        profileGUID,
                                        String        qualifiedName,
                                        String        methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String guidParameterName = "profileGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        repositoryHandler.removeEntity(userId,
                                       null,
                                       null,
                                       profileGUID,
                                       guidParameterName,
                                       PersonalProfileMapper.PERSONAL_PROFILE_TYPE_GUID,
                                       PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
                                       PersonalProfileMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                       qualifiedName,
                                       methodName);

        // todo remove all dependent entities - in switch to
        log.debug("Delete of personal details successful: " + profileGUID);
    }


    /**
     * Convert the returned entity into a PersonalProfile bean.  The entity is an anchor entity - so this method
     * retrieves contact details as well as the userIds associated with the PersonalProfile.
     *
     * @param userId calling user
     * @param entity personal profile entity
     * @param methodName calling method.
     *
     * @return PersonalProfile bean
     * @throws InvalidParameterException the unique identifier of the entity is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    private PersonalProfile  getPersonalProfileBean(String       userId,
                                                    EntityDetail entity,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        if (entity != null)
        {
            List<UserIdentity> associatedUserIds = userIdentityHandler.getAssociatedUserIds(userId, entity.getGUID(), methodName);
            List<ContactMethod> contactDetails = contactMethodHandler.getContactMethods(userId, entity.getGUID(), methodName);

            PersonalProfileConverter converter = new PersonalProfileConverter(entity,
                                                                              associatedUserIds,
                                                                              contactDetails,
                                                                              repositoryHelper,
                                                                              serviceName);

            return converter.getBean();
        }

        return null;
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param methodName calling method
     *
     * @return personal profile object.
     * @throws InvalidParameterException the unique identifier of the personal profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfile getPersonalProfileByGUID(String        userId,
                                                    String        profileGUID,
                                                    String        methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String   guidParameterName = "profileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);

        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                profileGUID,
                                                                guidParameterName,
                                                                PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
                                                                methodName);

        return this.getPersonalProfileBean(userId, entity, methodName);
    }


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param userId the name of the calling user.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @return personal profile object.
     *
     * @param methodName calling method
     * @throws InvalidParameterException userId is null; the qualified name is null or not unique.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfile getPersonalProfileByQualifiedName(String        userId,
                                                             String        qualifiedName,
                                                             String        methodName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String   employeeNumberParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, employeeNumberParameterName, methodName);

        PersonalProfileBuilder builder = new PersonalProfileBuilder(qualifiedName,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        InstanceProperties properties = builder.getQualifiedNameInstanceProperties(methodName);

        EntityDetail entity = repositoryHandler.getUniqueEntityByName(userId,
                                                                      qualifiedName,
                                                                      employeeNumberParameterName,
                                                                      properties,
                                                                      PersonalProfileMapper.PERSONAL_PROFILE_TYPE_GUID,
                                                                      PersonalProfileMapper.PERSONAL_PROFILE_TYPE_NAME,
                                                                      methodName);

        return this.getPersonalProfileBean(userId, entity, methodName);
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param methodName calling method
     *
     * @return list of personal profile objects.
     * @throws InvalidParameterException the name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<PersonalProfile> getPersonalProfilesByName(String        userId,
                                                           String        name,
                                                           int           startFrom,
                                                           int           pageSize,
                                                           String        methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String   nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);

        PersonalProfileBuilder builder = new PersonalProfileBuilder(null,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        InstanceProperties properties = builder.getNameInstanceProperties(methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesByName(userId,
                                                                          properties,
                                                                          PersonalProfileMapper.PERSONAL_PROFILE_TYPE_GUID,
                                                                          startFrom,
                                                                          pageSize,
                                                                          methodName);

        if (entities != null)
        {
            List<PersonalProfile>  personalProfiles = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    personalProfiles.add(this.getPersonalProfileBean(userId, entity, methodName));
                }
            }

            if (! personalProfiles.isEmpty())
            {
                return personalProfiles;
            }
        }

        return null;
    }
}
