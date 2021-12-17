/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.HostManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.HostElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.HostProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * HostManagerClient supports the APIs to maintain hosts and their related objects.
 */
public class HostManagerClient implements HostManagerInterface
{
    private static final String assetURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/it-infrastructure/users/{1}/assets";

    private static final String hostEntityURL = "/Host";
    private static final String hostedHostRelationship = "HostedHost";
    private static final String hostClusterMemberRelationship = "HostClusterMember";

    private String   serverName;               /* Initialized in constructor */
    private String   serverPlatformURLRoot;    /* Initialized in constructor */

    private InvalidParameterHandler     invalidParameterHandler = new InvalidParameterHandler();
    private ITInfrastructureRESTClient  restClient;               /* Initialized in constructor */

    private NullRequestBody nullRequestBody = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String   serverName,
                             String   serverPlatformURLRoot,
                             AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String serverName,
                             String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String                     serverName,
                             String                     serverPlatformURLRoot,
                             ITInfrastructureRESTClient restClient,
                             int                        maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }



    /* =====================================================================================================================
     * The host describes the computer or container that provides the operating system for the platforms.
     */


    /**
     * Create a new metadata element to represent a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the host be marked as owned by the infrastructure manager so others can not update?
     * @param hostProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createHost(String         userId,
                             String         infrastructureManagerGUID,
                             String         infrastructureManagerName,
                             boolean        infrastructureManagerIsHome,
                             HostProperties hostProperties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName                  = "createHost";
        final String propertiesParameterName     = "hostProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(hostProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(hostProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "?infrastructureManagerIsHome={2}";

        AssetProperties assetProperties = hostProperties.cloneToAsset();

        AssetRequestBody requestBody = new AssetRequestBody(assetProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  infrastructureManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a host using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the host be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createHostFromTemplate(String             userId,
                                         String             infrastructureManagerGUID,
                                         String             infrastructureManagerName,
                                         boolean            infrastructureManagerIsHome,
                                         String             templateGUID,
                                         TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                  = "createHostFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/from-template/{2}?infrastructureManagerIsHome={3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  infrastructureManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param hostProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateHost(String         userId,
                           String         infrastructureManagerGUID,
                           String         infrastructureManagerName,
                           String         hostGUID,
                           boolean        isMergeUpdate,
                           HostProperties hostProperties) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                  = "updateHost";
        final String elementGUIDParameterName    = "hostGUID";
        final String propertiesParameterName     = "hostProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(hostProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(hostProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        AssetProperties assetProperties = hostProperties.cloneToAsset();

        AssetRequestBody requestBody = new AssetRequestBody(assetProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        hostGUID,
                                        isMergeUpdate);
    }



    /**
     * Create a relationship between a host and a hosted host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the host
     * @param hostedHostGUID unique identifier of the hosted host
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupHostedHost(String userId,
                                String infrastructureManagerGUID,
                                String infrastructureManagerName,
                                String hostGUID,
                                String hostedHostGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName                  = "setupHostedHost";
        final String hostGUIDParameterName       = "hostGUID";
        final String hostedHostGUIDParameterName = "hostedHostGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, hostGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(hostedHostGUID, hostedHostGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + hostedHostRelationship + "/{3}";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        hostGUID,
                                        hostedHostGUID);
    }


    /**
     * Remove a relationship between a host and a hosted host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the host
     * @param hostedHostGUID unique identifier of the hosted host
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearHostedHost(String userId,
                                String infrastructureManagerGUID,
                                String infrastructureManagerName,
                                String hostGUID,
                                String hostedHostGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName                  = "clearHostedHost";
        final String hostGUIDParameterName       = "hostGUID";
        final String hostedHostGUIDParameterName = "hostedHostGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, hostGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(hostedHostGUID, hostedHostGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + hostedHostRelationship + "/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        hostGUID,
                                        hostedHostGUID);
    }


    /**
     * Create a relationship between a host and an cluster member host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the host
     * @param clusterMemberGUID unique identifier of the cluster member host
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupClusterMember(String userId,
                                   String infrastructureManagerGUID,
                                   String infrastructureManagerName,
                                   String hostGUID,
                                   String clusterMemberGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                     = "setupClusterMember";
        final String hostGUIDParameterName          = "hostGUID";
        final String clusterMemberGUIDParameterName = "clusterMemberGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, hostGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(clusterMemberGUID, clusterMemberGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + hostClusterMemberRelationship + "/{3}";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        hostGUID,
                                        clusterMemberGUID);
    }


    /**
     * Remove a relationship between a host and an cluster member host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the host
     * @param clusterMemberGUID unique identifier of the cluster member host
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearClusterMember(String userId,
                                   String infrastructureManagerGUID,
                                   String infrastructureManagerName,
                                   String hostGUID,
                                   String clusterMemberGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                     = "clearClusterMember";
        final String hostGUIDParameterName          = "hostGUID";
        final String clusterMemberGUIDParameterName = "clusterMemberGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, hostGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(clusterMemberGUID, clusterMemberGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + hostClusterMemberRelationship + "/{3/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        hostGUID,
                                        clusterMemberGUID);
    }



    /**
     * Update the zones for the host asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void publishHost(String userId,
                            String hostGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException
    {
        final String methodName               = "publishHost";
        final String elementGUIDParameterName = "hostGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        hostGUID);
    }


    /**
     * Update the zones for the host asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the host is first created).
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void withdrawHost(String userId,
                             String hostGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        final String methodName               = "withdrawHost";
        final String elementGUIDParameterName = "hostGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        hostGUID);
    }


    /**
     * Remove the metadata element representing a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeHost(String userId,
                           String infrastructureManagerGUID,
                           String infrastructureManagerName,
                           String hostGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        final String methodName = "removeHost";
        final String elementGUIDParameterName  = "hostGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(hostGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        hostGUID);
    }



    /**
     * Retrieve the list of host metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param effectiveTime effective time for the query
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
    public List<HostElement> findHosts(String userId,
                                       String searchString,
                                       Date   effectiveTime,
                                       int    startFrom,
                                       int    pageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName                = "findHosts";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + hostEntityURL + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return this.convertAssetElements(restResult.getElementList());
    }


    /**
     * Retrieve the list of host metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param effectiveTime effective time for the query
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
    public List<HostElement> getHostsByName(String userId,
                                            String name,
                                            Date   effectiveTime,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName        = "getHostsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + hostEntityURL + "?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return this.convertAssetElements(restResult.getElementList());
    }


    /**
     * Retrieve the list of hosts created by this caller.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param effectiveTime effective time for the query
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
    public List<HostElement> getHostsForInfrastructureManager(String userId,
                                                              String infrastructureManagerGUID,
                                                              String infrastructureManagerName,
                                                              Date   effectiveTime,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getHostsForInfrastructureManager";
        final String infrastructureManagerGUIDParameterName = "infrastructureManagerGUID";
        final String infrastructureManagerNameParameterName = "infrastructureManagerName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(infrastructureManagerGUID, infrastructureManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(infrastructureManagerName, infrastructureManagerNameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + hostEntityURL + "/infrastructureManagers/{2}/{3}/hosts?startFrom={4}&pageSize={5}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            infrastructureManagerGUID,
                                                                            infrastructureManagerName,
                                                                            startFrom,
                                                                            validatedPageSize);

        return this.convertAssetElements(restResult.getElementList());
    }


    /**
     * Retrieve the host metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public HostElement getHostByGUID(String userId,
                                     String guid) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName = "getHostByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + hostEntityURL + "/{2}";

        AssetResponse restResult = restClient.callAssetGetRESTCall(methodName,
                                                                   urlTemplate,
                                                                   serverName,
                                                                   userId,
                                                                   guid);

        return this.convertAssetElement(restResult.getElement());
    }


    /**
     * Return the list of hosts hosted by another host.
     *
     * @param userId calling user
     * @param supportingHostGUID unique identifier of the host to query
     * @param effectiveTime effective time for the query
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
    public List<HostElement> getHostedHosts(String userId,
                                            String supportingHostGUID,
                                            Date   effectiveTime,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "getHostedHosts";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + hostedHostRelationship + "?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            supportingHostGUID,
                                                                            startFrom,
                                                                            validatedPageSize);

        return this.convertAssetElements(restResult.getElementList());
    }


    /**
     * Return the list of cluster members associated with a host.
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the host to query
     * @param effectiveTime effective time for the query
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
    public List<HostElement> getClusterMembersForHost(String userId,
                                                      String hostGUID,
                                                      Date   effectiveTime,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getClusterMembersForHost";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + hostClusterMemberRelationship + "?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            hostGUID,
                                                                            startFrom,
                                                                            validatedPageSize);

        return this.convertAssetElements(restResult.getElementList());
    }


    /**
     * Convert a list of AssetElements into a list of HostElements.
     *
     * @param assetElements returned assets
     * @return result for caller
     */
    private List<HostElement> convertAssetElements(List<AssetElement> assetElements)
    {
        if (assetElements != null)
        {
            List<HostElement> hostElements = new ArrayList<>();

            for (AssetElement assetElement : assetElements)
            {
                hostElements.add(this.convertAssetElement(assetElement));
            }

            if (! hostElements.isEmpty())
            {
                return hostElements;
            }
        }

        return null;
    }


    /**
     * Convert a single AssetElement to a HostElement.
     *
     * @param assetElement return asset
     * @return result for caller
     */
    private HostElement convertAssetElement(AssetElement assetElement)
    {
        if (assetElement != null)
        {
            return new HostElement(assetElement);
        }

        return null;
    }
}
