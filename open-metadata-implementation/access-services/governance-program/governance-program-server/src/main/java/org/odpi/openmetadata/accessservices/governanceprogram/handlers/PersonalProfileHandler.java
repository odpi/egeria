/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.handlers;

import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfile;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * PersonalProfileHandler manages Person objects from the property server.  It runs server-side in the GovernanceProgram
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class PersonalProfileHandler
{
    private static final String personalDetailsTypeGUID     = "ac406bf8-e53e-49f1-9088-2af28bbbd285";
    private static final String personalDetailsTypeName     = "Person";
    private static final String employeeNumberPropertyName  = "qualifiedName";
    private static final String fullNamePropertyName        = "fullName";
    private static final String knownNamePropertyName       = "name";
    private static final String jobTitlePropertyName        = "jobTitle";
    private static final String jobDescriptionPropertyName  = "description";
    private static final String additionalPropertiesName    = "additionalProperties";

    private static final String profileGUIDParameterName    = "profileGUID";
    private static final String employeeNumberParameterName = "employeeNumber";
    private static final String knownNameParameterName      = "knownName";

    private static final Logger log = LoggerFactory.getLogger(PersonalProfileHandler.class);

    private String                  serviceName;
    private String                  serverName;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     */
    public PersonalProfileHandler(String                  serviceName,
                                  String                  serverName,
                                  InvalidParameterHandler invalidParameterHandler,
                                  OMRSRepositoryHelper    repositoryHelper,
                                  RepositoryHandler repositoryHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
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
     * @param additionalProperties  additional properties about the individual.
     * @return Unique identifier for the personal profile.
     * @throws InvalidParameterException the employee number or known name is null.
     */
    private InstanceProperties createPersonalProfileProperties(String              methodName,
                                                               String              employeeNumber,
                                                               String              fullName,
                                                               String              knownName,
                                                               String              jobTitle,
                                                               String              jobRoleDescription,
                                                               Map<String, String> additionalProperties) throws InvalidParameterException
    {
        invalidParameterHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);
        invalidParameterHandler.validateName(knownName, knownNameParameterName, methodName);

        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  employeeNumberPropertyName,
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

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     additionalPropertiesName,
                                                                     additionalProperties,
                                                                     methodName);

        log.debug("Instance properties: " + properties.toString());

        return properties;
    }


    /**
     * Extract a personal profile from the returned Person entity.
     *
     * @param entity entity details object
     * @param methodName calling method
     * @return personal profile object
     */
    public PersonalProfile   getPersonalProfileFromEntity(EntityDetail    entity,
                                                          String          methodName)
    {
        PersonalProfile personalProfile = null;

        if (entity != null)
        {
            personalProfile = new PersonalProfile();

            personalProfile.setGUID(entity.getGUID());

            InstanceType   instanceType = entity.getType();
            if (instanceType != null)
            {
                personalProfile.setType(instanceType.getTypeDefName());
            }
            else
            {
                personalProfile.setType(personalDetailsTypeName);
            }

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                personalProfile.setEmployeeNumber(repositoryHelper.getStringProperty(serviceName, employeeNumberPropertyName, instanceProperties, methodName));
                personalProfile.setFullName(repositoryHelper.getStringProperty(serviceName, fullNamePropertyName, instanceProperties, methodName));
                personalProfile.setKnownName(repositoryHelper.getStringProperty(serviceName, knownNamePropertyName, instanceProperties, methodName));
                personalProfile.setJobTitle(repositoryHelper.getStringProperty(serviceName, jobTitlePropertyName, instanceProperties, methodName));
                personalProfile.setJobRoleDescription(repositoryHelper.getStringProperty(serviceName, jobDescriptionPropertyName, instanceProperties, methodName));
                personalProfile.setAdditionalProperties(repositoryHelper.getStringMapFromProperty(serviceName, additionalPropertiesName, instanceProperties, methodName));
            }

            log.debug("Personal details: " + personalProfile.toString());
        }
        else
        {
            log.debug("Personal details: <null>");
        }

        return personalProfile;
    }


    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param userId the name of the calling user.
     * @param profileUserId userId of the individual whose profile this is.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     * @return Unique identifier for the personal profile.
     * @throws InvalidParameterException the employee number or known name is null.
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
                                        Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String        methodName = "createPersonalProfile";

        invalidParameterHandler.validateUserId(userId, methodName);

        InstanceProperties      properties = createPersonalProfileProperties(methodName,
                                                                             employeeNumber,
                                                                             fullName,
                                                                             knownName,
                                                                             jobTitle,
                                                                             jobRoleDescription,
                                                                             additionalProperties);

        return repositoryHandler.createEntity(userId,
                                              personalDetailsTypeGUID,
                                              personalDetailsTypeName,
                                              properties,
                                              methodName);
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
     * @param additionalProperties  additional properties about the individual.
     * @throws InvalidParameterException the full name is null or the employeeNumber does not match the profileGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   updatePersonalProfile(String              userId,
                                        String              profileGUID,
                                        String              employeeNumber,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String        methodName = "updatePersonalProfile";

        invalidParameterHandler.validateUserId(userId, methodName);

        InstanceProperties      properties = createPersonalProfileProperties(methodName,
                                                                             employeeNumber,
                                                                             fullName,
                                                                             knownName,
                                                                             jobTitle,
                                                                             jobRoleDescription,
                                                                             additionalProperties);

        repositoryHandler.updateEntity(userId,
                                       profileGUID,
                                       personalDetailsTypeGUID,
                                       personalDetailsTypeName,
                                       properties,
                                       methodName);

        log.debug("Update of personal details successful: " + profileGUID);
    }


    /**
     * Delete the personal profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @throws InvalidParameterException the employee number or full name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   deletePersonalProfile(String              userId,
                                        String              profileGUID,
                                        String              employeeNumber) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String        methodName = "deletePersonalProfile";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);

        repositoryHandler.deleteEntity(userId,
                                       profileGUID,
                                       personalDetailsTypeGUID,
                                       personalDetailsTypeName,
                                       employeeNumberPropertyName,
                                       employeeNumber,
                                       methodName);

        log.debug("Delete of personal details successful: " + profileGUID);
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object.
     * @throws InvalidParameterException the unique identifier of the personal profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfile getPersonalProfileByGUID(String        userId,
                                                    String        profileGUID) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByGUID";
        final String   guidParameter = "profileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameter, methodName);

        return this.getPersonalProfileFromEntity(repositoryHandler.getEntityByGUID(userId,
                                                                                   profileGUID,
                                                                                   guidParameter,
                                                                                   personalDetailsTypeName,
                                                                                   methodName),
                                                 methodName);
    }


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @return personal profile object.
     * @throws InvalidParameterException the employee number is null.
     * @throws EmployeeNumberNotUniqueException more than one personal profile was found.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfile getPersonalProfileByEmployeeNumber(String         userId,
                                                              String         employeeNumber) throws InvalidParameterException,
                                                                                                    EmployeeNumberNotUniqueException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByEmployeeNumber";
        final String   employeeNumberParameterName = "employeeNumber";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);

        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  employeeNumberPropertyName,
                                                                  employeeNumber,
                                                                  methodName);

        EntityDetail personalDetails = repositoryHandler.getUniqueEntityByName(userId,
                                                                               employeeNumber,
                                                                               employeeNumberParameterName,
                                                                               properties,
                                                                               personalDetailsTypeGUID,
                                                                               personalDetailsTypeName,
                                                                               methodName);

        if (personalDetails == null)
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.PERSONAL_DETAILS_NOT_FOUND_BY_EMP_ID;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(employeeNumber);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                employeeNumber);
        }
        else
        {
            return this.getPersonalProfileFromEntity(personalDetails, methodName);
        }
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @return list of personal profile objects.
     * @throws InvalidParameterException the name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<PersonalProfile> getPersonalProfilesByName(String        userId,
                                                           String        name) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfilesByName";
        final String   nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);

        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  fullNamePropertyName,
                                                                  name,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  knownNamePropertyName,
                                                                  name,
                                                                  methodName);

        List<EntityDetail> personalDetails = repositoryHandler.getEntitiesByName(userId,
                                                                                 properties,
                                                                                 personalDetailsTypeGUID,
                                                                                 0,
                                                                                 2,
                                                                                 methodName);

        if ((personalDetails == null) || (personalDetails.isEmpty()))
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.PERSONAL_DETAILS_NOT_FOUND_BY_NAME;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(name);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                name);
        }
        else
        {
            List<PersonalProfile>  resultsList = new ArrayList<>();

            for (EntityDetail   entity : personalDetails)
            {
                resultsList.add(this.getPersonalProfileFromEntity(entity, methodName));
            }

            return resultsList;
        }
    }
}
