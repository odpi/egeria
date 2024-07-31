/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.ProfileIdentityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ITProfileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactMethodProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ProfileIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.api.ITProfileManagerInterface;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;


/**
 * OrganizationManagement is the client used by the Organization Integrator OMIS that is responsible with synchronizing organizational
 * structures, profiles rules and users with open metadata.
 */
public class ITProfileManagerClient implements ITProfileManagerInterface
{
    private final String                     serverName;               /* Initialized in constructor */
    private final String                     serverPlatformURLRoot;    /* Initialized in constructor */
    private final ITInfrastructureRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler    invalidParameterHandler = new InvalidParameterHandler();

    private final String urlTemplatePrefix = "/servers/{0}/open-metadata/access-services/it-infrastructure/users/{1}";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException bad input parameters
     */
    public ITProfileManagerClient(String serverName,
                                  String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot);
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
    public ITProfileManagerClient(String   serverName,
                                  String   serverPlatformURLRoot,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, auditLog);
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
    public ITProfileManagerClient(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, userId, password);
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
    public ITProfileManagerClient(String   serverName,
                                  String   serverPlatformURLRoot,
                                  String   userId,
                                  String   password,
                                  AuditLog auditLog) throws  InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
    public ITProfileManagerClient(String                     serverName,
                                  String                     serverPlatformURLRoot,
                                  ITInfrastructureRESTClient restClient,
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
     * Create a definition of an IT profile.  If the itInfrastructureGUID is provided, it is connected to the infrastructure element that the
     * profile describes using the ITInfrastructureProfile relationship.  If the itUserId is specified, a UserIdentity for that userId is
     * found/created and connected to the new IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itInfrastructureGUID unique identifier of the piece of IT infrastructure that is described by the new IT profile.
     * @param itUserId            user Id used by the IT Infrastructure
     * @param properties          properties for a IT profile
     *
     * @return unique identifier of IT profile
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createITProfile(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              itInfrastructureGUID,
                                  String              itUserId,
                                  ITProfileProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "createITProfile";
        final String propertiesParameterName     = "properties";
        final String qualifiedNameParameterName  = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles";

        ITProfileRequestBody requestBody = new ITProfileRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setItInfrastructureGUID(itInfrastructureGUID);
        requestBody.setItUserId(itUserId);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId);

        return restResult.getGUID();
    }


