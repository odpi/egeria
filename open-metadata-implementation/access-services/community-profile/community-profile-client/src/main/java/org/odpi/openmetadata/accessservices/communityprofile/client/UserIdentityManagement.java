/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.client;


import org.odpi.openmetadata.accessservices.communityprofile.api.UserIdentityManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ProfileIdentityProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentityProperties;
import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * UserIdentityManagement is the client for explicitly managing the user identity entities and associating them with
 * profiles.  It is typically used when the relationship between user identities and profiles are many to one.
 */
public class UserIdentityManagement extends CommunityProfileBaseClient implements UserIdentityManagementInterface
{
    private final String urlTemplatePrefix = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/user-identities";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException bad input parameters
     */
    public UserIdentityManagement(String serverName,
                                  String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public UserIdentityManagement(String   serverName,
                                  String   serverPlatformURLRoot,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public UserIdentityManagement(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
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
     *
     * @throws InvalidParameterException bad input parameters
     */
    public UserIdentityManagement(String   serverName,
                                  String   serverPlatformURLRoot,
                                  String   userId,
                                  String   password,
                                  AuditLog auditLog) throws  InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
    public UserIdentityManagement(String                     serverName,
                                  String                     serverPlatformURLRoot,
                                  CommunityProfileRESTClient restClient,
                                  int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* ========================================================
     * Manage user identities
     */

    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param newIdentity properties for the new userIdentity.
     *
     * @return unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String createUserIdentity(String                 userId,
                                     String                 externalSourceGUID,
                                     String                 externalSourceName,
                                     UserIdentityProperties newIdentity) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName              = "createUserIdentity";
        final String propertiesParameterName = "newIdentity";
        final String urlTemplate             = serverPlatformURLRoot + urlTemplatePrefix;

        return super.createReferenceable(userId, externalSourceGUID, externalSourceName, newIdentity, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a UserIdentity that is for the sole use of a specific actor profile.  When the profile is deleted, the user identity is
     * deleted.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param profileGUID unique identifier of the profile
     * @param newIdentity properties for the new userIdentity
     *
     * @return unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String createUserIdentityForProfile(String                 userId,
                                               String                 externalSourceGUID,
                                               String                 externalSourceName,
                                               String                 profileGUID,
                                               UserIdentityProperties newIdentity) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName              = "createUserIdentityForProfile";
        final String guidParameterName       = "profileGUID";
        final String propertiesParameterName = "newIdentity";
        final String urlTemplate             = serverPlatformURLRoot + urlTemplatePrefix;

        return super.createReferenceableWithParent(userId, externalSourceGUID, externalSourceName, profileGUID, guidParameterName, newIdentity, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Update a UserIdentity.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties updated properties for the new userIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void updateUserIdentity(String                 userId,
                                   String                 externalSourceGUID,
                                   String                 externalSourceName,
                                   String                 userIdentityGUID,
                                   boolean                isMergeUpdate,
                                   UserIdentityProperties properties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName                  = "updateUserIdentity";
        final String guidParameterName           = "userIdentityGUID";
        final String propertiesParameterName     = "properties";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}?isMergeUpdate={3}";

        super.updateReferenceable(userId, externalSourceGUID, externalSourceName, userIdentityGUID, guidParameterName, isMergeUpdate, properties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a user identity object.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void deleteUserIdentity(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String userIdentityGUID) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName        = "deleteUserIdentity";
        final String guidParameterName = "userIdentityGUID";
        final String urlTemplate       = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/delete";

        super.removeReferenceable(userId, externalSourceGUID, externalSourceName, userIdentityGUID, guidParameterName, urlTemplate, methodName);
    }


    /**
     * Link a user identity to a profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param profileGUID the profile to add the identity to.
     * @param userIdentityGUID additional userId for the profile.
     * @param properties the properties that describe how the owner of the profile uses the user identity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void addIdentityToProfile(String                    userId,
                                     String                    externalSourceGUID,
                                     String                    externalSourceName,
                                     String                    userIdentityGUID,
                                     String                    profileGUID,
                                     ProfileIdentityProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName                    = "addIdentityToProfile";
        final String profileGUIDParameterName      = "profileGUID";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String urlTemplate                   = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/profiles/{3}/link";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, userIdentityGUID, userIdentityGUIDParameterName, properties, profileGUID, profileGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Update the properties of the relationship between a user identity and profile.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID additional userId for the profile
     * @param profileGUID the profile to add the identity to
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties the properties that describe how the owner of the profile uses the user identity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void updateProfileIdentity(String                    userId,
                                      String                    externalSourceGUID,
                                      String                    externalSourceName,
                                      String                    userIdentityGUID,
                                      String                    profileGUID,
                                      boolean                   isMergeUpdate,
                                      ProfileIdentityProperties properties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName                    = "updateProfileIdentity";
        final String profileGUIDParameterName      = "profileGUID";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String urlTemplate                   = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/profiles/{3}/link/update?isMergeUpdate={4}";

        super.updateRelationship(userId, externalSourceGUID, externalSourceName, userIdentityGUID, userIdentityGUIDParameterName, properties, profileGUID, profileGUIDParameterName, isMergeUpdate, urlTemplate, methodName);
    }


    /**
     * Unlink a user identity from a profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID profile to remove it from.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void removeIdentityFromProfile(String userId,
                                          String externalSourceGUID,
                                          String externalSourceName,
                                          String userIdentityGUID,
                                          String profileGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName                    = "removeIdentityFromProfile";
        final String profileGUIDParameterName      = "profileGUID";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/profiles/{3}/unlink";

        super.clearRelationship(userId, externalSourceGUID, externalSourceName, userIdentityGUID, userIdentityGUIDParameterName, profileGUID, profileGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
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
    public List<UserIdentityElement> findUserIdentities(String userId,
                                                        String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                 = "findUserIdentities";
        final String searchStringParameterName  = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        UserIdentityListResponse restResult = restClient.callUserIdentityListPostRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          requestBody,
                                                                                          serverName,
                                                                                          userId,
                                                                                          Integer.toString(startFrom),
                                                                                          Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
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
    public List<UserIdentityElement>  getUserIdentitiesByName(String userId,
                                                              String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName         = "getUserIdentitiesByName";
        final String namePropertyName   = "qualifiedName";
        final String nameParameterName  = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(namePropertyName);
        requestBody.setNameParameterName(nameParameterName);

        UserIdentityListResponse restResult = restClient.callUserIdentityListPostRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          requestBody,
                                                                                          serverName,
                                                                                          userId,
                                                                                          Integer.toString(startFrom),
                                                                                          Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public UserIdentityElement getUserIdentityByGUID(String userId,
                                                     String userIdentityGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                    = "getUserIdentityByGUID";
        final String userIdentityGUIDParameterName = "userIdentityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(userIdentityGUID, userIdentityGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}";

        UserIdentityResponse restResult = restClient.callUserIdentityGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 userIdentityGUID);

        return restResult.getElement();
    }
}
