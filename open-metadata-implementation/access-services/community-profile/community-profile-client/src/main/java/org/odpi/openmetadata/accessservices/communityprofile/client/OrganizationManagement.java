/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.OrganizationManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.Appointee;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.PersonRoleAppointee;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;

import java.util.Date;
import java.util.List;


/**
 * OrganizationManagement is the client used by the Organization Integrator OMIS that is responsible with synchronizing organizational
 * structures, profiles rules and users with open metadata.
 */
public class OrganizationManagement extends CommunityProfileBaseClient implements OrganizationManagementInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException bad input parameters
     */
    public OrganizationManagement(String serverName,
                                  String serverPlatformURLRoot,
                                  int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public OrganizationManagement(String   serverName,
                                  String   serverPlatformURLRoot,
                                  AuditLog auditLog,
                                  int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException bad input parameters
     */
    public OrganizationManagement(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password,
                                  int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException bad input parameters
     */
    public OrganizationManagement(String   serverName,
                                  String   serverPlatformURLRoot,
                                  String   userId,
                                  String   password,
                                  AuditLog auditLog,
                                  int      maxPageSize) throws  InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public OrganizationManagement(String                     serverName,
                                  String                     serverPlatformURLRoot,
                                  CommunityProfileRESTClient restClient,
                                  int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Create a definition of an actor profile.  This could be for the whole organization, a team, a person or a system.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param properties properties for a actor profile
     * @param contributionRecord optional properties for the contribution record
     *
     * @return unique identifier of actor profile
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createActorProfile(String                 userId,
                                     String                 externalSourceGUID,
                                     String                 externalSourceName,
                                     ActorProfileProperties properties,
                                     ContributionRecord     contributionRecord) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                  = "createActorProfile";
        final String propertiesParameterName     = "properties";
        final String qualifiedNameParameterName  = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles";

        ActorProfileRequestBody requestBody = new ActorProfileRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);
        requestBody.setContributionRecord(contributionRecord);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId);

        return restResult.getGUID();
    }


    /**
     * Update the definition of an actor profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param actorProfileGUID unique identifier of actor profile
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     * @param contributionRecord additional properties for contribution record.
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateActorProfile(String                 userId,
                                   String                 externalSourceGUID,
                                   String                 externalSourceName,
                                   String                 actorProfileGUID,
                                   boolean                isMergeUpdate,
                                   ActorProfileProperties properties,
                                   ContributionRecord     contributionRecord) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                  = "updateActorProfile";
        final String guidParameterName           = "actorProfileGUID";
        final String propertiesParameterName     = "properties";
        final String qualifiedNameParameterName  = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actorProfileGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/{2}?isMergeUpdate={3}";

        ActorProfileRequestBody requestBody = new ActorProfileRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);
        requestBody.setContributionRecord(contributionRecord);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, actorProfileGUID, isMergeUpdate);
    }


    /**
     * Remove the definition of an actor profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param actorProfileGUID unique identifier of actor profile
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteActorProfile(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String actorProfileGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName        = "deleteActorProfile";
        final String guidParameterName = "actorProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actorProfileGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/{2}/delete";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, actorProfileGUID);
    }


    /**
     * Add a new contact method to the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param actorProfileGUID identifier of the profile to update.
     * @param properties properties of contact method.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String addContactMethod(String                  userId,
                                   String                  externalSourceGUID,
                                   String                  externalSourceName,
                                   String                  actorProfileGUID,
                                   ContactMethodProperties properties) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName                  = "addContactMethod";
        final String propertiesParameterName     = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/{2}/contact-methods";

        ContactMethodRequestBody requestBody = new ContactMethodRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  actorProfileGUID);

        return restResult.getGUID();
    }


    /**
     * Remove an obsolete contact method from the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void deleteContactMethod(String            userId,
                                    String            externalSourceGUID,
                                    String            externalSourceName,
                                    String            contactMethodGUID) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName        = "deleteContactMethod";
        final String guidParameterName = "contactMethodGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(contactMethodGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/contact-methods/{2}/delete";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, contactMethodGUID);
    }


    /**
     * Link two related team/organization actor profiles together as part of a hierarchy.
     * A team/organization actor profile can only have one parent but many child actor profiles.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param superTeamProfileGUID unique identifier of the parent team profile
     * @param subTeamProfileGUID unique identifier of the child team profile
     * @param delegationEscalationAuthority can workflows delegate/escalate through this link?
     * @param effectiveFrom start date for the team relationship
     * @param effectiveTo end date for the team relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void linkTeamsInHierarchy(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  superTeamProfileGUID,
                                     String  subTeamProfileGUID,
                                     boolean delegationEscalationAuthority,
                                     Date    effectiveFrom,
                                     Date    effectiveTo) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName               = "linkTeamsInHierarchy";
        final String profileGUIDParameterName = "superTeamProfileGUID";
        final String subTeamGUIDParameterName = "subTeamProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(superTeamProfileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(subTeamProfileGUID, subTeamGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/{2}/sub-team-profiles/{3}/link?delegationEscalationAuthority={4}";

        EffectiveDatesRequestBody requestBody = new EffectiveDatesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, superTeamProfileGUID, subTeamProfileGUID, delegationEscalationAuthority);
    }


    /**
     * Remove the link between two actor profiles in the actor profile hierarchy.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param superTeamProfileGUID unique identifier of the parent actor profile
     * @param subTeamProfileGUID unique identifier of the child actor profile
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkTeamsInHierarchy(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String superTeamProfileGUID,
                                       String subTeamProfileGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName               = "unlinkTeamsInHierarchy";
        final String profileGUIDParameterName = "superTeamProfileGUID";
        final String subTeamGUIDParameterName = "subTeamProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(superTeamProfileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(subTeamProfileGUID, subTeamGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/{2}/sub-team-profiles/{3}/unlink";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, superTeamProfileGUID, subTeamProfileGUID);
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param userId calling user
     * @param actorProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ActorProfileElement getActorProfileByGUID(String userId,
                                                     String actorProfileGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "getActorProfileByGUID";
        final String guidParameterName = "actorProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actorProfileGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/{2}";

        ActorProfileResponse restResult = restClient.callActorProfileGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 actorProfileGUID);

        return restResult.getElement();
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param userId calling user
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileUserId or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ActorProfileElement getActorProfileByUserId(String userId,
                                                       String actorProfileUserId) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName        = "getActorProfileByGUID";
        final String nameParameterName = "actorProfileUserId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(actorProfileUserId, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/user-ids/{2}";

        ActorProfileResponse restResult = restClient.callActorProfileGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 actorProfileUserId);

        return restResult.getElement();
    }


    /**
     * Return information about a named actor profile.
     *
     * @param userId calling user
     * @param name unique name for the actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ActorProfileElement> getActorProfilesByName(String userId,
                                                            String name,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName         = "getActorProfilesByName";
        final String namePropertyName   = "name";
        final String nameParameterName  = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(namePropertyName);
        requestBody.setNameParameterName(nameParameterName);

        ActorProfilesResponse restResult = restClient.callActorProfilesPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    Integer.toString(startFrom),
                                                                                    Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Return information about all actor profiles.
     *
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ActorProfileElement> getActorProfiles(String userId,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getActorProfiles";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles?startFrom={2}&pageSize={3}";

        ActorProfilesResponse restResult = restClient.callActorProfilesGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   Integer.toString(startFrom),
                                                                                   Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Return information about the actor profiles associated with a location.
     *
     * @param userId calling user
     * @param locationGUID unique identifier for the location
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException locationGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ActorProfileElement> getActorProfilesByLocation(String userId,
                                                                String locationGUID,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName         = "getActorProfileByName";
        final String guidParameterName  = "locationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/by-location/{2}?startFrom={3}&pageSize={4}";


        ActorProfilesResponse restResult = restClient.callActorProfilesGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   locationGUID,
                                                                                   Integer.toString(startFrom),
                                                                                   Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the list of matching profiles for the search string.
     *
     * @param userId the name of the calling user.
     * @param searchString RegEx string to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ActorProfileElement> findActorProfiles(String userId,
                                                       String searchString,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName                 = "findActorProfile";
        final String searchStringParameterName  = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        ActorProfilesResponse restResult = restClient.callActorProfilesPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    Integer.toString(startFrom),
                                                                                    Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Create a definition of a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param properties properties for a person role
     *
     * @return unique identifier of person role
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createPersonRole(String               userId,
                                   String               externalSourceGUID,
                                   String               externalSourceName,
                                   PersonRoleProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                  = "createPersonRole";
        final String propertiesParameterName     = "properties";
        final String qualifiedNameParameterName  = "properties.roleId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getRoleId(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles";

        PersonRoleRequestBody requestBody = new PersonRoleRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId);

        return restResult.getGUID();
    }


    /**
     * Update the definition of a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param personRoleGUID unique identifier of person role
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updatePersonRole(String               userId,
                                 String               externalSourceGUID,
                                 String               externalSourceName,
                                 String               personRoleGUID,
                                 boolean              isMergeUpdate,
                                 PersonRoleProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "updatePersonRole";
        final String guidParameterName           = "personRoleGUID";
        final String propertiesParameterName     = "properties";
        final String qualifiedNameParameterName  = "properties.roleId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getRoleId(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/{2}?isMergeUpdate={3}";

        PersonRoleRequestBody requestBody = new PersonRoleRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, personRoleGUID, isMergeUpdate);
    }


    /**
     * Remove the definition of a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param personRoleGUID unique identifier of person role
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deletePersonRole(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String personRoleGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName                  = "deletePersonRole";
        final String guidParameterName           = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/{2}/delete";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, personRoleGUID);
    }


    /**
     * Link a person role to a person profile to show that that person is performing the role.
     *
     * @param userId               calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID    unique identifier of the person profile
     * @param properties           optional properties for the appointment
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String linkPersonRoleToProfile(String                userId,
                                          String                externalSourceGUID,
                                          String                externalSourceName,
                                          String                personRoleGUID,
                                          String                personProfileGUID,
                                          AppointmentProperties properties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                  = "linkPersonRoleToProfile";
        final String profileGUIDParameterName    = "personProfileGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personProfileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, personRoleGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/{2}/profiles/{3}/link";

        AppointmentRequestBody requestBody = new AppointmentRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, personRoleGUID, personProfileGUID);

        return response.getGUID();
    }


    /**
     * Return the list of people appointed to a particular role.
     *
     * @param userId               calling user
     * @param personRoleGUID       unique identifier of the person role
     * @param effectiveTime        time for appointments, null for full appointment history
     * @param startFrom            index of the list to start from (0 for start)
     * @param pageSize             maximum number of elements to return
     *
     * @return list of appointees
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<Appointee> getAppointees(String userId,
                                         String personRoleGUID,
                                         Date   effectiveTime,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                  = "getAppointees";
        final String personRoleGUIDParameterName = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, personRoleGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/{2}/appointees?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        AppointeesResponse restResult = restClient.callAppointeesPostRESTCall(methodName,
                                                                              urlTemplate,
                                                                              requestBody,
                                                                              serverName,
                                                                              userId,
                                                                              personRoleGUID,
                                                                              Integer.toString(startFrom),
                                                                              Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Update the properties for the appointment of a person to a role.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param appointmentGUID unique identifier of the appointment relationship
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateAppointment(String                userId,
                                  String                externalSourceGUID,
                                  String                externalSourceName,
                                  String                appointmentGUID,
                                  boolean               isMergeUpdate,
                                  AppointmentProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                  = "updatePersonRole";
        final String guidParameterName           = "personRoleGUID";
        final String propertiesParameterName     = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(appointmentGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/appointees/{2}?isMergeUpdate={3}";

        AppointmentRequestBody requestBody = new AppointmentRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, appointmentGUID, isMergeUpdate);

    }


    /**
     * Remove the link between a person role and a person profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param appointmentGUID unique identifier of the appointment relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkPersonRoleFromProfile(String userId,
                                            String externalSourceGUID,
                                            String externalSourceName,
                                            String appointmentGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName        = "unlinkPersonRoleFromProfile";
        final String guidParameterName = "appointmentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(appointmentGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/appointees/{2}/unlink";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, appointmentGUID);
    }


    /**
     * Link a team leader person role or team member person role to a team profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param position position name of the role in the team
     * @param leadershipPosition is this a leadership position
     *
     * @throws InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void linkTeamPlayer(String  userId,
                               String  externalSourceGUID,
                               String  externalSourceName,
                               String  teamRoleGUID,
                               String  teamProfileGUID,
                               String  position,
                               boolean leadershipPosition) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                  = "linkTeamPlayer";
        final String profileGUIDParameterName    = "teamProfileGUID";
        final String personRoleGUIDParameterName = "teamRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamProfileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(teamRoleGUID, personRoleGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/team-profiles/{2}/team-roles/{3}/link";

        TeamPlayerRequestBody requestBody = new TeamPlayerRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setPosition(position);
        requestBody.setIsLeadershipRole(leadershipPosition);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, teamProfileGUID, teamRoleGUID);
    }


    /**
     * Remove the link between a person role and a team profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param leadershipPosition is this a leadership position
     *
     * @throws InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkTeamPlayer(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  teamRoleGUID,
                                 String  teamProfileGUID,
                                 boolean leadershipPosition) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "unlinkTeamPlayer";
        final String profileGUIDParameterName    = "teamProfileGUID";
        final String personRoleGUIDParameterName = "teamRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamProfileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(teamRoleGUID, personRoleGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/team-profiles/{2}/team-roles/{3}/unlink";

        TeamPlayerRequestBody requestBody = new TeamPlayerRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setIsLeadershipRole(leadershipPosition);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, teamProfileGUID, teamRoleGUID);
    }


    /**
     * Return information about a specific person role.
     *
     * @param userId calling user
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *
     * @throws InvalidParameterException personRoleGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public PersonRoleElement getPersonRoleByGUID(String userId,
                                                 String personRoleGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName        = "getPersonRoleByGUID";
        final String guidParameterName = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/{2}";

        PersonRoleResponse restResult = restClient.callPersonRoleGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             personRoleGUID);

        return restResult.getElement();
    }


    /**
     * Return information about a named person role.
     *
     * @param userId calling user
     * @param name unique name for the actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<PersonRoleElement> getPersonRolesByName(String userId,
                                                        String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName         = "getPersonRolesByName";
        final String namePropertyName   = "name";
        final String nameParameterName  = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(namePropertyName);
        requestBody.setNameParameterName(nameParameterName);

        PersonRolesResponse restResult = restClient.callPersonRolesPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                Integer.toString(startFrom),
                                                                                Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Return information about the leadership person roles linked to a team.
     *
     * @param userId calling user
     * @param teamGUID unique identifier for the Team actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<PersonRoleElement> getLeadershipRolesForTeam(String userId,
                                                             String teamGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "getLeadershipRolesForTeam";
        final String guidPropertyName  = "teamGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamGUID, guidPropertyName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/by-team/{2}/leadership?startFrom={3}&pageSize={4}";

        PersonRolesResponse restResult = restClient.callPersonRolesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               teamGUID,
                                                                               Integer.toString(startFrom),
                                                                               Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Return information about the membership person roles linked to a team.
     *
     * @param userId calling user
     * @param teamGUID unique identifier for the Team actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<PersonRoleElement> getMembershipRolesForTeam(String userId,
                                                             String teamGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "getMembershipRolesForTeam";
        final String guidPropertyName  = "teamGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamGUID, guidPropertyName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/by-team/{2}/membership?startFrom={3}&pageSize={4}";

        PersonRolesResponse restResult = restClient.callPersonRolesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               teamGUID,
                                                                               Integer.toString(startFrom),
                                                                               Integer.toString(pageSize));

        return restResult.getElements();
    }



    /**
     * Retrieve the list of matching roles for the search string.
     *
     * @param userId the name of the calling user.
     * @param searchString RegEx string to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<PersonRoleElement> findPersonRoles(String userId,
                                                   String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName                 = "findPersonRoles";
        final String searchStringParameterName  = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        PersonRolesResponse restResult = restClient.callPersonRolesPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                Integer.toString(startFrom),
                                                                                Integer.toString(pageSize));

        return restResult.getElements();
    }
}