    /**
     * Update the definition of an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itProfileGUID unique identifier of IT profile
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateITProfile(String                 userId,
                                String                 externalSourceGUID,
                                String                 externalSourceName,
                                String                 itProfileGUID,
                                boolean                isMergeUpdate,
                                ITProfileProperties properties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName                  = "updateITProfile";
        final String guidParameterName           = "itProfileGUID";
        final String propertiesParameterName     = "properties";
        final String qualifiedNameParameterName  = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles/{2}?isMergeUpdate={3}";

        ITProfileRequestBody requestBody = new ITProfileRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, itProfileGUID, isMergeUpdate);
    }


    /**
     * Remove the definition of an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itProfileGUID unique identifier of IT profile
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteITProfile(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String itProfileGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName        = "deleteITProfile";
        final String guidParameterName = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles/{2}/delete";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, itProfileGUID);
    }


    /**
     * Add a new contact method to the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itProfileGUID identifier of the profile to update.
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
                                   String                  itProfileGUID,
                                   ContactMethodProperties properties) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName                  = "addContactMethod";
        final String propertiesParameterName     = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles/{2}/contact-methods";

        ContactMethodRequestBody requestBody = new ContactMethodRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  itProfileGUID);

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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles/contact-methods/{2}/delete";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, contactMethodGUID);
    }



    /**
     * Link a piece of infrastructure to an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itInfrastructureGUID unique identifier of the IT Infrastructure element
     * @param itProfileGUID unique identifier of the IT profile
     * @param effectiveFrom start date for the  relationship
     * @param effectiveTo end date for the relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void linkITInfrastructureToProfile(String userId,
                                              String externalSourceGUID,
                                              String externalSourceName,
                                              String itInfrastructureGUID,
                                              String itProfileGUID,
                                              Date   effectiveFrom,
                                              Date   effectiveTo) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                        = "linkITInfrastructureToProfile";
        final String itInfrastructureGUIDParameterName = "itInfrastructureGUID";
        final String itProfileGUIDParameterName        = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itInfrastructureGUID, itInfrastructureGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, itProfileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/assets/{2}/profiles/{3}/link";

        EffectiveDatesRequestBody requestBody = new EffectiveDatesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, itInfrastructureGUID, itProfileGUID);
    }


    /**
     * Update the effectivity dates of a link from a piece of infrastructure to an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itInfrastructureGUID unique identifier of the IT Infrastructure element
     * @param itProfileGUID unique identifier of the IT profile
     * @param effectiveFrom start date for the  relationship
     * @param effectiveTo end date for the relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateITInfrastructureToProfile(String userId,
                                                String externalSourceGUID,
                                                String externalSourceName,
                                                String itInfrastructureGUID,
                                                String itProfileGUID,
                                                Date   effectiveFrom,
                                                Date   effectiveTo) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                        = "updateITInfrastructureToProfile";
        final String itInfrastructureGUIDParameterName = "itInfrastructureGUID";
        final String itProfileGUIDParameterName        = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itInfrastructureGUID, itInfrastructureGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, itProfileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/assets/{2}/profiles/{3}/link/update";

        EffectiveDatesRequestBody requestBody = new EffectiveDatesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, itInfrastructureGUID, itProfileGUID);
    }


    /**
     * Remove the link between a piece of infrastructure to an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itInfrastructureGUID unique identifier of the IT Infrastructure element
     * @param itProfileGUID unique identifier of the IT profile
     * @param effectiveTime time that the relationship is active - null for any time
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkITInfrastructureFromProfile(String userId,
                                                  String externalSourceGUID,
                                                  String externalSourceName,
                                                  String itInfrastructureGUID,
                                                  String itProfileGUID,
                                                  Date   effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                        = "unlinkITInfrastructureFromProfile";
        final String itInfrastructureGUIDParameterName = "itInfrastructureGUID";
        final String itProfileGUIDParameterName        = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itInfrastructureGUID, itInfrastructureGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, itProfileGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/assets/{2}/profiles/{3}/unlink";

        EffectiveTimeQueryRequestBody requestBody = new EffectiveTimeQueryRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, itInfrastructureGUID, itProfileGUID);
    }


    /**
     * Return information about a specific IT profile.
     *
     * @param userId calling user
     * @param itProfileGUID unique identifier for the IT profile
     *
     * @return properties of the IT profile
     *
     * @throws InvalidParameterException itProfileGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ITProfileElement getITProfileByGUID(String userId,
                                               String itProfileGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName        = "getITProfileByGUID";
        final String guidParameterName = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles/{2}";

        ITProfileResponse restResult = restClient.callITProfileGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 itProfileGUID);

        return restResult.getElement();
    }


    /**
     * Return information about a specific IT profile.
     *
     * @param userId calling user
     * @param itProfileUserId unique identifier for the IT profile
     *
     * @return properties of the IT profile
     *
     * @throws InvalidParameterException itProfileUserId or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ITProfileElement getITProfileByUserId(String userId,
                                                       String itProfileUserId) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName        = "getITProfileByGUID";
        final String nameParameterName = "itProfileUserId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(itProfileUserId, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles/user-ids/{2}";

        ITProfileResponse restResult = restClient.callITProfileGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 itProfileUserId);

        return restResult.getElement();
    }


    /**
     * Return information about a named IT profile.
     *
     * @param userId calling user
     * @param name unique name for the IT profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching IT profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ITProfileElement> getITProfileByName(String userId,
                                                           String name,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName         = "getITProfileByName";
        final String namePropertyName   = "name";
        final String nameParameterName  = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(namePropertyName);
        requestBody.setNameParameterName(nameParameterName);

        ITProfilesResponse restResult = restClient.callITProfileListPostRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 requestBody,
                                                                                 serverName,
                                                                                 userId,
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
     * @return list of matching IT profiles
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ITProfileElement> findITProfile(String userId,
                                                      String searchString,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName                 = "findITProfile";
        final String searchStringParameterName  = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/profiles/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        ITProfilesResponse restResult = restClient.callITProfileListPostRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 requestBody,
                                                                                 serverName,
                                                                                 userId,
                                                                                 Integer.toString(startFrom),
                                                                                 Integer.toString(pageSize));

        return restResult.getElements();
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
        final String methodName                  = "createUserIdentity";
        final String propertiesParameterName     = "newIdentity";
        final String qualifiedNameParameterName  = "newIdentity.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(newIdentity, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(newIdentity.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix;

        UserIdentityRequestBody requestBody = new UserIdentityRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(newIdentity);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId);

        return restResult.getGUID();
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
        final String qualifiedNameParameterName  = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(userIdentityGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}?isMergeUpdate={3}";

        UserIdentityRequestBody requestBody = new UserIdentityRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, userIdentityGUID, isMergeUpdate);
    }


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
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
        final String methodName                  = "deleteUserIdentity";
        final String guidParameterName           = "userIdentityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(userIdentityGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/delete";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, userIdentityGUID);
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(userIdentityGUID, userIdentityGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/profiles/{3}/link";

        ProfileIdentityRequestBody requestBody = new ProfileIdentityRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, userIdentityGUID, profileGUID);
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(userIdentityGUID, userIdentityGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/profiles/{3}/link/update?isMergeUpdate={4}";

        ProfileIdentityRequestBody requestBody = new ProfileIdentityRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, userIdentityGUID, profileGUID, isMergeUpdate);

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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(userIdentityGUID, userIdentityGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/profiles/{3}/unlink";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, serverName, userId, userIdentityGUID, profileGUID);
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

        UserIdentitiesResponse restResult = restClient.callUserIdentityListPostRESTCall(methodName,
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

        UserIdentitiesResponse restResult = restClient.callUserIdentityListPostRESTCall(methodName,
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
