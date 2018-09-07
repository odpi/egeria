/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfile;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * PersonalProfileHandler retrieves Person objects from the property server.  It runs server-side in the GovernanceProgram
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
class PersonalProfileHandler
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

    private String                        serviceName;
    private OMRSRepositoryHelper          repositoryHelper = null;
    private String                        serverName       = null;
    private GovernanceProgramErrorHandler errorHandler     = null;
    private GovernanceProgramBasicHandler basicHandler     = null;

    /**
     * Construct the personal details handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    PersonalProfileHandler(String                  serviceName,
                           OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new GovernanceProgramErrorHandler(repositoryConnector);
            basicHandler = new GovernanceProgramBasicHandler(serviceName, repositoryConnector);
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
                                                               Map<String, Object> additionalProperties) throws InvalidParameterException
    {
        errorHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);
        errorHandler.validateName(knownName, knownNameParameterName, methodName);

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

        properties = repositoryHelper.addMapPropertyToInstance(serviceName,
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
    PersonalProfile   getPersonalProfileFromEntity(EntityDetail    entity,
                                                   String          methodName)
    {
        PersonalProfile personalProfile = null;

        if (entity != null)
        {
            personalProfile = new PersonalProfile();

            personalProfile.setGUID(entity.getGUID());
            personalProfile.setType(personalDetailsTypeName);

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                personalProfile.setEmployeeNumber(repositoryHelper.getStringProperty(serviceName, employeeNumberPropertyName, instanceProperties, methodName));
                personalProfile.setFullName(repositoryHelper.getStringProperty(serviceName, fullNamePropertyName, instanceProperties, methodName));
                personalProfile.setKnownName(repositoryHelper.getStringProperty(serviceName, knownNamePropertyName, instanceProperties, methodName));
                personalProfile.setJobTitle(repositoryHelper.getStringProperty(serviceName, jobTitlePropertyName, instanceProperties, methodName));
                personalProfile.setJobRoleDescription(repositoryHelper.getStringProperty(serviceName, jobDescriptionPropertyName, instanceProperties, methodName));
                personalProfile.setAdditionalProperties(repositoryHelper.getMapFromProperty(serviceName, additionalPropertiesName, instanceProperties, methodName));
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
    String createPersonalProfile(String              userId,
                                 String              employeeNumber,
                                 String              fullName,
                                 String              knownName,
                                 String              jobTitle,
                                 String              jobRoleDescription,
                                 Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String        methodName = "createPersonalProfile";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        InstanceProperties      properties = createPersonalProfileProperties(methodName,
                                                                             employeeNumber,
                                                                             fullName,
                                                                             knownName,
                                                                             jobTitle,
                                                                             jobRoleDescription,
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

                return personalDetailsEntity.getGUID();
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

        log.debug("Null return from method: " + methodName);

        return null;
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
     * @throws UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid.
     * @throws InvalidParameterException the full name is null or the employeeNumber does not match the profileGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void   updatePersonalProfile(String              userId,
                                 String              profileGUID,
                                 String              employeeNumber,
                                 String              fullName,
                                 String              knownName,
                                 String              jobTitle,
                                 String              jobRoleDescription,
                                 Map<String, Object> additionalProperties) throws UnrecognizedGUIDException,
                                                                                  InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String        methodName = "updatePersonalProfile";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        InstanceProperties      properties = createPersonalProfileProperties(methodName,
                                                                             employeeNumber,
                                                                             fullName,
                                                                             knownName,
                                                                             jobTitle,
                                                                             jobRoleDescription,
                                                                             additionalProperties);

        try
        {
            metadataCollection.updateEntityProperties(userId,
                                                      profileGUID,
                                                      properties);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnrecognizedGUIDException(userId,
                                                         methodName,
                                                         serverName,
                                                         personalDetailsTypeName,
                                                         profileGUID);
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        log.debug("Update of personal details successful: " + profileGUID);
    }


    /**
     * Delete the personal profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @throws UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid.
     * @throws InvalidParameterException the employee number or full name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void   deletePersonalProfile(String              userId,
                                 String              profileGUID,
                                 String              employeeNumber) throws UnrecognizedGUIDException,
                                                                            InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String        methodName = "deletePersonalProfile";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(userId, profileGUIDParameterName, profileGUID, methodName);
        errorHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);

        PersonalProfile   personalProfile = getPersonalProfileByGUID(userId, profileGUID);

        if ((personalProfile != null) && (personalProfile.getEmployeeNumber().equals(employeeNumber)))
        {
            basicHandler.deleteEntity(userId, profileGUID, personalDetailsTypeGUID, personalDetailsTypeName, methodName);
        }
        else
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.PERSONAL_DETAILS_NOT_DELETED;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(profileGUID);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                employeeNumberParameterName);
        }

        log.debug("Delete of personal details successful: " + profileGUID);
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object.
     * @throws UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    PersonalProfile getPersonalProfileByGUID(String        userId,
                                             String        profileGUID) throws UnrecognizedGUIDException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByGUID";
        final String   guidParameter = "profileGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(profileGUID, guidParameter, personalDetailsTypeName, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            return this.getPersonalProfileFromEntity(metadataCollection.getEntityDetail(userId, profileGUID), methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnrecognizedGUIDException(userId,
                                                         methodName,
                                                         serverName,
                                                         personalDetailsTypeName,
                                                         profileGUID);
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
    PersonalProfile getPersonalProfileByEmployeeNumber(String         userId,
                                                       String         employeeNumber) throws InvalidParameterException,
                                                                                             EmployeeNumberNotUniqueException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByEmployeeNumber";
        final String   employeeNumberParameterName = "employeeNumber";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            InstanceProperties properties;

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      employeeNumberPropertyName,
                                                                      employeeNumber,
                                                                      methodName);

            List<EntityDetail> personalDetails = metadataCollection.findEntitiesByProperty(userId,
                                                                                           personalDetailsTypeGUID,
                                                                                           properties,
                                                                                           MatchCriteria.ANY,
                                                                                           0,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           2);

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
            else if (personalDetails.size() != 1)
            {
                GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.DUPLICATE_PERSONAL_DETAILS_FOR_EMP_ID;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(employeeNumber);

                throw new EmployeeNumberNotUniqueException(errorCode.getHTTPErrorCode(),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           errorMessage,
                                                           errorCode.getSystemAction(),
                                                           errorCode.getUserAction(),
                                                           personalDetails);
            }
            else
            {
                return this.getPersonalProfileFromEntity(personalDetails.get(0), methodName);
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (InvalidParameterException | EmployeeNumberNotUniqueException error)
        {
            throw error;
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        log.debug("Null return from method: " + methodName);

        return null;
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
    List<PersonalProfile> getPersonalProfilesByName(String        userId,
                                                    String        name) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfilesByName";
        final String   nameParameter = "name";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateName(name, nameParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
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

            List<EntityDetail> personalDetails = metadataCollection.findEntitiesByProperty(userId,
                                                                                           personalDetailsTypeGUID,
                                                                                           properties,
                                                                                           MatchCriteria.ANY,
                                                                                           0,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           2);

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
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (InvalidParameterException  error)
        {
            throw error;
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        log.debug("Null return from method: " + methodName);

        return null;
    }
}
