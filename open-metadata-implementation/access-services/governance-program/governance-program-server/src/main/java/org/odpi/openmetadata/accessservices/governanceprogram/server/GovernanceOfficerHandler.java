/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * GovernanceOfficerHandler retrieves GovernanceOfficer objects from the property server and maintains the
 * relationship between the governance officer and the profile of the individual that is appointed as the
 * governance officer.  It runs server-side in the GovernanceProgram
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
class GovernanceOfficerHandler
{
    /*
     * Mapping of Governance Program API property names to open metadata types
     */
    private static final String governanceOfficerTypeGUID               = "578a3510-9ad3-45fe-8ada-e4e9572c37c8";
    private static final String governanceOfficerTypeName               = "GovernanceOfficer";
    private static final String appointmentIdPropertyName               = "qualifiedName";
    private static final String titlePropertyName                       = "title";
    private static final String appointmentContextPropertyName          = "scope";
    private static final String governanceDomainPropertyName            = "domain";
    private static final String additionalPropertiesName                = "additionalProperties";

    private static final String governancePostTypeGUID                  = "4c4d1d0c-a9fc-4305-8b71-4e691c0f9ae0";
    private static final String governancePostTypeName                  = "GovernancePost";

    private static final String personalProfileTypeName                 = "Person";


    /*
     * Parameter names used for error messages
     */
    private static final String governanceOfficerGUIDParameterName = "governanceOfficerGUID";
    private static final String governanceDomainParameterName      = "governanceDomain";
    private static final String appointmentIdParameterName         = "appointmentId";
    private static final String titleParameterName                 = "title";
    private static final String profileGUIDParameterName           = "profileGUID";

    private static final Logger log = LoggerFactory.getLogger(GovernanceOfficerHandler.class);


    private String                        serviceName;

    private GovernanceProgramErrorHandler errorHandler;
    private GovernanceProgramBasicHandler basicHandler;
    private ExternalReferencesHandler     externalReferencesHandler;
    private PersonalProfileHandler        personalProfileHandler;

    private OMRSRepositoryHelper          repositoryHelper            = null;
    private String                        serverName                  = null;
    private GovernanceProgramEnumHandler  enumHandler                 = null;



