/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.handlers;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ElementType;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceOfficerElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
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
 * GovernanceOfficerHandler retrieves GovernanceOfficerProperties objects from the property server and maintains the
 * relationship between the governance officer and the profile of the individual that is appointed as the
 * governance officer.  It runs server-side in the GovernanceProgram
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class GovernanceOfficerHandler
{
    /*
     * Mapping of Governance Program API property names to open metadata types
     */
    private static final String governanceOfficerTypeGUID               = "578a3510-9ad3-45fe-8ada-e4e9572c37c8";
    private static final String governanceOfficerTypeName               = "GovernanceOfficerProperties";
    private static final String appointmentIdPropertyName               = "qualifiedName";
    private static final String titlePropertyName                       = "name";
    private static final String appointmentContextPropertyName          = "scope";
    private static final String governanceDomainPropertyName            = "domain";
    private static final String additionalPropertiesName                = "additionalProperties";

    private static final String governancePostTypeGUID                  = "4a316abe-bcce-4d11-ad5a-4bfb4079b80b";
    private static final String governancePostTypeName                  = "PersonRoleAppointment";


    /*
     * Parameter names used for error messages
     */
    private static final String governanceOfficerGUIDParameterName = "governanceOfficerGUID";
    private static final String governanceDomainParameterName      = "governanceDomain";
    private static final String appointmentIdParameterName         = "appointmentId";
    private static final String titleParameterName                 = "title";
    private static final String profileGUIDParameterName           = "profileGUID";

    private static final Logger log = LoggerFactory.getLogger(GovernanceOfficerHandler.class);


    private String                       serviceName;
    private String                       serverName;
    private RepositoryHandler            repositoryHandler;
    private OMRSRepositoryHelper         repositoryHelper;
    private InvalidParameterHandler      invalidParameterHandler;
    private GovernanceProgramEnumHandler enumHandler;
    private PersonalProfileHandler       personalProfileHandler;
    private ExternalReferencesHandler    externalReferencesHandler;

    private RESTExceptionHandler         errorHandler = new RESTExceptionHandler();


    /**
     * Construct the handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     * @param personalProfileHandler handler to manage personal profiles
     * @param externalReferencesHandler handler to manage external references
     */
    public GovernanceOfficerHandler(String                       serviceName,
                                    String                       serverName,
                                    InvalidParameterHandler      invalidParameterHandler,
                                    OMRSRepositoryHelper         repositoryHelper,
                                    RepositoryHandler            repositoryHandler,
                                    PersonalProfileHandler       personalProfileHandler,
                                    ExternalReferencesHandler    externalReferencesHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.enumHandler = new GovernanceProgramEnumHandler(serviceName, repositoryHelper);
        this.personalProfileHandler = personalProfileHandler;
        this.externalReferencesHandler = externalReferencesHandler;
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
                                                                  Map<String, String>  additionalProperties) throws InvalidParameterException
    {
        invalidParameterHandler.validateEnum(governanceDomain, governanceDomainParameterName, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        invalidParameterHandler.validateName(title, titleParameterName, methodName);

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
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         additionalPropertiesName,
                                                                         additionalProperties,
                                                                         methodName);
        }

        log.debug("Instance properties: " + properties.toString());

        return properties;
    }





    /**
     * Extract a governance officer bean from the returned GovernanceOfficerProperties entity and any
     * GovernancePost relationship to Person entity.  The assumption is that this is a 0..1
     * relationship so one entity (or null) is returned.  If lots of relationships are found then the
     * PropertyServerException is thrown.
     *
     * @param governanceOfficerEntity entity for person appointed to governance officer role - may be null
     * @param userId  user making the request.
     * @param methodName  name of calling method
     * @return governance officer or null
     * @throws PropertyServerException problem access the property server
     */
    private GovernanceOfficerElement getGovernanceOfficerFromInstances(EntityDetail            governanceOfficerEntity,
                                                                       String                  userId,
                                                                       String                  methodName) throws PropertyServerException
    {
        GovernanceOfficerElement    governanceOfficer = null;
        long                        now               = new Date().getTime();

        if (governanceOfficerEntity != null)
        {
            governanceOfficer = new GovernanceOfficerElement();

            ElementHeader header = new ElementHeader();

            header.setGUID(governanceOfficerEntity.getGUID());

            ElementType type = new ElementType();

            type.setTypeName(governanceOfficerTypeName);

            header.setType(type);

            governanceOfficer.setElementHeader(header);

            InstanceProperties instanceProperties = governanceOfficerEntity.getProperties();

            if (instanceProperties != null)
            {
                governanceOfficer.setAppointmentId(repositoryHelper.getStringProperty(serviceName, appointmentIdPropertyName, instanceProperties, methodName));
                governanceOfficer.setAppointmentContext(repositoryHelper.getStringProperty(serviceName, appointmentContextPropertyName, instanceProperties, methodName));
                governanceOfficer.setQualifiedName(repositoryHelper.getStringProperty(serviceName, titlePropertyName, instanceProperties, methodName));
                governanceOfficer.setGovernanceDomain(enumHandler.getGovernanceDomainFromProperties(instanceProperties, governanceDomainPropertyName, methodName));
                governanceOfficer.setAdditionalProperties(repositoryHelper.getStringMapFromProperty(serviceName, additionalPropertiesName, instanceProperties, methodName));
            }


            try
            {
                List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                            governanceOfficerEntity.getGUID(),
                                                                                            governanceOfficerTypeName,
                                                                                            governancePostTypeGUID,
                                                                                            governancePostTypeName,
                                                                                            methodName);

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

                        throw new GovernanceAppointeeNotUniqueException(GovernanceProgramErrorCode.MULTIPLE_INCUMBENTS_FOR_GOVERNANCE_OFFICER.getMessageDefinition(governanceDomain.getName(),
                                                                                                                                                                   governanceOfficerEntity.getGUID(),
                                                                                                                                                                   governanceOfficer.getAppointmentId(),
                                                                                                                                                                   governanceOfficer.getAppointmentContext(),
                                                                                                                                                                   Integer.toString(currentIncumbents.size())),
                                                                        this.getClass().getName(),
                                                                        methodName,
                                                                        currentIncumbents);
                    }
                    governanceOfficer.setPredecessors(this.getPredecessors(relationships, now, userId));
                    governanceOfficer.setSuccessors(this.getSuccessors(relationships, now, userId));
                }
            }
            catch (Exception   error)
            {
                errorHandler.handleUnexpectedException(error,
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
     * @throws InvalidParameterException GUID is null
     * @throws PropertyServerException server is having trouble
     * @throws UserNotAuthorizedException user is not authorized
     */
    private GovernanceOfficerAppointee   getAppointee(String     governancePostGUID,
                                                      String     personalProfileGUID,
                                                      String     userId,
                                                      Date       startDate,
                                                      Date       endDate) throws InvalidParameterException,
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
     * @throws InvalidParameterException GUID is not known
     * @throws PropertyServerException server is having trouble
     * @throws UserNotAuthorizedException user is not authorized
     */
    private List<GovernanceOfficerAppointee>   getPredecessors(List<Relationship>   governancePostings,
                                                               long                 now,
                                                               String               userId) throws InvalidParameterException,
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

                        if (governancePost.getEntityOneProxy() != null)
                        {
                            personalProfileGUID = governancePost.getEntityOneProxy().getGUID();
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
     * @throws InvalidParameterException GUID is not known
     * @throws PropertyServerException server is having trouble
     * @throws UserNotAuthorizedException user is not authorized
     */
    private List<GovernanceOfficerAppointee> getCurrentIncumbents(List<Relationship>   governancePostings,
                                                                  long                 now,
                                                                  String               userId) throws InvalidParameterException,
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

                        if (governancePost.getEntityOneProxy() != null)
                        {
                            personalProfileGUID = governancePost.getEntityOneProxy().getGUID();
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
     * @throws InvalidParameterException GUID is not known
     * @throws PropertyServerException server is having trouble
     * @throws UserNotAuthorizedException user is not authorized
     */
    private List<GovernanceOfficerAppointee>   getSuccessors(List<Relationship>   governancePostings,
                                                             long                 now,
                                                             String               userId) throws InvalidParameterException,
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

                        if (governancePost.getEntityOneProxy() != null)
                        {
                            personalProfileGUID = governancePost.getEntityOneProxy().getGUID();
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
    public String createGovernanceOfficer(String                     userId,
                                          GovernanceDomain           governanceDomain,
                                          String                     appointmentId,
                                          String                     appointmentContext,
                                          String                     title,
                                          Map<String, String>        additionalProperties,
                                          List<ExternalReference>    externalReferences) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String        methodName = "createGovernanceOfficer";

        invalidParameterHandler.validateUserId(userId, methodName);

        InstanceProperties      properties = createGovernanceOfficerProperties(methodName,
                                                                               governanceDomain,
                                                                               appointmentId,
                                                                               appointmentContext,
                                                                               title,
                                                                               additionalProperties);

        String governanceOfficerGUID = repositoryHandler.createEntity(userId,
                                                                      governanceOfficerTypeGUID,
                                                                      governanceOfficerTypeName,
                                                                      null,
                                                                      null,
                                                                      properties,
                                                                      methodName);

        if (governanceOfficerGUID != null)
        {
            log.debug("New governance officer entity: " + governanceOfficerGUID);

            if (externalReferences != null)
            {
                externalReferencesHandler.storeExternalReferences(userId, governanceOfficerGUID, externalReferences);
            }
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
     * @throws InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                                   the existing values associated with the governanceOfficerGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   updateGovernanceOfficer(String                     userId,
                                          String                     governanceOfficerGUID,
                                          GovernanceDomain           governanceDomain,
                                          String                     appointmentId,
                                          String                     appointmentContext,
                                          String                     title,
                                          Map<String, String>        additionalProperties,
                                          List<ExternalReference>    externalReferences) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String        methodName = "updateGovernanceOfficer";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);

        InstanceProperties      properties = createGovernanceOfficerProperties(methodName,
                                                                               governanceDomain,
                                                                               appointmentId,
                                                                               appointmentContext,
                                                                               title,
                                                                               additionalProperties);


        repositoryHandler.updateEntityProperties(userId,
                                                 null,
                                                 null,
                                                 governanceOfficerGUID,
                                                 governanceOfficerTypeGUID,
                                                 governanceOfficerTypeName,
                                                 properties,
                                                 methodName);

        externalReferencesHandler.storeExternalReferences(userId, governanceOfficerGUID, externalReferences);

        log.debug("Update of governance officer successful: " + governanceOfficerGUID);
    }


    /**
     * Remove the requested governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param governanceDomain  the governance domain for the governance officer.
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   deleteGovernanceOfficer(String              userId,
                                          String              governanceOfficerGUID,
                                          String              appointmentId,
                                          GovernanceDomain    governanceDomain) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "deleteGovernanceOfficer";
        final String guidParameter = "governanceOfficerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        invalidParameterHandler.validateEnum(governanceDomain, governanceDomainParameterName, methodName);

        repositoryHandler.removeEntity(userId,
                                       null,
                                       null,
                                       governanceOfficerGUID,
                                       guidParameter,
                                       governanceOfficerTypeGUID,
                                       governanceOfficerTypeName,
                                       appointmentIdPropertyName,
                                       appointmentId,
                                       methodName);

        log.debug("Delete of governance officers successful: " + governanceOfficerGUID);
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object
     * @throws InvalidParameterException the unique identifier of the governance officer is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerProperties getGovernanceOfficerByGUID(String     userId,
                                                                  String     governanceOfficerGUID) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String        methodName = "getGovernanceOfficerByGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);


        EntityDetail  governanceOfficerEntity  = repositoryHandler.getEntityByGUID(userId,
                                                                                   governanceOfficerGUID,
                                                                                   governanceOfficerGUIDParameterName,
                                                                                   governanceOfficerTypeName,
                                                                                   methodName);

        return this.getGovernanceOfficerFromInstances(governanceOfficerEntity, userId, methodName);
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
    public GovernanceOfficerProperties getGovernanceOfficerByAppointmentId(String     userId,
                                                                           String     appointmentId) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     AppointmentIdNotUniqueException,
                                                                                                     UserNotAuthorizedException
    {
        final String        methodName = "getGovernanceOfficerByAppointmentId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);

        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  appointmentIdPropertyName,
                                                                  appointmentId,
                                                                  methodName);


        List<EntityDetail> governanceOfficerEntities = repositoryHandler.getEntityByName(userId,
                                                                                         properties,
                                                                                         governanceOfficerTypeGUID,
                                                                                         methodName);

        if (governanceOfficerEntities == null)
        {
            throw new InvalidParameterException(GovernanceProgramErrorCode.GOVERNANCE_OFFICER_NOT_FOUND_BY_APPOINTMENT_ID.getMessageDefinition(appointmentId),
                                                this.getClass().getName(),
                                                methodName,
                                                appointmentId);
        }
        else if (governanceOfficerEntities.size() != 1)
        {
            throw new AppointmentIdNotUniqueException(GovernanceProgramErrorCode.DUPLICATE_GOVERNANCE_OFFICER_FOR_APPOINTMENT_ID.getMessageDefinition(appointmentId),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      governanceOfficerEntities);
        }
        else
        {
            return this.getGovernanceOfficerFromInstances(governanceOfficerEntities.get(0),
                                                          userId,
                                                          methodName);
        }
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws InvalidParameterException the userId is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<GovernanceOfficerProperties>  getGovernanceOfficers(String     userId) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String        methodName = "getGovernanceOfficers";

        invalidParameterHandler.validateUserId(userId, methodName);

        List<EntityDetail> governanceOfficerEntities = repositoryHandler.getEntitiesByType(userId,
                                                                                           governanceOfficerTypeGUID,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           methodName);

        if (governanceOfficerEntities != null)
        {
            List<GovernanceOfficerProperties> governanceOfficers = new ArrayList<>();

            for (EntityDetail   governanceOfficerEntity : governanceOfficerEntities)
            {
                governanceOfficers.add(this.getGovernanceOfficerFromInstances(governanceOfficerEntity,
                                                                              userId,
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

        log.debug("Null return from method: " + methodName);

        return null;
    }


    /**
     * Return all of the active governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws InvalidParameterException the userId is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<GovernanceOfficerProperties>  getActiveGovernanceOfficers(String     userId) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {

        final String        methodName = "getActiveGovernanceOfficers";

        List<GovernanceOfficerProperties> fullList   = this.getGovernanceOfficers(userId);
        List<GovernanceOfficerProperties> returnList = new ArrayList<>();

        for (GovernanceOfficerProperties governanceOfficer : fullList)
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
    public List<GovernanceOfficerProperties>  getGovernanceOfficersByDomain(String             userId,
                                                                            GovernanceDomain   governanceDomain) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String        methodName = "getGovernanceOfficersByDomain";

        invalidParameterHandler.validateEnum(governanceDomain, governanceDomainParameterName, methodName);

        List<GovernanceOfficerProperties> fullList   = this.getGovernanceOfficers(userId);
        List<GovernanceOfficerProperties> returnList = new ArrayList<>();

        for (GovernanceOfficerProperties governanceOfficer : fullList)
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
     * @throws InvalidParameterException the unique identifier of the governance officer or profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void appointGovernanceOfficer(String  userId,
                                         String  governanceOfficerGUID,
                                         String  profileGUID,
                                         Date    startDate) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String        methodName = "appointGovernanceOfficer";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);

        GovernanceOfficerProperties governanceOfficer = this.getGovernanceOfficerByGUID(userId, governanceOfficerGUID);
        Date                        endDate           = null;
        Date                        now               = new Date();
        Date                        actualStartDate   = startDate;

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
                catch (Exception   error)
                {
                    throw new PropertyServerException(GovernanceProgramErrorCode.FREE_ROLE_FOR_APPOINTMENT_FAILED.getMessageDefinition(error.getMessage()),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      error);
                }
            }
            else
            {
                throw new PropertyServerException(GovernanceProgramErrorCode.NO_PROFILE_FOR_GOVERNANCE_POST.getMessageDefinition(governanceOfficerGUID,
                                                                                                                                 governanceOfficer.getAppointee().getGUID()),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }

        InstanceProperties properties = new InstanceProperties();
        properties.setEffectiveFromTime(startDate);
        properties.setEffectiveToTime(endDate);

        repositoryHandler.createRelationship(userId,
                                             governancePostTypeGUID,
                                             null,
                                             null,
                                             profileGUID,
                                             governanceOfficerGUID,
                                             properties,
                                             methodName);
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @param endDate date at which the current incumbent leaves the position
     * @throws InvalidParameterException the profile is not linked to this governance officer.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void relieveGovernanceOfficer(String  userId,
                                         String  governanceOfficerGUID,
                                         String  profileGUID,
                                         Date    endDate) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String        methodName = "relieveGovernanceOfficer";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);


        Date  actualEndDate = endDate;

        if (actualEndDate == null)
        {
            actualEndDate = new Date();
        }

        List<Relationship> governancePosts = repositoryHandler.getRelationshipsByType(userId,
                                                                                      governanceOfficerGUID,
                                                                                      governanceOfficerTypeName,
                                                                                      governancePostTypeGUID,
                                                                                      governancePostTypeName,
                                                                                      methodName);

        if ((governancePosts == null) || (governancePosts.isEmpty()))
        {
            throw new InvalidParameterException(GovernanceProgramErrorCode.PROFILE_NOT_LINKED_TO_GOVERNANCE_OFFICER.getMessageDefinition(profileGUID,
                                                                                                                                         governanceOfficerGUID),
                                                this.getClass().getName(),
                                                methodName,
                                                profileGUIDParameterName);
        }

        Relationship  relievedPost = null;

        for (Relationship  governancePost : governancePosts)
        {
            if (governancePost != null)
            {
                EntityProxy   appointeeProxy = governancePost.getEntityOneProxy();

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
                    throw new PropertyServerException(GovernanceProgramErrorCode.INVALID_GOVERNANCE_POST_RELATIONSHIP.getMessageDefinition(governancePost.toString()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
            }
        }

        if (relievedPost == null)
        {
            throw new InvalidParameterException(GovernanceProgramErrorCode.PROFILE_NOT_LINKED_TO_GOVERNANCE_OFFICER.getMessageDefinition(profileGUID,
                                                                                                                                         governanceOfficerGUID),
                                                this.getClass().getName(),
                                                methodName,
                                                profileGUIDParameterName);
        }
        else
        {
            InstanceProperties properties = relievedPost.getProperties();

            if (properties == null)
            {
                properties = new InstanceProperties();
            }

            properties.setEffectiveToTime(actualEndDate);

            repositoryHandler.updateRelationshipProperties(userId, null, null, relievedPost, properties, methodName);
        }
    }
}
