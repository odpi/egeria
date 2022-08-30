/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;


import org.odpi.openmetadata.accessservices.communityprofile.api.PersonalProfileManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.PersonalProfileUniverse;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodType;
import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * CommunityProfileLeadership provides the client-side interface for the Governance Program Open Metadata Access Service (OMAS).
 * This client, manages all the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the governance program definitions exchanged with this client.
 */
public class PersonalProfileManagement implements PersonalProfileManagementInterface
{
    private final String                     serverName;               /* Initialized in constructor */
    private final String                     serverPlatformURLRoot;    /* Initialized in constructor */
    private final CommunityProfileRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler    invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public PersonalProfileManagement(String serverName,
                                     String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new CommunityProfileRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PersonalProfileManagement(String   serverName,
                                     String   serverPlatformURLRoot,
                                     AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new CommunityProfileRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public PersonalProfileManagement(String serverName,
                                     String serverPlatformURLRoot,
                                     String userId,
                                     String password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new CommunityProfileRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public PersonalProfileManagement(String   serverName,
                                     String   serverPlatformURLRoot,
                                     String   userId,
                                     String   password,
                                     AuditLog auditLog) throws  InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new CommunityProfileRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public PersonalProfileManagement(String                     serverName,
                                     String                     serverPlatformURLRoot,
                                     CommunityProfileRESTClient restClient,
                                     int                        maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = restClient;
    }


    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileUserId userId of the individual whose profile this is.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     *
     * @return Unique identifier for the personal profile.
     *
     * @throws InvalidParameterException the employee number or full name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public String createPersonalProfile(String              userId,
                                        String              externalSourceGUID,
                                        String              externalSourceName,
                                        String              profileUserId,
                                        String              qualifiedName,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "createPersonalProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/personal-profiles";

        final String   profileUserIdParameterName = "profileUserId";
        final String   qualifiedParameterName = "qualifiedName";
        final String   knownNameParameterName = "knownName";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(profileUserId, profileUserIdParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedParameterName, methodName);
        invalidParameterHandler.validateName(knownName, knownNameParameterName, methodName);

        PersonalProfileRequestBody requestBody = new PersonalProfileRequestBody();
        requestBody.setOriginatingSystemGUID(externalSourceGUID);
        requestBody.setOriginatingSystemName(externalSourceName);
        requestBody.setProfileUserId(profileUserId);
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setAdditionalProperties(additionalProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileGUID unique identifier for the profile.
     * @param qualifiedName personnel/serial/unique employee number of the individual. Used to verify the profileGUID.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param profileProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException the known name is null or the qualifiedName does not match the profileGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void   updatePersonalProfile(String              userId,
                                        String              externalSourceGUID,
                                        String              externalSourceName,
                                        String              profileGUID,
                                        String              qualifiedName,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, Object> profileProperties,
                                        Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "updatePersonalProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/personal-profiles/{2}";

        final String   guidParameterName = "profileGUID";
        final String   qualifiedNameParameterName = "qualifiedName";
        final String   knownNameParameterName = "knownName";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(knownName, knownNameParameterName, methodName);

        PersonalProfileRequestBody requestBody = new PersonalProfileRequestBody();
        requestBody.setOriginatingSystemGUID(externalSourceGUID);
        requestBody.setOriginatingSystemName(externalSourceName);
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setProfileProperties(profileProperties);
        requestBody.setAdditionalProperties(additionalProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        profileGUID);

    }


    /**
     * Delete the personal profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileGUID unique identifier for the profile.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @throws InvalidParameterException the qualifiedName or guid is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void deletePersonalProfile(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String profileGUID,
                                      String qualifiedName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName = "deletePersonalProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/personal-profiles/{2}/delete";

        final String   guidParameterName = "profileGUID";
        final String   employeeNumberParameterName = "employeeNumber";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, employeeNumberParameterName, methodName);

        PersonalProfileValidatorRequestBody requestBody = new PersonalProfileValidatorRequestBody();
        requestBody.setOriginatingSystemGUID(externalSourceGUID);
        requestBody.setOriginatingSystemName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        profileGUID);
    }


    /**
     * Return the total karma points for this user.
     *
     * @param userId userId of the user making the request.
     * @param profileUserId userId of the profile to update.
     *
     * @return int count of karma points
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public long getKarmaPoints(String userId,
                               String profileUserId) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        return 0;
    }


    /**
     * Return the list of contact methods for this user.
     *
     * @param userId userId of the user making the request.
     * @param profileUserId userId of the profile to update.
     *
     * @return list of contact methods
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<ContactMethodProperties> getContactMethods(String userId,
                                                           String profileUserId) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Add a new contact method to the requesting user's profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileUserId userId of the profile to update.
     * @param type type of contact method.
     * @param service service for the contact method.
     * @param value account name for the service.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String addContactMethod(String            userId,
                                   String            externalSourceGUID,
                                   String            externalSourceName,
                                   String            profileUserId,
                                   ContactMethodType type,
                                   String            service,
                                   String            value) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Remove an obsolete contact method from the requesting user's profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileUserId userId of the profile to update.
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     * @param type type of contact method. This is used to confirm that the GUID is the right one.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteContactMethod(String            userId,
                                      String            externalSourceGUID,
                                      String            externalSourceName,
                                      String            profileUserId,
                                      String            contactMethodGUID,
                                      ContactMethodType type) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public PersonalProfileUniverse getPersonalProfileByGUID(String userId,
                                                            String profileGUID) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/personal-profiles/{2}";

        final String   guidParameterName = "profileGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);

        PersonalProfileResponse restResult = restClient.callPersonalProfileGetRESTCall(methodName,
                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       profileGUID);

        return restResult.getPersonalProfile();
    }


    /**
     * Retrieve a personal profile by userId.
     *
     * @param userId the name of the calling user.
     * @param profileUserId userId associated with the profile.
     *
     * @return personal profile object.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public PersonalProfileUniverse getPersonalProfileForUser(String        userId,
                                                             String        profileUserId) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileForUser";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/personal-profiles/by-user/{2}";

        final String  profileUserIdParameterName = "profileUserId";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(profileUserId, profileUserIdParameterName, methodName);

        PersonalProfileResponse restResult = restClient.callPersonalProfileGetRESTCall(methodName,
                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       profileUserId);

        return restResult.getPersonalProfile();
    }


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param userId the name of the calling user.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     *
     * @return personal profile object.
     *
     * @throws InvalidParameterException the employee number.
     * @throws PropertyServerException the server is not available, or there is a problem retrieving the profile.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public PersonalProfileUniverse getPersonalProfileByQualifiedName(String userId,
                                                                     String qualifiedName) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByQualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/personal-profiles/by-qualified-name";

        final String   qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(qualifiedName);

        PersonalProfileResponse restResult = restClient.callPersonalProfilePostRESTCall(methodName,
                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       qualifiedName);

        return restResult.getPersonalProfile();
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param userId the name of the calling user.
     * @param name name of individual.
     *
     * @return list of personal profile objects.
     *
     * @throws InvalidParameterException the name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<PersonalProfileUniverse> getPersonalProfilesByName(String userId,
                                                                   String  name) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfilesByName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/personal-profiles/by-name";

        final String   nameParameterName = "name";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(name);

        PersonalProfileListResponse restResult = restClient.callPersonalProfileListPostRESTCall(methodName,
                                                                                               serverPlatformURLRoot + urlTemplate,
                                                                                               requestBody,
                                                                                               serverName,
                                                                                               userId,
                                                                                               name);

        return restResult.getPersonalProfiles();
    }
}
