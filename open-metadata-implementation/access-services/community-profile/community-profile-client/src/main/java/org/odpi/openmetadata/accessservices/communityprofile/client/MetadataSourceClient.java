/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.client;


import org.odpi.openmetadata.accessservices.communityprofile.api.MetadataSourceInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataSourceProperties;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * MetadataSourceClient is the client for setting up the SoftwareServerCapabilities that represent metadata sources
 * that supply information relating to people and organizations.  It is used by the Organization Integrator OMIS
 * to create the metadata source.  It can also be used to properly classify the metadata source entity to clarify
 * whether it is a:
 * <ul>
 *     <li>User Profile Manager - authoritative source of profile (people and organizations) information.</li>
 *     <li>User Access Directory - authoritative source of user identity information.</li>
 *     <li>Master Data Manager - authoritative source of profile (people and organizations) information
 *     aggregated and managed from many systems.</li>
 * </ul>
 */
public class MetadataSourceClient extends CommunityProfileBaseClient implements MetadataSourceInterface
{
    private final NullRequestBody            nullRequestBody         = new NullRequestBody();

    private final String urlTemplatePrefix = baseURLTemplatePrefix + "/metadata-sources";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException bad input parameters
     */
    public MetadataSourceClient(String serverName,
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
    public MetadataSourceClient(String   serverName,
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
    public MetadataSourceClient(String serverName,
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
    public MetadataSourceClient(String   serverName,
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
    public MetadataSourceClient(String                     serverName,
                                String                     serverPlatformURLRoot,
                                CommunityProfileRESTClient restClient,
                                int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* ========================================================
     * The metadata source represents the third party technology this integration processing is connecting to
     */

    /**
     * Create information about the metadata source that is providing user profile information.
     *
     * @param userId calling user
     * @param properties description of the metadata source
     *
     * @return unique identifier of the user profile manager's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String createMetadataSource(String                   userId,
                                       MetadataSourceProperties properties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                  = "createMetadataSource";
        final String propertiesParameterName     = "properties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix;

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Retrieve the unique identifier of the software server capability that describes a metadata source.  This could be
     * a user profile manager, user access directory and/or a master data manager.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the metadata source
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String getMetadataSourceGUID(String userId,
                                        String qualifiedName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                  = "getMetadataSourceGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-name/{2}";

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                 urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 qualifiedName);

        return restResult.getGUID();
    }


    /**
     * Retrieve the properties of the software server capability that describes a metadata source.  This could be
     * a user profile manager, user access directory and/or a master data manager.
     *
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public MetadataSourceElement getMetadataSource(String userId,
                                                   String metadataSourceGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                       = "getMetadataSource";
        final String metadataSourceGUIDParameterName  = "metadataSourceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataSourceGUID, metadataSourceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}";

        MetadataSourceResponse restResult = restClient.callMetadataSourceGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     metadataSourceGUID);

        return restResult.getElement();
    }


    /**
     * Update classification of the metadata source as being capable if managing user profiles.
     *
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void addUserProfileManagerClassification(String userId,
                                                    String metadataSourceGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                       = "addUserProfileManagerClassification";
        final String metadataSourceGUIDParameterName  = "metadataSourceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataSourceGUID, metadataSourceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/user-profile-manager";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        metadataSourceGUID);
    }


    /**
     * Update classification of the metadata source that is providing a user access directory information
     * such as the groups and access rights of a userId.
     *
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void addUserAccessDirectoryClassification(String userId,
                                                     String metadataSourceGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                       = "addUserAccessDirectoryClassification";
        final String metadataSourceGUIDParameterName  = "metadataSourceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataSourceGUID, metadataSourceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/user-access-directory";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        metadataSourceGUID);
    }


    /**
     * Update classification of the metadata source that is a master data manager for user profile information.
     *
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void addMasterDataManagerClassification(String userId,
                                                   String metadataSourceGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                       = "addMasterDataManagerClassification";
        final String metadataSourceGUIDParameterName  = "metadataSourceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataSourceGUID, metadataSourceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/master-data-manager";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        metadataSourceGUID);
    }
}
