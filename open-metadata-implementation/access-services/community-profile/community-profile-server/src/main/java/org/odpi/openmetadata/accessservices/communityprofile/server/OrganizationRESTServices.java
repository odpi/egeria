/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.converters.CommunityProfileOMASConverter;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ContactDetailsHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ContributionRecordHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The OrganizationRESTServices provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) capability for organization management.  This interface provides support for creating all types of actor profiles and
 * associated user roles in order to describe the structure of an organization.
 */
public class OrganizationRESTServices
{
    static private final CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OrganizationRESTServices.class),
                                                                            instanceHandler.getServiceName());


    private final RESTExceptionHandler   restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public OrganizationRESTServices()
    {
    }


    /**
     * Create a definition of an actor profile.  This could be for the whole organization, a team, a person or a system.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody properties for a actor profile
     *
     * @return unique identifier of actor profile
     *
     *   InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public GUIDResponse createActorProfile(String                  serverName,
                                           String                  userId,
                                           ActorProfileRequestBody requestBody)
    {
        final String methodName = "createActorProfile";
        final String guideParameterName = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                String profileGUID = handler.createActorProfile(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getProperties().getQualifiedName(),
                                                                requestBody.getProperties().getKnownName(),
                                                                requestBody.getProperties().getDescription(),
                                                                requestBody.getProperties().getAdditionalProperties(),
                                                                requestBody.getProperties().getTypeName(),
                                                                requestBody.getProperties().getExtendedProperties(),
                                                                requestBody.getProperties().getEffectiveFrom(),
                                                                requestBody.getProperties().getEffectiveTo(),
                                                                new Date(),
                                                                methodName);

                if (requestBody.getContributionRecord() != null)
                {
                    ContributionRecordHandler<ContributionRecordElement> recordHandler = instanceHandler.getContributionRecordHandler(userId, serverName, methodName);

                    recordHandler.saveContributionRecord(userId,
                                                         profileGUID,
                                                         guideParameterName,
                                                         requestBody.getProperties().getQualifiedName(),
                                                         requestBody.getContributionRecord().getKarmaPoints(),
                                                         requestBody.getContributionRecord().getIsPublic(),
                                                         null,
                                                         null,
                                                         null,
                                                         true,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName);
                }

                response.setGUID(profileGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the definition of an actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileGUID unique identifier of actor profile
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *
     *   InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse updateActorProfile(String                  serverName,
                                           String                  userId,
                                           String                  actorProfileGUID,
                                           boolean                 isMergeUpdate,
                                           ActorProfileRequestBody requestBody)
    {
        final String methodName        = "updateActorProfile";
        final String guidParameterName = "actorProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateActorProfile(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           actorProfileGUID,
                                           guidParameterName,
                                           requestBody.getProperties().getQualifiedName(),
                                           requestBody.getProperties().getKnownName(),
                                           requestBody.getProperties().getDescription(),
                                           requestBody.getProperties().getAdditionalProperties(),
                                           requestBody.getProperties().getTypeName(),
                                           requestBody.getProperties().getExtendedProperties(),
                                           isMergeUpdate,
                                           null,
                                           null,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);

                if (requestBody.getContributionRecord() != null)
                {
                    ContributionRecordHandler<ContributionRecordElement> recordHandler = instanceHandler.getContributionRecordHandler(userId, serverName, methodName);

                    recordHandler.saveContributionRecord(userId,
                                                         actorProfileGUID,
                                                         guidParameterName,
                                                         requestBody.getProperties().getQualifiedName(),
                                                         requestBody.getContributionRecord().getKarmaPoints(),
                                                         requestBody.getContributionRecord().getIsPublic(),
                                                         null,
                                                         null,
                                                         null,
                                                         true,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }


        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the definition of an actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileGUID unique identifier of actor profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException guid or userId is null; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse deleteActorProfile(String                    serverName,
                                           String                    userId,
                                           String                    actorProfileGUID,
                                           ExternalSourceRequestBody requestBody)
    {
        final String methodName        = "deleteActorProfile";
        final String guidParameterName = "actorProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            if (requestBody != null)
            {
                ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.removeActorProfile(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           actorProfileGUID,
                                           guidParameterName,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add a new contact method to the profile.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param actorProfileGUID identifier of the profile to update.
     * @param requestBody properties of contact method.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     *   InvalidParameterException the userId is null or invalid.  Another property is invalid.
     *   PropertyServerException there is a problem retrieving information from the property server(s).
     *   UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addContactMethod(String                   serverName,
                                         String                   userId,
                                         String                   actorProfileGUID,
                                         ContactMethodRequestBody requestBody)
    {
        final String methodName        = "addContactMethod";
        final String guidParameterName = "actorProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ContactDetailsHandler<ContactMethodElement> handler = instanceHandler.getContactDetailsHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                int contactMethodTypeOrdinal = 0;

                if (requestBody.getProperties().getContactMethodType() != null)
                {
                    contactMethodTypeOrdinal = requestBody.getProperties().getContactMethodType().getOrdinal();
                }

                handler.createContactMethod(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            actorProfileGUID,
                                            guidParameterName,
                                            requestBody.getProperties().getName(),
                                            requestBody.getProperties().getContactType(),
                                            contactMethodTypeOrdinal,
                                            requestBody.getProperties().getContactMethodService(),
                                            requestBody.getProperties().getContactMethodValue(),
                                            null,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove an obsolete contact method from the profile.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException the userId is null or invalid.  Another property is invalid.
     *   PropertyServerException there is a problem retrieving information from the property server(s).
     *   UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteContactMethod(String                    serverName,
                                            String                    userId,
                                            String                    contactMethodGUID,
                                            ExternalSourceRequestBody requestBody)
    {
        final String methodName                  = "deleteContactMethod";
        final String guidParameterName           = "contactMethodGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            ContactDetailsHandler<ContactMethodElement> handler = instanceHandler.getContactDetailsHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               contactMethodGUID,
                                               guidParameterName,
                                               OpenMetadataType.CONTACT_DETAILS.typeGUID,
                                               OpenMetadataType.CONTACT_DETAILS.typeName,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
                                               methodName);
            }
            else
            {
                handler.deleteBeanInRepository(userId,
                                               null,
                                               null,
                                               contactMethodGUID,
                                               guidParameterName,
                                               OpenMetadataType.CONTACT_DETAILS.typeGUID,
                                               OpenMetadataType.CONTACT_DETAILS.typeName,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
                                               methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link two related team/organization actor profiles together as part of a hierarchy.
     * A team/organization actor profile can only have one parent but many child actor profiles.
     *
     * @param serverName called server
     * @param userId calling user
     * @param superTeamProfileGUID unique identifier of the parent team profile
     * @param subTeamProfileGUID unique identifier of the child team profile
     * @param delegationEscalationAuthority can workflows delegate/escalate through this link?
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse linkTeamsInHierarchy(String                    serverName,
                                             String                    userId,
                                             String                    superTeamProfileGUID,
                                             String                    subTeamProfileGUID,
                                             boolean                   delegationEscalationAuthority,
                                             EffectiveDatesRequestBody requestBody)
    {
        final String methodName               = "linkTeamsInHierarchy";
        final String profileGUIDParameterName = "superTeamProfileGUID";
        final String subTeamGUIDParameterName = "subTeamProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            if (requestBody != null)
            {
                ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.linkTeamHierarchy(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          superTeamProfileGUID,
                                          profileGUIDParameterName,
                                          subTeamProfileGUID,
                                          subTeamGUIDParameterName,
                                          delegationEscalationAuthority,
                                          requestBody.getEffectiveFrom(),
                                          requestBody.getEffectiveTo(),
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between two actor profiles in the actor profile hierarchy.
     *
     * @param serverName called server
     * @param userId calling user
     * @param superTeamProfileGUID unique identifier of the parent actor profile
     * @param subTeamProfileGUID unique identifier of the child actor profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkTeamsInHierarchy(String                    serverName,
                                               String                    userId,
                                               String                    superTeamProfileGUID,
                                               String                    subTeamProfileGUID,
                                               ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "unlinkTeamsInHierarchy";
        final String profileGUIDParameterName = "superTeamProfileGUID";
        final String subTeamGUIDParameterName = "subTeamProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.unlinkTeamHierarchy(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            superTeamProfileGUID,
                                            profileGUIDParameterName,
                                            subTeamProfileGUID,
                                            subTeamGUIDParameterName,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException actorProfileGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfileResponse getActorProfileByGUID(String serverName,
                                                      String userId,
                                                      String actorProfileGUID)
    {
        final String methodName        = "getActorProfileByGUID";
        final String guidParameterName = "actorProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfileResponse response = new ActorProfileResponse();
        AuditLog             auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getActorProfileByGUID(userId,
                                                              actorProfileGUID,
                                                              guidParameterName,
                                                              OpenMetadataType.ACTOR_PROFILE.typeName,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException actorProfileUserId or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfileResponse getActorProfileByUserId(String serverName,
                                                        String userId,
                                                        String actorProfileUserId)
    {
        final String methodName        = "getActorProfileByGUID";
        final String nameParameterName = "actorProfileUserId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfileResponse response = new ActorProfileResponse();
        AuditLog             auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getActorProfileForUser(userId,
                                                               actorProfileUserId,
                                                               nameParameterName,
                                                               OpenMetadataType.ACTOR_PROFILE.typeName,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a named actor profiles.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody unique name for the actor profile
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfilesResponse getActorProfilesByName(String          serverName,
                                                        String          userId,
                                                        int             startFrom,
                                                        int             pageSize,
                                                        NameRequestBody requestBody)
    {
        final String methodName         = "getActorProfileByName";
        final String nameParameterName  = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfilesResponse response = new ActorProfilesResponse();
        AuditLog              auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getActorProfilesByName(userId,
                                                                requestBody.getName(),
                                                                nameParameterName,
                                                                OpenMetadataType.ACTOR_PROFILE.typeGUID,
                                                                OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return all actor profiles.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfilesResponse getActorProfiles(String          serverName,
                                                  String          userId,
                                                  int             startFrom,
                                                  int             pageSize)
    {
        final String methodName         = "getActorProfiles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfilesResponse response = new ActorProfilesResponse();
        AuditLog              auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getActorProfiles(userId,
                                                          OpenMetadataType.ACTOR_PROFILE.typeGUID,
                                                          OpenMetadataType.ACTOR_PROFILE.typeName,
                                                          startFrom,
                                                          pageSize,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a named actor profiles.
     *
     * @param serverName called server
     * @param userId calling user
     * @param locationGUID unique identifier for the location
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfilesResponse getActorProfilesByLocation(String          serverName,
                                                            String          userId,
                                                            String          locationGUID,
                                                            int             startFrom,
                                                            int             pageSize)
    {
        final String methodName         = "getActorProfilesByLocation";
        final String guidParameterName  = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfilesResponse response = new ActorProfilesResponse();
        AuditLog              auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getActorProfilesByLocation(userId,
                                                                    locationGUID,
                                                                    guidParameterName,
                                                                    OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of matching profiles for the search string.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody RegEx string to search for
     *
     * @return list of matching actor profiles
     *
     *   InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *   PropertyServerException the server is not available.
     *   UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ActorProfilesResponse findActorProfiles(String                  serverName,
                                                   String                  userId,
                                                   int                     startFrom,
                                                   int                     pageSize,
                                                   SearchStringRequestBody requestBody)
    {
        final String methodName                 = "findActorProfiles";
        final String searchStringParameterName  = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfilesResponse response = new ActorProfilesResponse();
        AuditLog              auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.findActorProfiles(userId,
                                                           requestBody.getSearchString(),
                                                           searchStringParameterName,
                                                           OpenMetadataType.ACTOR_PROFILE.typeGUID,
                                                           OpenMetadataType.ACTOR_PROFILE.typeName,
                                                           startFrom,
                                                           pageSize,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a definition of a person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody properties for a person role
     *
     * @return unique identifier of person role
     *
     *   InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public GUIDResponse createPersonRole(String                serverName,
                                         String                userId,
                                         PersonRoleRequestBody requestBody)
    {
        final String methodName = "createPersonRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog      = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                String profileGUID = handler.createPersonRole(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              requestBody.getProperties().getQualifiedName(),
                                                              requestBody.getProperties().getRoleId(),
                                                              requestBody.getProperties().getTitle(),
                                                              requestBody.getProperties().getDescription(),
                                                              requestBody.getProperties().getScope(),
                                                              requestBody.getProperties().getHeadCount(),
                                                              requestBody.getProperties().getHeadCountLimitSet(),
                                                              requestBody.getProperties().getDomainIdentifier(),
                                                              requestBody.getProperties().getAdditionalProperties(),
                                                              requestBody.getProperties().getTypeName(),
                                                              requestBody.getProperties().getExtendedProperties(),
                                                              requestBody.getProperties().getEffectiveFrom(),
                                                              requestBody.getProperties().getEffectiveTo(),
                                                              new Date(),
                                                              methodName);
                response.setGUID(profileGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the definition of a person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier of person role
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *
     *   InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse updatePersonRole(String                serverName,
                                         String                userId,
                                         String                personRoleGUID,
                                         boolean               isMergeUpdate,
                                         PersonRoleRequestBody requestBody)
    {
        final String methodName                  = "updatePersonRole";
        final String guidParameterName           = "personRoleGUID";
        final String qualifiedNameParameterName  = "properties.roleId";
        final String nameParameterName  = "properties.title";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler.updatePersonRole(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         personRoleGUID,
                                         guidParameterName,
                                         requestBody.getProperties().getQualifiedName(),
                                         qualifiedNameParameterName,
                                         requestBody.getProperties().getRoleId(),
                                         requestBody.getProperties().getTitle(),
                                         nameParameterName,
                                         requestBody.getProperties().getDescription(),
                                         requestBody.getProperties().getScope(),
                                         requestBody.getProperties().getHeadCount(),
                                         requestBody.getProperties().getHeadCountLimitSet(),
                                         requestBody.getProperties().getDomainIdentifier(),
                                         requestBody.getProperties().getAdditionalProperties(),
                                         requestBody.getProperties().getTypeName(),
                                         requestBody.getProperties().getExtendedProperties(),
                                         isMergeUpdate,
                                         null,
                                         null,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the definition of a person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier of person role
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException guid or userId is null; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse deletePersonRole(String                    serverName,
                                         String                    userId,
                                         String                    personRoleGUID,
                                         ExternalSourceRequestBody requestBody)
    {
        final String methodName                  = "deletePersonRole";
        final String guidParameterName           = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            ContactDetailsHandler<ContactMethodElement> handler = instanceHandler.getContactDetailsHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               personRoleGUID,
                                               guidParameterName,
                                               OpenMetadataType.CONTACT_DETAILS.typeGUID,
                                               OpenMetadataType.CONTACT_DETAILS.typeName,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
                                               methodName);
            }
            else
            {
                handler.deleteBeanInRepository(userId,
                                               null,
                                               null,
                                               personRoleGUID,
                                               guidParameterName,
                                               OpenMetadataType.CONTACT_DETAILS.typeGUID,
                                               OpenMetadataType.CONTACT_DETAILS.typeName,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
                                               methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a person role to a person profile to show that that person is performing the role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier of the person role
     * @param personProfileGUID unique identifier of the person profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public GUIDResponse linkPersonRoleToProfile(String                 serverName,
                                                String                 userId,
                                                String                 personRoleGUID,
                                                String                 personProfileGUID,
                                                AppointmentRequestBody requestBody)
    {
        final String methodName                  = "linkPersonRoleToProfile";
        final String profileGUIDParameterName    = "personProfileGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                String profileGUID = handler.appointPersonToRole(userId,
                                                                 requestBody.getExternalSourceGUID(),
                                                                 requestBody.getExternalSourceName(),
                                                                 personProfileGUID,
                                                                 profileGUIDParameterName,
                                                                 personRoleGUID,
                                                                 personRoleGUIDParameterName,
                                                                 requestBody.getProperties().getIsPublic(),
                                                                 requestBody.getProperties().getEffectiveFrom(),
                                                                 requestBody.getProperties().getEffectiveTo(),
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName);
                response.setGUID(profileGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of people appointed to a particular role.
     *
     * @param serverName called server
     * @param userId               calling user
     * @param personRoleGUID       unique identifier of the person role
     * @param startFrom            index of the list to start from (0 for start)
     * @param pageSize             maximum number of elements to return
     * @param requestBody        time for appointments, null for full appointment history
     *
     * @return list of appointees or
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public AppointeesResponse getAppointees(String                   serverName,
                                            String                   userId,
                                            String                   personRoleGUID,
                                            int                      startFrom,
                                            int                      pageSize,
                                            ResultsRequestBody requestBody)
    {
        final String methodName                  = "getAppointees";
        final String personRoleGUIDParameterName = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AppointeesResponse response = new AppointeesResponse();
        AuditLog           auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement>      roleHandler    = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);
            ActorProfileHandler<ActorProfileElement> profileHandler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                // todo the paging is not right
                List<Relationship> appointmentRelationships = roleHandler.getAttachmentLinks(userId,
                                                                                             personRoleGUID,
                                                                                             personRoleGUIDParameterName,
                                                                                             OpenMetadataType.PERSON_ROLE.typeName,
                                                                                             OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeGUID,
                                                                                             OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                             null,
                                                                                             OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                                             1,
                                                                                             null,
                                                                                             null,
                                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                                             null,
                                                                                             false,
                                                                                             false,
                                                                                             startFrom,
                                                                                             pageSize,
                                                                                             requestBody.getEffectiveTime(),
                                                                                             methodName);
                if (appointmentRelationships != null)
                {
                    List<Appointee>           appointees = new ArrayList<>();
                    OMRSRepositoryHelper      repositoryHelper = roleHandler.getRepositoryHelper();
                    String                    serviceName = roleHandler.getServiceName();
                    CommunityProfileOMASConverter<Appointee> converter = new CommunityProfileOMASConverter<>(repositoryHelper, serviceName, serverName);
                    RepositoryErrorHandler                   errorHandler = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName, auditLog);

                    for (Relationship relationship : appointmentRelationships)
                    {
                        if ((relationship != null) && (relationship.getProperties() != null))
                        {
                            if (requestBody.getEffectiveTime() == null)
                            {
                                Appointee appointee = getAppointeeFromRelationship(userId,
                                                                                   relationship,
                                                                                   profileHandler,
                                                                                   converter,
                                                                                   repositoryHelper,
                                                                                   serviceName,
                                                                                   errorHandler,
                                                                                   methodName);

                                appointees.add(appointee);
                            }
                            else
                            {
                                InstanceProperties properties = relationship.getProperties();
                                Date               effectiveTime = requestBody.getEffectiveTime();

                                /*
                                 * Need to retrieve the appointments that are active
                                 */
                                if (((properties.getEffectiveFromTime() == null) || properties.getEffectiveFromTime().before(effectiveTime)) &&
                                            ((properties.getEffectiveToTime() == null) || properties.getEffectiveToTime().after(effectiveTime)))
                                {
                                    Appointee appointee = getAppointeeFromRelationship(userId,
                                                                                       relationship,
                                                                                       profileHandler,
                                                                                       converter,
                                                                                       repositoryHelper,
                                                                                       serviceName,
                                                                                       errorHandler,
                                                                                       methodName);

                                    appointees.add(appointee);
                                }
                            }
                        }
                        else
                        {
                            errorHandler.logBadRelationship(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                            relationship,
                                                            methodName);
                        }
                    }

                    if (!appointees.isEmpty())
                    {
                        response.setElements(appointees);
                    }
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Extract the appointee from the supplied relationship
     *
     * @param userId calling user
     * @param relationship PersonRoleAppointment relationship
     * @param methodName calling method
     *
     * @return populated appointee
     *
     * @throws InvalidParameterException the unique identifier of the governance role is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    private Appointee getAppointeeFromRelationship(String                                             userId,
                                                   Relationship                                       relationship,
                                                   ActorProfileHandler<ActorProfileElement>           profileHandler,
                                                   CommunityProfileOMASConverter<Appointee>           converter,
                                                   OMRSRepositoryHelper                               repositoryHelper,
                                                   String                                             serviceName,
                                                   RepositoryErrorHandler                             errorHandler,
                                                   String                                             methodName) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        final String profileGUIDParameterName = "profileGUID";

        if ((relationship != null) && (relationship.getProperties() != null) && (relationship.getEntityOneProxy() != null) && (relationship.getEntityTwoProxy() != null))
        {
            Appointee appointee = new Appointee();

            InstanceProperties properties = relationship.getProperties();

            ElementHeader elementHeader = converter.getMetadataElementHeader(Appointee.class,
                                                                             relationship,
                                                                             null,
                                                                             methodName);

            appointee.setElementHeader(elementHeader);
            appointee.setStartDate(properties.getEffectiveFromTime());
            appointee.setEndDate(properties.getEffectiveToTime());
            appointee.setIsPublic(repositoryHelper.getBooleanProperty(serviceName,
                                                                      OpenMetadataProperty.IS_PUBLIC.name,
                                                                      relationship.getProperties(),
                                                                      methodName));


            ActorProfileElement profile = profileHandler.getActorProfileByGUID(userId,
                                                                               relationship.getEntityOneProxy().getGUID(),
                                                                               profileGUIDParameterName,
                                                                               OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                               false,
                                                                               false,
                                                                               new Date(),
                                                                               methodName);

            appointee.setProfile(profile);

            return appointee;
        }
        else
        {
            errorHandler.logBadRelationship(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                            relationship,
                                            methodName);
        }

        return null;
    }


    /**
     * Update the properties for the appointment of a person to a role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param appointmentGUID unique identifier of the appointment relationship
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateAppointment(String                 serverName,
                                          String                 userId,
                                          String                 appointmentGUID,
                                          boolean                isMergeUpdate,
                                          AppointmentRequestBody requestBody)
    {
        final String methodName                  = "updatePersonRole";
        final String guidParameterName           = "appointmentGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler.updateAppointment(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          appointmentGUID,
                                          guidParameterName,
                                          requestBody.getProperties().getIsPublic(),
                                          requestBody.getProperties().getEffectiveFrom(),
                                          requestBody.getProperties().getEffectiveTo(),
                                          isMergeUpdate,
                                          false, false, new Date(),
                                          methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a person role and a person profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param appointmentGUID unique identifier of the appointment relationship
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkPersonRoleFromProfile(String                    serverName,
                                                    String                    userId,
                                                    String                    appointmentGUID,
                                                    ExternalSourceRequestBody requestBody)
    {
        final String methodName                  = "unlinkPersonRoleFromProfile";
        final String appointmentGUIDParameterName    = "appointmentGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.relievePersonFromRole(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              null,
                                              null,
                                              null,
                                              null,
                                              appointmentGUID,
                                              appointmentGUIDParameterName,
                                              new Date(),
                                              new Date(),
                                              methodName);
            }
            else
            {
                handler.relievePersonFromRole(userId,
                                              null,
                                              null,
                                              null,
                                              null,
                                              null,
                                              null,
                                              appointmentGUID,
                                              appointmentGUIDParameterName,
                                              new Date(),
                                              new Date(),
                                              methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a team leader person role or team member person role to a team profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param requestBody  properties for the relationship
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse linkTeamPlayer(String                serverName,
                                       String                userId,
                                       String                teamRoleGUID,
                                       String                teamProfileGUID,
                                       TeamPlayerRequestBody requestBody)
    {
        final String methodName                  = "linkTeamPlayer";
        final String teamRoleGUIDParameterName    = "teamRoleGUID";
        final String teamProfileGUIDParameterName = "teamProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getIsLeadershipRole())
                {
                    handler.addTeamLeader(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          teamRoleGUID,
                                          teamRoleGUIDParameterName,
                                          teamProfileGUID,
                                          teamProfileGUIDParameterName,
                                          requestBody.getPosition(),
                                          null,
                                          null,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
                }
                else
                {
                    handler.addTeamLeader(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          teamRoleGUID,
                                          teamRoleGUIDParameterName,
                                          teamProfileGUID,
                                          teamProfileGUIDParameterName,
                                          requestBody.getPosition(),
                                          null,
                                          null,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a person role and a team profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkTeamPlayer(String                serverName,
                                         String                userId,
                                         String                teamRoleGUID,
                                         String                teamProfileGUID,
                                         TeamPlayerRequestBody requestBody)
    {
        final String methodName                   = "unlinkTeamPlayer";
        final String teamRoleGUIDParameterName    = "teamRoleGUID";
        final String teamProfileGUIDParameterName = "teamProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getIsLeadershipRole())
                {
                    handler.removeTeamLeader(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             teamRoleGUID,
                                             teamRoleGUIDParameterName,
                                             teamProfileGUID,
                                             teamProfileGUIDParameterName,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
                }
                else
                {
                    handler.removeTeamMember(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             teamRoleGUID,
                                             teamRoleGUIDParameterName,
                                             teamProfileGUID,
                                             teamProfileGUIDParameterName,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }





    /**
     * Return information about a specific person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *
     *   InvalidParameterException personRoleGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public PersonRoleResponse getPersonRoleByGUID(String serverName,
                                                  String userId,
                                                  String personRoleGUID)
    {
        final String methodName        = "getPersonRoleByGUID";
        final String guidParameterName = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRoleResponse response = new PersonRoleResponse();
        AuditLog           auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getPersonRoleByGUID(userId,
                                                            personRoleGUID,
                                                            guidParameterName,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a named person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody unique name for the actor profile
     *
     * @return list of matching person roles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public PersonRolesResponse getPersonRoleByName(String          serverName,
                                                   String          userId,
                                                   int             startFrom,
                                                   int             pageSize,
                                                   NameRequestBody requestBody)
    {
        final String methodName         = "getPersonRoleByName";
        final String nameParameterName  = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRolesResponse response = new PersonRolesResponse();
        AuditLog            auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getPersonRolesByName(userId,
                                                              requestBody.getName(),
                                                              nameParameterName,
                                                              startFrom,
                                                              pageSize,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a person role connected to the named team via the TeamLeadership relationship.
     *
     * @param serverName called server
     * @param userId calling user
     * @param teamGUID unique identifier for the Team actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public PersonRolesResponse getLeadershipRolesForTeam(String          serverName,
                                                         String          userId,
                                                         String          teamGUID,
                                                         int             startFrom,
                                                         int             pageSize)
    {
        final String methodName         = "getLeadershipRolesForTeam";
        final String guidParameterName  = "teamGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRolesResponse response = new PersonRolesResponse();
        AuditLog            auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getTeamLeaderRoles(userId,
                                                            teamGUID,
                                                            guidParameterName,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a person role connected to the named team via the TeamMembership relationship.
     *
     * @param serverName called server
     * @param userId calling user
     * @param teamGUID unique identifier for the Team actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public PersonRolesResponse getMembershipRolesForTeam(String          serverName,
                                                         String          userId,
                                                         String          teamGUID,
                                                         int             startFrom,
                                                         int             pageSize)
    {
        final String methodName         = "getMembershipRolesForTeam";
        final String guidParameterName  = "teamGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRolesResponse response = new PersonRolesResponse();
        AuditLog            auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getTeamMemberRoles(userId,
                                                            teamGUID,
                                                            guidParameterName,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of matching roles for the search string.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody RegEx string to search for
     *
     * @return list of matching actor profiles
     *
     *   InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *   PropertyServerException the server is not available.
     *   UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonRolesResponse findPersonRoles(String                  serverName,
                                               String                  userId,
                                               int                     startFrom,
                                               int                     pageSize,
                                               SearchStringRequestBody requestBody)
    {
        final String methodName                 = "findPersonRoles";
        final String searchStringParameterName  = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRolesResponse response = new PersonRolesResponse();
        AuditLog            auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.findPersonRoles(userId,
                                                         requestBody.getSearchString(),
                                                         searchStringParameterName,
                                                         startFrom,
                                                         pageSize,
                                                         false,
                                                         false,
                                                         new Date(), methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
