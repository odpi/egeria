/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.EndpointManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * EndpointManagerClient supports the APIs to maintain connections and their related objects.
 */
public class EndpointManagerClient implements EndpointManagerInterface
{
    private static final String endpointURLTemplatePrefix      = "/servers/{0}/open-metadata/access-services/it-infrastructure/users/{1}/endpoints";

    private final String serverName;               /* Initialized in constructor */
    private final String serverPlatformURLRoot;    /* Initialized in constructor */

    private final InvalidParameterHandler    invalidParameterHandler = new InvalidParameterHandler();
    private final ITInfrastructureRESTClient restClient;               /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EndpointManagerClient(String   serverName,
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EndpointManagerClient(String serverName,
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EndpointManagerClient(String serverName,
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EndpointManagerClient(String   serverName,
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EndpointManagerClient(String                serverName,
                                 String                serverPlatformURLRoot,
                                 ITInfrastructureRESTClient restClient,
                                 int                   maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }


    /**
     * Create a new metadata element to represent an endpoint
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureGUID unique identifier of the infrastructure to connect it to (optional)
     * @param endpointProperties properties about the endpoint to store
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createEndpoint(String             userId,
                                 String             infrastructureManagerGUID,
                                 String             infrastructureManagerName,
                                 String             infrastructureGUID,
                                 EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                  = "createEndpoint";
        final String propertiesParameterName     = "endpointProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(endpointProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(endpointProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix;
        final String urlTemplateWithGUID = serverPlatformURLRoot + endpointURLTemplatePrefix + "/for-infrastructure/{2}";

        EndpointRequestBody requestBody = new EndpointRequestBody(endpointProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        GUIDResponse restResult;

        if (infrastructureGUID == null)
        {
            restResult = restClient.callGUIDPostRESTCall(methodName,
                                                         urlTemplate,
                                                         requestBody,
                                                         serverName,
                                                         userId);
        }
        else
        {
            restResult = restClient.callGUIDPostRESTCall(methodName,
                                                         urlTemplateWithGUID,
                                                         requestBody,
                                                         serverName,
                                                         userId,
                                                         infrastructureGUID);
        }

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureGUID unique identifier of the infrastructure to connect it to (optional)
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createEndpointFromTemplate(String             userId,
                                             String             infrastructureManagerGUID,
                                             String             infrastructureManagerName,
                                             String             infrastructureGUID,
                                             String             networkAddress,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                  = "createEndpointFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";
        final String networkAddressParameterName = "networkAddress";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(networkAddress, networkAddressParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix + "/network-address/{2}/from-template/{3}";
        final String urlTemplateWithGUID = serverPlatformURLRoot + endpointURLTemplatePrefix + "/for-infrastructure/{2}/network-address/{3}/from-template/{3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        GUIDResponse restResult;

        if (infrastructureGUID == null)
        {
            restResult = restClient.callGUIDPostRESTCall(methodName,
                                                         urlTemplate,
                                                         requestBody,
                                                         serverName,
                                                         userId,
                                                         networkAddress,
                                                         templateGUID);
        }
        else
        {
            restResult = restClient.callGUIDPostRESTCall(methodName,
                                                         urlTemplateWithGUID,
                                                         requestBody,
                                                         serverName,
                                                         userId,
                                                         infrastructureGUID,
                                                         networkAddress,
                                                         templateGUID);
        }

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a endpoint.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateEndpoint(String             userId,
                               String             infrastructureManagerGUID,
                               String             infrastructureManagerName,
                               boolean            isMergeUpdate,
                               String             endpointGUID,
                               EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                  = "updateEndpoint";
        final String elementGUIDParameterName    = "endpointGUID";
        final String propertiesParameterName     = "endpointProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(endpointProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(endpointProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        EndpointRequestBody requestBody = new EndpointRequestBody(endpointProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        endpointGUID,
                                        isMergeUpdate);
    }




    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param endpointGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeEndpoint(String userId,
                               String infrastructureManagerGUID,
                               String infrastructureManagerName,
                               String endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName               = "removeEndpoint";
        final String elementGUIDParameterName = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix + "/{2}/delete";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        endpointGUID);
    }

    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
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
    public List<EndpointElement> findEndpoints(String userId,
                                               String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "findEndpoints";
        final String parameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(parameterName);

        EndpointsResponse restResult = restClient.callEndpointsPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
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
    public List<EndpointElement> getEndpointsByName(String userId,
                                                    String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getEndpointsByName";
        final String nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameter);

        EndpointsResponse restResult = restClient.callEndpointsPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching networkAddress.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param networkAddress networkAddress to search for
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
    public List<EndpointElement> getEndpointsByNetworkAddress(String userId,
                                                              String networkAddress,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getEndpointsByName";
        final String nameParameter = "networkAddress";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(networkAddress, nameParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix + "/by-network-address?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(networkAddress);
        requestBody.setNamePropertyName(nameParameter);

        EndpointsResponse restResult = restClient.callEndpointsPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of endpoint metadata elements that are attached to a specific infrastructure element.
     *
     * @param userId calling user
     * @param infrastructureGUID element to search for
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
    public List<EndpointElement> getEndpointsForInfrastructure(String userId,
                                                               String infrastructureGUID,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName        = "getEndpointsForInfrastructure";
        final String guidParameterName = "infrastructureGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(infrastructureGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix + "/for-infrastructure/{2}?startFrom={3}&pageSize={4}";

        EndpointsResponse restResult = restClient.callEndpointsGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           infrastructureGUID,
                                                                           startFrom,
                                                                           validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public EndpointElement getEndpointByGUID(String userId,
                                             String endpointGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "getEndpointByGUID";
        final String guidParameterName = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + endpointURLTemplatePrefix + "/{2}";

        EndpointResponse restResult = restClient.callEndpointGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         endpointGUID);

        return restResult.getElement();
    }
}
