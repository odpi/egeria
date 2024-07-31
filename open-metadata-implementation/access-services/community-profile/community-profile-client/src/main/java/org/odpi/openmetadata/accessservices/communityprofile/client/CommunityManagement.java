/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.CommunityManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.accessservices.communityprofile.properties.TemplateProperties;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CommunityElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;

import java.util.List;

/**
 * CommunityManagerClient supports the APIs to maintain communities and their related objects.
 * It issues REST API calls to the Open Metadata Server running Community Profile OMAS that have a URL that begins:
 * <i>serverPlatformURLRoot</i>/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}/communities
 */
public class CommunityManagement extends CommunityProfileBaseClient implements CommunityManagementInterface
{
    private static final String communityURLTemplatePrefix = baseURLTemplatePrefix + "/communities";


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
    public CommunityManagement(String   serverName,
                               String   serverPlatformURLRoot,
                               AuditLog auditLog,
                               int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CommunityManagement(String serverName,
                               String serverPlatformURLRoot,
                               int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CommunityManagement(String serverName,
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CommunityManagement(String   serverName,
                               String   serverPlatformURLRoot,
                               String   userId,
                               String   password,
                               AuditLog auditLog,
                               int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CommunityManagement(String                     serverName,
                               String                     serverPlatformURLRoot,
                               CommunityProfileRESTClient restClient,
                               int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* =====================================================================================================================
     * A Community is a cross-organization group of people sharing knowledge, creating common artifacts and collaborating.
     */

    /**
     * Create a new metadata element to represent a community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityProperties properties about the community to store.  The qualifiedName property must be supplied and must be unique.
     *
     * @return unique identifier of the new community
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createCommunity(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  CommunityProperties communityProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName                  = "createCommunity";
        final String propertiesParameterName     = "communityProperties";
        final String urlTemplate = serverPlatformURLRoot + communityURLTemplatePrefix;

        return super.createReferenceable(userId, externalSourceGUID, externalSourceName, communityProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new community
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createCommunityFromTemplate(String             userId,
                                              String             externalSourceGUID,
                                              String             externalSourceName,
                                              String             templateGUID,
                                              TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName  = "createCommunityFromTemplate";
        final String urlTemplate = serverPlatformURLRoot + communityURLTemplatePrefix + "/from-template/{2}";

        return super.createReferenceableFromTemplate(userId, externalSourceGUID, externalSourceName, templateGUID, templateProperties, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param communityProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateCommunity(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              communityGUID,
                                boolean             isMergeUpdate,
                                CommunityProperties communityProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                  = "updateCommunity";
        final String elementGUIDParameterName    = "communityGUID";
        final String propertiesParameterName     = "communityProperties";
        final String urlTemplate = serverPlatformURLRoot + communityURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        super.updateReferenceable(userId, externalSourceGUID, externalSourceName, communityGUID, elementGUIDParameterName, isMergeUpdate, communityProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a membership relationship between a community and a person role to show that anyone appointed to the role is a member of the community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityGUID unique identifier of the community
     * @param membershipProperties describes the permissions that the role has in the community
     * @param personRoleGUID unique identifier of the person role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupCommunityRole(String                        userId,
                                   String                        externalSourceGUID,
                                   String                        externalSourceName,
                                   String                        communityGUID,
                                   CommunityMembershipProperties membershipProperties,
                                   String                        personRoleGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                  = "setupCommunityRole";
        final String communityGUIDParameterName  = "communityGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";

        final String urlTemplate = serverPlatformURLRoot + communityURLTemplatePrefix + "/{2}/community-roles/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, communityGUID, communityGUIDParameterName, membershipProperties, personRoleGUID, personRoleGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a membership relationship between a community and a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityGUID unique identifier of the community
     * @param personRoleGUID unique identifier of the person role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearCommunityRole(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String communityGUID,
                                   String personRoleGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                  = "clearCommunityRole";
        final String communityGUIDParameterName  = "communityGUID";
        final String personRoleGUIDParameterName = "personRoleGUID";
        final String urlTemplate                 = serverPlatformURLRoot + communityURLTemplatePrefix + "/{2}/community-roles/{3}/delete";

        super.clearRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                communityGUID,
                                communityGUIDParameterName,
                                personRoleGUID,
                                personRoleGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove the metadata element representing a community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeCommunity(String userId,
                                String externalSourceGUID,
                                String externalSourceName,
                                String communityGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName               = "removeCommunity";
        final String elementGUIDParameterName = "communityGUID";
        final String urlTemplate              = serverPlatformURLRoot + communityURLTemplatePrefix + "/{2}/delete";

        super.removeReferenceable(userId, externalSourceGUID, externalSourceName, communityGUID, elementGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<CommunityElement> findCommunities(String userId,
                                                  String searchString,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                = "findCommunity";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + communityURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        CommunitiesResponse restResult = restClient.callCommunitiesPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<CommunityElement> getCommunitiesByName(String userId,
                                                       String name,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName        = "getCommunityByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + communityURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        CommunitiesResponse restResult = restClient.callCommunitiesPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of all communities defined in open metadata.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<CommunityElement> getCommunities(String userId,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "getCommunities";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + communityURLTemplatePrefix + "?startFrom={2}&pageSize={3}";

        CommunitiesResponse restResult = restClient.callCommunitiesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElements();
    }



    /**
     * Return information about the person roles linked to a community.
     *
     * @param userId calling user
     * @param communityGUID unique identifier for the community
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
    public List<PersonRoleElement> getRolesForCommunity(String userId,
                                                        String communityGUID,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName        = "getRolesForCommunity";
        final String guidPropertyName  = "communityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(communityGUID, guidPropertyName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/by-community/{2}?startFrom={3}&pageSize={4}";

        PersonRolesResponse restResult = restClient.callPersonRolesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               communityGUID,
                                                                               Integer.toString(startFrom),
                                                                               Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param communityGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public CommunityElement getCommunityByGUID(String userId,
                                               String communityGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getCommunityByGUID";
        final String guidParameterName = "communityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(communityGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + communityURLTemplatePrefix + "/{2}";

        CommunityResponse restResult = restClient.callCommunityGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           communityGUID);

        return restResult.getElement();
    }
}