    /**
     * Construct the governance officer handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    GovernanceOfficerHandler(String                  serviceName,
                             OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;

        errorHandler = new GovernanceProgramErrorHandler(repositoryConnector);
        basicHandler = new GovernanceProgramBasicHandler(serviceName, repositoryConnector);
        externalReferencesHandler = new ExternalReferencesHandler(serviceName, repositoryConnector);
        personalProfileHandler = new PersonalProfileHandler(serviceName, repositoryConnector);

        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();

            enumHandler = new GovernanceProgramEnumHandler(serviceName, repositoryHelper);
        }
    }


    /**
     * Create the properties for storing a governance officer entity.
     *
     * @param methodName the name of the calling method.
     * @param governanceDomain  the governance domain for the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param appointmentContext  the context in which the governance officer is appointed.
     *                            This may be an organizational scope, location, or scope of assets.
     * @param title job title for the governance officer
     * @param additionalProperties additional properties for the governance officer.
     * @return instance properties object
     * @throws InvalidParameterException the governance domain, title or appointment id is null.
     */
    private InstanceProperties  createGovernanceOfficerProperties(String               methodName,
                                                                  GovernanceDomain     governanceDomain,
                                                                  String               appointmentId,
                                                                  String               appointmentContext,
                                                                  String               title,
                                                                  Map<String, Object>  additionalProperties) throws InvalidParameterException
    {
        errorHandler.validateEnum(governanceDomain, governanceDomainParameterName, methodName);
        errorHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        errorHandler.validateName(title, titleParameterName, methodName);

        InstanceProperties properties;

        properties = enumHandler.addGovernanceDomainToProperties(null,
                                                                 governanceDomain,
                                                                 governanceDomainPropertyName,
                                                                 methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  appointmentIdPropertyName,
                                                                  appointmentId,
                                                                  methodName);

        if (appointmentContext != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      appointmentContextPropertyName,
                                                                      appointmentContext,
                                                                      methodName);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  titlePropertyName,
                                                                  title,
                                                                  methodName);

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                   properties,
                                                                   additionalPropertiesName,
                                                                   additionalProperties,
                                                                   methodName);
        }

        log.debug("Instance properties: " + properties.toString());

        return properties;
    }





    /**
     * Extract a governance officer bean from the returned GovernanceOfficer entity and any
     * GovernancePost relationship to Person entity.  The assumption is that this is a 0..1
     * relationship so one entity (or null) is returned.  If lots of relationships are found then the
     * PropertyServerException is thrown.
     *
     * @param governanceOfficerEntity entity for person appointed to governance officer role - may be null
     * @param userId - user making the request.
     * @param metadataCollection - metadata collection to retrieve instances from
     * @param methodName - name of calling method
     * @return governance officer or null
     * @throws PropertyServerException problem access the property server
     */
    private GovernanceOfficer   getGovernanceOfficerFromInstances(EntityDetail            governanceOfficerEntity,
                                                                  String                  userId,
                                                                  OMRSMetadataCollection  metadataCollection,
                                                                  String                  methodName) throws PropertyServerException
    {
        GovernanceOfficer governanceOfficer = null;
        long              now = new Date().getTime();

        if (governanceOfficerEntity != null)
        {
            governanceOfficer = new GovernanceOfficer();

            governanceOfficer.setGUID(governanceOfficerEntity.getGUID());
            governanceOfficer.setType(governanceOfficerTypeName);

            InstanceProperties instanceProperties = governanceOfficerEntity.getProperties();

            if (instanceProperties != null)
            {
                governanceOfficer.setAppointmentId(repositoryHelper.getStringProperty(serviceName, appointmentIdPropertyName, instanceProperties, methodName));
                governanceOfficer.setAppointmentContext(repositoryHelper.getStringProperty(serviceName, appointmentContextPropertyName, instanceProperties, methodName));
                governanceOfficer.setTitle(repositoryHelper.getStringProperty(serviceName, titlePropertyName, instanceProperties, methodName));
                governanceOfficer.setGovernanceDomain(enumHandler.getGovernanceDomainFromProperties(instanceProperties, governanceDomainPropertyName, methodName));
                governanceOfficer.setAdditionalProperties(repositoryHelper.getMapFromProperty(serviceName, additionalPropertiesName, instanceProperties, methodName));
            }

            try
            {
                List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                                governanceOfficerEntity.getGUID(),
                                                                                                governancePostTypeGUID,
                                                                                                0,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                100);

                if (relationships != null)
                {
                    List<GovernanceOfficerAppointee>  currentIncumbents = this.getCurrentIncumbents(relationships, now, userId);

                    if (currentIncumbents == null)
                    {
                        governanceOfficer.setAppointee(null);
                    }
                    else if (currentIncumbents.size() == 1)
                    {
                        governanceOfficer.setAppointee(currentIncumbents.get(0));
                    }
                    else
                    {
                        GovernanceDomain   governanceDomain = governanceOfficer.getGovernanceDomain();

                        if (governanceDomain == null)
                        {
                            governanceDomain = GovernanceDomain.OTHER;
                        }
                        GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.MULTIPLE_INCUMBENTS_FOR_GOVERNANCE_OFFICER;
                        String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(governanceDomain.getName(),
                                                                                                                                     governanceOfficer.getGUID(),
                                                                                                                                     governanceOfficer.getAppointmentId(),
                                                                                                                                     governanceOfficer.getAppointmentContext(),
                                                                                                                                     Integer.toString(currentIncumbents.size()));

                        throw new GovernanceAppointeeNotUniqueException(errorCode.getHTTPErrorCode(),
                                                                        this.getClass().getName(),
                                                                        methodName,
                                                                        errorMessage,
                                                                        errorCode.getSystemAction(),
                                                                        errorCode.getUserAction(),
                                                                        currentIncumbents);
                    }
                    governanceOfficer.setPredecessors(this.getPredecessors(relationships, now, userId));
                    governanceOfficer.setSuccessors(this.getSuccessors(relationships, now, userId));
                }
            }
            catch (Throwable   error)
            {
                errorHandler.handleRepositoryError(error,
                                                   methodName,
                                                   serverName,
                                                   serviceName);
            }

            log.debug("Retrieved governance officer: " + governanceOfficer.toString());
        }
        else
        {
            log.debug("Null governance officer entity");
        }


        return governanceOfficer;
    }


    /**
     * Return the governance appointee information for the individual identified by the personalProfileGUID.
     *
     * @param governancePostGUID unique identifier of the post
     * @param personalProfileGUID unique identifier for the individual's profile
     * @param userId calling user
     * @param startDate start date of the appointment
     * @param endDate end date of the appointment
     * @return governance officer appointment
     * @throws UnrecognizedGUIDException GUID is not known
     * @throws PropertyServerException server is having trouble
     * @throws UserNotAuthorizedException user is not authorized
     */
    private GovernanceOfficerAppointee   getAppointee(String     governancePostGUID,
                                                      String     personalProfileGUID,
                                                      String     userId,
                                                      Date       startDate,
                                                      Date       endDate) throws UnrecognizedGUIDException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        PersonalProfile personalProfile = personalProfileHandler.getPersonalProfileByGUID(userId, personalProfileGUID);
        GovernanceOfficerAppointee  appointee = new GovernanceOfficerAppointee();

        appointee.setGUID(governancePostGUID);
        appointee.setType(governancePostTypeName);
        appointee.setProfile(personalProfile);
        appointee.setStartDate(startDate);
        appointee.setEndDate(endDate);

        return appointee;
    }


    /**
     * Return the list of individuals that used to be appointed to this governance officer
     * post along with the dates of their appointment.
     *
     * @param governancePostings list of postings for the governance officer.
     * @param now current time in milli-secs
     * @param userId calling user
     * @return list of appointees
     * @throws UnrecognizedGUIDException GUID is not known
     * @throws PropertyServerException server is having trouble
     * @throws UserNotAuthorizedException user is not authorized
     */
    private List<GovernanceOfficerAppointee>   getPredecessors(List<Relationship>   governancePostings,
                                                               long                 now,
                                                               String               userId) throws UnrecognizedGUIDException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        List<GovernanceOfficerAppointee>  appointees = new ArrayList<>();

        if (governancePostings != null)
        {
            for (Relationship   governancePost : governancePostings)
            {
                if (governancePost != null)
                {
                    Date                endDate = null;
                    Date                startDate = null;
                    InstanceProperties  properties = governancePost.getProperties();

                    if (properties != null)
                    {
                        endDate = properties.getEffectiveToTime();
                        startDate = properties.getEffectiveFromTime();
                    }

                    if ((endDate != null) && (endDate.getTime() < now))
                    {
                        String   personalProfileGUID = null;

                        if (governancePost.getEntityTwoProxy() != null)
                        {
                            personalProfileGUID = governancePost.getEntityTwoProxy().getGUID();
                        }


                        appointees.add(this.getAppointee(governancePost.getGUID(),
                                                         personalProfileGUID,
                                                         userId,
                                                         startDate,
                                                         endDate));
                    }
                }
            }
        }

        if (appointees.isEmpty())
        {
            return null;
        }
        else
        {
            return appointees;
        }
    }

    /**
     * Return the list of individuals that are currently appointed to this governance officer
     * post along with the dates of their appointment.  If all is well, there should be only one.
     *
     * @param governancePostings list of postings for the governance officer.
     * @param now current time in milli-secs
     * @param userId calling user
     * @return list of appointees
     * @throws UnrecognizedGUIDException GUID is not known
     * @throws PropertyServerException server is having trouble
     * @throws UserNotAuthorizedException user is not authorized
     */
    private List<GovernanceOfficerAppointee> getCurrentIncumbents(List<Relationship>   governancePostings,
                                                                  long                 now,
                                                                  String               userId) throws UnrecognizedGUIDException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        List<GovernanceOfficerAppointee>  appointees = new ArrayList<>();

        if (governancePostings != null)
        {
            for (Relationship   governancePost : governancePostings)
            {
                if (governancePost != null)
                {
                    Date                endDate = null;
                    Date                startDate = null;
                    InstanceProperties  properties = governancePost.getProperties();

                    if (properties != null)
                    {
                        endDate = properties.getEffectiveToTime();
                        startDate = properties.getEffectiveFromTime();
                    }

                    if (((startDate == null) || (startDate.getTime() <= now)) && ((endDate == null) || (endDate.getTime() >= now)))
                    {
                        String   personalProfileGUID = null;

                        if (governancePost.getEntityTwoProxy() != null)
                        {
                            personalProfileGUID = governancePost.getEntityTwoProxy().getGUID();
                        }

                        appointees.add(this.getAppointee(governancePost.getGUID(),
                                                         personalProfileGUID,
                                                         userId,
                                                         startDate,
                                                         endDate));
                    }
                }
            }
        }

        if (appointees.isEmpty())
        {
            return null;
        }
        else
        {
            return appointees;
        }
    }


    /**
     * Return the list of individuals that are will be appointed to this governance officer
     * post in the future along with the dates of their appointment.
     *
     * @param governancePostings list of postings for the governance officer.
     * @param now current time in milli-secs
     * @param userId calling user
     * @return list of appointees
     * @throws UnrecognizedGUIDException GUID is not known
     * @throws PropertyServerException server is having trouble
     * @throws UserNotAuthorizedException user is not authorized
     */
    private List<GovernanceOfficerAppointee>   getSuccessors(List<Relationship>   governancePostings,
                                                             long                 now,
                                                             String               userId) throws UnrecognizedGUIDException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        List<GovernanceOfficerAppointee>  appointees = new ArrayList<>();

        if (governancePostings != null)
        {
            for (Relationship   governancePost : governancePostings)
            {
                if (governancePost != null)
                {
                    Date                endDate = null;
                    Date                startDate = null;
                    InstanceProperties  properties = governancePost.getProperties();

                    if (properties != null)
                    {
                        endDate = properties.getEffectiveToTime();
                        startDate = properties.getEffectiveFromTime();
                    }

                    if (((startDate != null) && (startDate.getTime() > now)))
                    {
                        String   personalProfileGUID = null;

                        if (governancePost.getEntityTwoProxy() != null)
                        {
                            personalProfileGUID = governancePost.getEntityTwoProxy().getGUID();
                        }

                        appointees.add(this.getAppointee(governancePost.getGUID(),
                                                         personalProfileGUID,
                                                         userId,
                                                         startDate,
                                                         endDate));
                    }
                }
            }
        }

        if (appointees.isEmpty())
        {
            return null;
        }
        else
        {
            return appointees;
        }
    }


    /**
     * Create the governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceDomain  the governance domain for the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param appointmentContext  the context in which the governance officer is appointed.
     *                            This may be an organizational scope, location, or scope of assets.
     * @param title job title for the governance officer
     * @param additionalProperties additional properties for the governance officer.
     * @param externalReferences links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @return Unique identifier (guid) of the governance officer.
     * @throws InvalidParameterException the governance domain, title or appointment id is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    String createGovernanceOfficer(String                     userId,
                                   GovernanceDomain           governanceDomain,
                                   String                     appointmentId,
                                   String                     appointmentContext,
                                   String                     title,
                                   Map<String, Object>        additionalProperties,
                                   List<ExternalReference>    externalReferences) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String        methodName = "createGovernanceOfficer";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        InstanceProperties      properties = createGovernanceOfficerProperties(methodName,
                                                                               governanceDomain,
                                                                               appointmentId,
                                                                               appointmentContext,
                                                                               title,
                                                                               additionalProperties);

        EntityDetail governanceOfficer;
        String governanceOfficerGUID = null;

        try
        {
            governanceOfficer = metadataCollection.addEntity(userId,
                                                             governanceOfficerTypeGUID,
                                                             properties,
                                                             null,
                                                             null);

            if (governanceOfficer != null)
            {
                governanceOfficerGUID = governanceOfficer.getGUID();

                log.debug("New governance officer entity: " + governanceOfficerGUID);

                if (externalReferences != null)
                {
                    externalReferencesHandler.storeExternalReferences(userId, governanceOfficerGUID, externalReferences);
                }
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

        return governanceOfficerGUID;
    }


    /**
     * Update selected fields for the governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param governanceDomain  the governance domain for the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param appointmentContext  the context in which the governance officer is appointed.
     *                            This may be an organizational scope, location, or scope of assets.
     * @param title job title for the governance officer
     * @param additionalProperties additional properties for the governance officer.
     * @param externalReferences links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid.
     * @throws InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                                   the existing values associated with the governanceOfficerGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void   updateGovernanceOfficer(String                     userId,
                                   String                     governanceOfficerGUID,
                                   GovernanceDomain           governanceDomain,
                                   String                     appointmentId,
                                   String                     appointmentContext,
                                   String                     title,
                                   Map<String, Object>        additionalProperties,
                                   List<ExternalReference>    externalReferences) throws UnrecognizedGUIDException,
                                                                                         InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String        methodName = "updateGovernanceOfficer";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, governanceOfficerTypeName, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        InstanceProperties      properties = createGovernanceOfficerProperties(methodName,
                                                                               governanceDomain,
                                                                               appointmentId,
                                                                               appointmentContext,
                                                                               title,
                                                                               additionalProperties);

        try
        {
            metadataCollection.updateEntityProperties(userId,
                                                      governanceOfficerGUID,
                                                      properties);

            externalReferencesHandler.storeExternalReferences(userId, governanceOfficerGUID, externalReferences);
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
                                                         governanceOfficerTypeName,
                                                         governanceOfficerGUID);
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        log.debug("Update of governance officer successful: " + governanceOfficerGUID);
    }


    /**
     * Remove the requested governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param governanceDomain  the governance domain for the governance officer.
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid.
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void   deleteGovernanceOfficer(String              userId,
                                   String              governanceOfficerGUID,
                                   String              appointmentId,
                                   GovernanceDomain    governanceDomain) throws UnrecognizedGUIDException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String        methodName = "deleteGovernanceOfficer";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(userId, governanceOfficerGUIDParameterName, governanceOfficerGUID, methodName);
        errorHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        errorHandler.validateEnum(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficer   governanceOfficer = getGovernanceOfficerByGUID(userId, governanceOfficerGUID);

        if ((governanceOfficer != null) && (governanceOfficer.getAppointmentId().equals(appointmentId))
                                        && (governanceOfficer.getGovernanceDomain().equals(governanceDomain)))
        {
            basicHandler.deleteEntity(userId, governanceOfficerGUID, governanceOfficerTypeGUID, governanceOfficerTypeName, methodName);
        }
        else
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.GOVERNANCE_OFFICER_NOT_DELETED;
            String                     errorMessage = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(governanceOfficerGUID,
                                                                                         appointmentId,
                                                                                         governanceDomain.getName());

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                appointmentIdParameterName);
        }

        log.debug("Delete of governance officers successful: " + governanceOfficerGUID);
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    GovernanceOfficer getGovernanceOfficerByGUID(String     userId,
                                                 String     governanceOfficerGUID) throws UnrecognizedGUIDException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String        methodName = "getGovernanceOfficerByGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, governanceOfficerTypeName, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        EntityDetail  governanceOfficerEntity  = basicHandler.getEntityByGUID(governanceOfficerGUID,
                                                                              governanceOfficerGUIDParameterName,
                                                                              userId,
                                                                              methodName,
                                                                              governanceOfficerTypeName);

        return this.getGovernanceOfficerFromInstances(governanceOfficerEntity, userId, metadataCollection, methodName);
    }


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    GovernanceOfficer    getGovernanceOfficerByAppointmentId(String     userId,
                                                             String     appointmentId) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              AppointmentIdNotUniqueException,
                                                                                              UserNotAuthorizedException
    {
        final String        methodName = "getGovernanceOfficerByAppointmentId";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateName(appointmentId, appointmentIdParameterName, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            InstanceProperties properties;

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      appointmentIdPropertyName,
                                                                      appointmentId,
                                                                      methodName);

            List<EntityDetail> governanceOfficerEntities = metadataCollection.findEntitiesByProperty(userId,
                                                                                                     governanceOfficerTypeGUID,
                                                                                                     properties,
                                                                                                     MatchCriteria.ANY,
                                                                                                     0,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     50);

            if (governanceOfficerEntities == null)
            {
                GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.GOVERNANCE_OFFICER_NOT_FOUND_BY_APPOINTMENT_ID;
                String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(appointmentId);

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    appointmentId);
            }
            else if (governanceOfficerEntities.size() != 1)
            {
                GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.DUPLICATE_GOVERNANCE_OFFICER_FOR_APPOINTMENT_ID;
                String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(appointmentId);

                throw new AppointmentIdNotUniqueException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction(),
                                                          governanceOfficerEntities);
            }
            else
            {
                return this.getGovernanceOfficerFromInstances(governanceOfficerEntities.get(0),
                                                              userId,
                                                              metadataCollection,
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
        catch (InvalidParameterException |  AppointmentIdNotUniqueException error)
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
     * Return all of the defined governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<GovernanceOfficer>  getGovernanceOfficers(String     userId) throws PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String        methodName = "getGovernanceOfficers";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            List<EntityDetail> governanceOfficerEntities = metadataCollection.findEntitiesByProperty(userId,
                                                                                                     governanceOfficerTypeGUID,
                                                                                                     null,
                                                                                                     null,
                                                                                                     0,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     500);

            if (governanceOfficerEntities != null)
            {
                List<GovernanceOfficer>  governanceOfficers = new ArrayList<>();

                for (EntityDetail   governanceOfficerEntity : governanceOfficerEntities)
                {
                    governanceOfficers.add(this.getGovernanceOfficerFromInstances(governanceOfficerEntity,
                                                                                  userId,
                                                                                  metadataCollection,
                                                                                  methodName));
                }

                if (governanceOfficers.isEmpty())
                {
                    log.debug("No governance officers");
                    return null;
                }
                else
                {
                    log.debug(governanceOfficers.size() + " governance officers");
                    return governanceOfficers;
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

        log.debug("Null return from method: " + methodName);

        return null;
    }


    /**
     * Return all of the active governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<GovernanceOfficer>  getActiveGovernanceOfficers(String     userId) throws PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String        methodName = "getActiveGovernanceOfficers";

        List<GovernanceOfficer>   fullList = this.getGovernanceOfficers(userId);
        List<GovernanceOfficer>   returnList = new ArrayList<>();

        for (GovernanceOfficer  governanceOfficer : fullList)
        {
            if ((governanceOfficer != null) && (governanceOfficer.getAppointee() != null))
            {
                returnList.add(governanceOfficer);
            }
        }

        if (returnList.isEmpty())
        {
            log.debug("No governance officers for method: " + methodName);

            return null;
        }
        else
        {
            log.debug(returnList.size() + " active governance officers");

            return returnList;
        }
    }


    /**
     * Return all of the defined governance officers for a specific governance domain.  In a small organization
     * there is typically only one governance officer.   However a large organization may have multiple governance
     * officers, each with a different scope.  The governance officer with a null scope is the overall leader.
     *
     * @param userId the name of the calling user.
     * @param governanceDomain domain of interest
     * @return list of governance officer objects
     * @throws InvalidParameterException the governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<GovernanceOfficer>  getGovernanceOfficersByDomain(String             userId,
                                                           GovernanceDomain   governanceDomain) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String        methodName = "getGovernanceOfficersByDomain";

        errorHandler.validateEnum(governanceDomain, governanceDomainParameterName, methodName);

        List<GovernanceOfficer>   fullList = this.getGovernanceOfficers(userId);
        List<GovernanceOfficer>   returnList = new ArrayList<>();

        for (GovernanceOfficer  governanceOfficer : fullList)
        {
            if ((governanceOfficer != null) && (governanceOfficer.getGovernanceDomain() == governanceDomain))
            {
                returnList.add(governanceOfficer);
            }
        }

        if (returnList.isEmpty())
        {
            log.debug("Null return from method: " + methodName);

            return null;
        }
        else
        {
            log.debug(returnList.size() + " governance officers for domain: " + governanceDomain);

            return returnList;
        }
    }


    /**
     * Link a person to a governance officer.  Only one person may be appointed at any one time.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @param startDate the official start date of the appointment - null means effective immediately.
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void appointGovernanceOfficer(String  userId,
                                  String  governanceOfficerGUID,
                                  String  profileGUID,
                                  Date    startDate) throws UnrecognizedGUIDException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String        methodName = "appointGovernanceOfficer";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, governanceOfficerTypeName, methodName);
        errorHandler.validateGUID(profileGUID, profileGUIDParameterName, personalProfileTypeName, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        GovernanceOfficer   governanceOfficer = this.getGovernanceOfficerByGUID(userId, governanceOfficerGUID);
        Date                endDate = null;
        Date                now = new Date();
        Date                actualStartDate = startDate;

        /*
         * if the start date is null we set it to now.
         */
        if (actualStartDate == null)
        {
            actualStartDate = now;
        }

        /*
         * Work out if a successor has been appointed since this will determine the end date of the new appointment.
         */
        if (governanceOfficer.getSuccessors() != null)
        {
            for (GovernanceOfficerAppointee  successor : governanceOfficer.getSuccessors())
            {
                if (endDate == null)
                {
                    endDate = successor.getStartDate();
                }
                else if (successor.getStartDate() != null)
                {
                    if (endDate.getTime() > successor.getStartDate().getTime())
                    {
                        endDate = successor.getStartDate();
                    }
                }
            }
        }

        /*
         * If there is a current incumbent in the role, their appointment needs to be terminated.
         */
        if (governanceOfficer.getAppointee() != null)
        {
            if (governanceOfficer.getAppointee().getProfile() != null)
            {
                try
                {
                    Date incumbentEndDate = new Date(actualStartDate.getTime() - 1);

                    this.relieveGovernanceOfficer(userId, governanceOfficerGUID, governanceOfficer.getAppointee().getProfile().getGUID(), incumbentEndDate);
                }
                catch (Throwable   error)
                {
                    GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.FREE_ROLE_FOR_APPOINTMENT_FAILED;
                    String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getMessage());

                    throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction(),
                                                      error);
                }
            }
            else
            {
                GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.NO_PROFILE_FOR_GOVERNANCE_POST;
                String                     errorMessage = errorCode.getErrorMessageId()
                                                        + errorCode.getFormattedErrorMessage(governanceOfficerGUID,
                                                                                             governanceOfficer.getAppointee().getGUID());

                throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction());
            }
        }

        InstanceProperties properties = new InstanceProperties();
        properties.setEffectiveFromTime(startDate);
        properties.setEffectiveToTime(endDate);

        try
        {
            metadataCollection.addRelationship(userId, governancePostTypeGUID, properties, governanceOfficerGUID, profileGUID, null);
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
                                                         personalProfileTypeName,
                                                         profileGUID);
        }
        catch (Throwable  error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid.
     * @throws InvalidParameterException the profile is not linked to this governance officer.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void relieveGovernanceOfficer(String  userId,
                                  String  governanceOfficerGUID,
                                  String  profileGUID,
                                  Date    endDate) throws UnrecognizedGUIDException,
                                                          InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        final String        methodName = "relieveGovernanceOfficer";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, governanceOfficerTypeName, methodName);
        errorHandler.validateGUID(profileGUID, profileGUIDParameterName, personalProfileTypeName, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        Date  actualEndDate = endDate;

        if (actualEndDate == null)
        {
            actualEndDate = new Date();
        }

        try
        {
            List<Relationship> governancePosts = metadataCollection.getRelationshipsForEntity(userId,
                                                                                              governanceOfficerGUID,
                                                                                              governancePostTypeGUID,
                                                                                              0,
                                                                                              null,
                                                                                              null,
                                                                                              null,
                                                                                              null,
                                                                                              500);

            if ((governancePosts == null) || (governancePosts.isEmpty()))
            {
                GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.PROFILE_NOT_LINKED_TO_GOVERNANCE_OFFICER;
                String                     errorMessage = errorCode.getErrorMessageId()
                                                        + errorCode.getFormattedErrorMessage(profileGUID,
                                                                                             governanceOfficerGUID);

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    profileGUIDParameterName);
            }

            Relationship  relievedPost = null;

            for (Relationship  governancePost : governancePosts)
            {
                if (governancePost != null)
                {
                    EntityProxy   appointeeProxy = governancePost.getEntityTwoProxy();

                    if (appointeeProxy != null)
                    {
                        if (profileGUID.equals(appointeeProxy.getGUID()))
                        {
                            if (relievedPost == null)
                            {
                                relievedPost = governancePost;
                            }
                            else
                            {
                                /*
                                 * This person has been appointed more than once.  Find out which one was the later one.
                                 */
                                Date  governancePostEndDate = null;
                                Date  relievedPostEndDate = null;

                                if (governancePost.getProperties() != null)
                                {
                                    governancePostEndDate = governancePost.getProperties().getEffectiveToTime();
                                }

                                if (relievedPost.getProperties() != null)
                                {
                                    relievedPostEndDate = relievedPost.getProperties().getEffectiveToTime();
                                }

                                if ((relievedPostEndDate != null) && ((governancePostEndDate == null) || (governancePostEndDate.getTime() > relievedPostEndDate.getTime())))
                                {
                                    relievedPost = governancePost;
                                }
                            }
                        }
                    }
                    else
                    {
                        GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.INVALID_GOVERNANCE_POST_RELATIONSHIP;
                        String                     errorMessage = errorCode.getErrorMessageId()
                                                                + errorCode.getFormattedErrorMessage(governancePost.toString());

                        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction());
                    }
                }
            }

            if (relievedPost == null)
            {
                GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.PROFILE_NOT_LINKED_TO_GOVERNANCE_OFFICER;
                String                     errorMessage = errorCode.getErrorMessageId()
                                                        + errorCode.getFormattedErrorMessage(profileGUID,
                                                                                             governanceOfficerGUID);

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    profileGUIDParameterName);            }
            else
            {
                InstanceProperties properties = relievedPost.getProperties();

                if (properties == null)
                {
                    properties = new InstanceProperties();
                }

                properties.setEffectiveToTime(actualEndDate);

                metadataCollection.updateRelationshipProperties(userId, relievedPost.getGUID(), properties);
            }
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
                                                         personalProfileTypeName,
                                                         profileGUID);
        }
        catch (InvalidParameterException | PropertyServerException  error)
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
    }
}
