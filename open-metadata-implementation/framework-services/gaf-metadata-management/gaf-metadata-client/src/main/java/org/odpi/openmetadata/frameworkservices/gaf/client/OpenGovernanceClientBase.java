/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.DuplicateManagementInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceActionProcessInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceActionTypeInterface;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenGovernanceClientBase provides common governance services that originate in the Governance Action Framework (GAF).
 * This includes the ability to define and execute governance action processes as well as manage duplicates.
 */
public class OpenGovernanceClientBase implements ActionControlInterface,
                                                 GovernanceActionTypeInterface,
                                                 GovernanceActionProcessInterface,
                                                 DuplicateManagementInterface
{
    protected final String serverName;               /* Initialized in constructor */
    protected final String serviceURLMarker;         /* Initialized in constructor */
    protected final String serverPlatformURLRoot;    /* Initialized in constructor */

    private   final GAFRESTClient           restClient;               /* Initialized in constructor */

    protected final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    protected final NullRequestBody         nullRequestBody = new NullRequestBody();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenGovernanceClientBase(String                    serviceURLMarker,
                                    String                    serverName,
                                    String                    serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenGovernanceClientBase(String serviceURLMarker,
                                    String serverName,
                                    String serverPlatformURLRoot,
                                    int    maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenGovernanceClientBase(String serviceURLMarker,
                                    String serverName,
                                    String serverPlatformURLRoot,
                                    String serverUserId,
                                    String serverPassword,
                                    int    maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor (with security)";

        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenGovernanceClientBase(String serviceURLMarker,
                                    String serverName,
                                    String serverPlatformURLRoot,
                                    String serverUserId,
                                    String serverPassword) throws InvalidParameterException
    {
        final String methodName = "Client Constructor (with security)";

        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            pre-initialized REST client
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public OpenGovernanceClientBase(String        serviceURLMarker,
                                    String        serverName,
                                    String        serverPlatformURLRoot,
                                    GAFRESTClient restClient,
                                    int           maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor (with REST client)";

        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = restClient;
    }




    /**
     * Create an engine action in the metadata store which will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this engine action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param receivedGuards list of guards to initiate the engine action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType governance request type from the caller
     * @param requestParameters properties to pass to the governance service
     * @param processName name of the process that this action is a part of
     * @param requestSourceName source of the request
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the requesting governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException the caller is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateEngineAction(String                userId,
                                       String                qualifiedName,
                                       int                   domainIdentifier,
                                       String                displayName,
                                       String                description,
                                       List<String>          requestSourceGUIDs,
                                       List<NewActionTarget> actionTargets,
                                       List<String>          receivedGuards,
                                       Date                  startTime,
                                       String                governanceEngineName,
                                       String                requestType,
                                       Map<String, String>   requestParameters,
                                       String                processName,
                                       String                requestSourceName,
                                       String                originatorServiceName,
                                       String                originatorEngineName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "initiateEngineAction";
        final String qualifiedNameParameterName = "qualifiedName";
        final String engineNameParameterName = "governanceEngineName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-engines/{3}/engine-actions/initiate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(governanceEngineName, engineNameParameterName, methodName);

        InitiateEngineActionRequestBody requestBody = new InitiateEngineActionRequestBody();

        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDomainIdentifier(domainIdentifier);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setRequestSourceGUIDs(requestSourceGUIDs);
        requestBody.setActionTargets(actionTargets);
        requestBody.setReceivedGuards(receivedGuards);
        requestBody.setStartDate(startTime);
        requestBody.setRequestType(requestType);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setProcessName(processName);
        requestBody.setRequestSourceName(requestSourceName);
        requestBody.setOriginatorServiceName(originatorServiceName);
        requestBody.setOriginatorEngineName(originatorEngineName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId,
                                                                  governanceEngineName);

        return restResult.getGUID();
    }


    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param userId caller's userId
     * @param governanceActionTypeQualifiedName unique name of the governance action type to use
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param startTime future start time or null for "as soon as possible".
     * @param requestParameters request properties to be passed to the engine action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null or unrecognized qualified name of the type
     * @throws UserNotAuthorizedException the caller is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionType(String                userId,
                                               String                governanceActionTypeQualifiedName,
                                               List<String>          requestSourceGUIDs,
                                               List<NewActionTarget> actionTargets,
                                               Date                  startTime,
                                               Map<String, String>   requestParameters,
                                               String                originatorServiceName,
                                               String                originatorEngineName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "initiateGovernanceActionType";
        final String qualifiedNameParameterName = "governanceActionTypeQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-types/initiate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceActionTypeQualifiedName, qualifiedNameParameterName, methodName);

        InitiateGovernanceActionTypeRequestBody requestBody = new InitiateGovernanceActionTypeRequestBody();

        requestBody.setGovernanceActionTypeQualifiedName(governanceActionTypeQualifiedName);
        requestBody.setRequestSourceGUIDs(requestSourceGUIDs);
        requestBody.setActionTargets(actionTargets);
        requestBody.setStartDate(startTime);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setOriginatorServiceName(originatorServiceName);
        requestBody.setOriginatorEngineName(originatorEngineName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param userId caller's userId
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param startTime future start time or null for "as soon as possible".
     * @param requestParameters request properties to be passed to the first engine action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the first engine action of the process
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException the caller is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionProcess(String                userId,
                                                  String                processQualifiedName,
                                                  List<String>          requestSourceGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Date                  startTime,
                                                  Map<String, String>   requestParameters,
                                                  String                originatorServiceName,
                                                  String                originatorEngineName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "initiateGovernanceActionProcess";
        final String qualifiedNameParameterName = "processQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/initiate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(processQualifiedName, qualifiedNameParameterName, methodName);

        InitiateGovernanceActionProcessRequestBody requestBody = new InitiateGovernanceActionProcessRequestBody();

        requestBody.setProcessQualifiedName(processQualifiedName);
        requestBody.setRequestSourceGUIDs(requestSourceGUIDs);
        requestBody.setActionTargets(actionTargets);
        requestBody.setStartDate(startTime);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setOriginatorServiceName(originatorServiceName);
        requestBody.setOriginatorEngineName(originatorEngineName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Request the status of an executing engine action request.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public EngineActionElement getEngineAction(String userId,
                                               String engineActionGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getEngineAction";
        final String guidParameterName = "engineActionGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/engine-actions/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);

        EngineActionElementResponse restResult = restClient.callEngineActionGetRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        serverName,
                                                                                        serviceURLMarker,
                                                                                        userId,
                                                                                        engineActionGUID);

        return restResult.getElement();
    }



    /**
     * Request that execution of an engine action is stopped.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public void cancelEngineAction(String userId,
                                   String engineActionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "cancelEngineAction";
        final String guidParameterName = "engineActionGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/engine-actions/{3}/cancel";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        engineActionGUID);
    }


    /**
     * Retrieve the engine actions known to the server.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public List<EngineActionElement>  getEngineActions(String userId,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getEngineActions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/engine-actions?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);

        EngineActionElementsResponse restResult = restClient.callEngineActionsGetRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          serverName,
                                                                                          serviceURLMarker,
                                                                                          userId,
                                                                                          Integer.toString(startFrom),
                                                                                          Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public List<EngineActionElement>  getActiveEngineActions(String userId,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getActiveEngineActions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/engine-actions/active?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);

        EngineActionElementsResponse restResult = restClient.callEngineActionsGetRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          serverName,
                                                                                          serviceURLMarker,
                                                                                          userId,
                                                                                          Integer.toString(startFrom),
                                                                                          Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the list of engine action metadata elements that contain the search string.
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
    public List<EngineActionElement> findEngineActions(String userId,
                                                       String searchString,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "findEngineActions";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/engine-actions/by-search-string?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        EngineActionElementsResponse restResult = restClient.callEngineActionsGetRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          requestBody,
                                                                                          serverName,
                                                                                          serviceURLMarker,
                                                                                          userId,
                                                                                          Integer.toString(startFrom),
                                                                                          Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the list of engine action metadata elements with a matching qualified or display name.
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
    public List<EngineActionElement> getEngineActionsByName(String userId,
                                                            String name,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "getEngineActionsByName";
        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/engine-actions/by-name?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        EngineActionElementsResponse restResult = restClient.callEngineActionsGetRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          requestBody,
                                                                                          serverName,
                                                                                          serviceURLMarker,
                                                                                          userId,
                                                                                          Integer.toString(startFrom),
                                                                                          Integer.toString(pageSize));

        return restResult.getElements();
    }
    
    
    /**
     * Link elements as peer duplicates. Create a simple relationship between two elements.
     * If the relationship already exists, the properties are updated.
     *
     * @param userId caller's userId
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param setKnownDuplicate boolean flag indicating whether the KnownDuplicate classification should be set on the linked entities.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the caller is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void linkElementsAsPeerDuplicates(String  userId,
                                             String  metadataElement1GUID,
                                             String  metadataElement2GUID,
                                             int     statusIdentifier,
                                             String  steward,
                                             String  stewardTypeName,
                                             String  stewardPropertyName,
                                             String  source,
                                             String  notes,
                                             boolean setKnownDuplicate) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "linkElementsAsPeerDuplicates";
        final String end1ParameterName = "metadataElement1GUID";
        final String end2ParameterName = "metadataElement2GUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/related-elements/link-as-peer-duplicate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1ParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2ParameterName, methodName);

        PeerDuplicatesRequestBody requestBody = new PeerDuplicatesRequestBody();

        requestBody.setMetadataElement1GUID(metadataElement1GUID);
        requestBody.setMetadataElement2GUID(metadataElement2GUID);
        requestBody.setStatusIdentifier(statusIdentifier);
        requestBody.setSteward(steward);
        requestBody.setStewardTypeName(stewardTypeName);
        requestBody.setStewardPropertyName(stewardPropertyName);
        requestBody.setSource(source);
        requestBody.setNotes(notes);
        requestBody.setSetKnownDuplicate(setKnownDuplicate);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId);
    }


    /**
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     *
     * @param userId caller's userId
     * @param consolidatedElementGUID unique identifier of the metadata element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param sourceElementGUIDs List of the source elements that must be linked to the consolidated element.  It is assumed that they already
     *                           have the KnownDuplicateClassification.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the caller is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void linkConsolidatedDuplicate(String       userId,
                                          String       consolidatedElementGUID,
                                          int          statusIdentifier,
                                          String       steward,
                                          String       stewardTypeName,
                                          String       stewardPropertyName,
                                          String       source,
                                          String       notes,
                                          List<String> sourceElementGUIDs) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "linkConsolidatedDuplicate";
        final String consolidatedElementGUIDParameterName = "consolidatedElementGUID";
        final String sourceElementGUIDsParameterName = "sourceElementGUIDs";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/related-elements/link-as-consolidated-duplicate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consolidatedElementGUID, consolidatedElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(sourceElementGUIDs, sourceElementGUIDsParameterName, methodName);

        ConsolidatedDuplicatesRequestBody requestBody = new ConsolidatedDuplicatesRequestBody();

        requestBody.setConsolidatedElementGUID(consolidatedElementGUID);
        requestBody.setStatusIdentifier(statusIdentifier);
        requestBody.setSteward(steward);
        requestBody.setStewardTypeName(stewardTypeName);
        requestBody.setStewardPropertyName(stewardPropertyName);
        requestBody.setSource(source);
        requestBody.setNotes(notes);
        requestBody.setSourceElementGUIDs(sourceElementGUIDs);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId);
    }

    /* =====================================================================================================================
     * A governance action type is a template for a single engine action.
     */


    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param userId     calling user
     * @param properties properties about the governance action type to store
     * @return unique identifier of the new governance action type
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceActionType(String                         userId,
                                             GovernanceActionTypeProperties properties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "createGovernanceActionType";
        final String propertiesParameterName = "properties";
        final String qualifiedNameParameterName = "properties.getQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-types";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a governance action type.
     *
     * @param userId                   calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate            should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param properties               new properties for the metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceActionType(String                         userId,
                                           String                         governanceActionTypeGUID,
                                           boolean                        isMergeUpdate,
                                           GovernanceActionTypeProperties properties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "updateGovernanceActionType";
        final String guidParameterName = "governanceActionTypeGUID";
        final String propertiesParameterName = "properties";
        final String qualifiedNameParameterName = "properties.getQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-types/{3}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionTypeGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        UpdateGovernanceActionTypeRequestBody requestBody = new UpdateGovernanceActionTypeRequestBody();

        requestBody.setMergeUpdate(isMergeUpdate);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        governanceActionTypeGUID);
    }


    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param userId                   calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to remove
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceActionType(String userId,
                                           String governanceActionTypeGUID) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "removeGovernanceActionType";
        final String guidParameterName = "governanceActionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-types/{3}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionTypeGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        governanceActionTypeGUID);
    }


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId       calling user
     * @param searchString string to find in the properties
     * @param startFrom    paging start point
     * @param pageSize     maximum results that can be returned
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GovernanceActionTypeElement> findGovernanceActionTypes(String userId,
                                                                       String searchString,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "findGovernanceActionTypes";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-types/by-search-string?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceActionTypesResponse restResult = restClient.callGovernanceTypesPostRESTCall(methodName,
                                                                                              urlTemplate,
                                                                                              requestBody,
                                                                                              serverName,
                                                                                              serviceURLMarker,
                                                                                              userId,
                                                                                              Integer.toString(startFrom),
                                                                                              Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId    calling user
     * @param name      name to search for
     * @param startFrom paging start point
     * @param pageSize  maximum results that can be returned
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GovernanceActionTypeElement> getGovernanceActionTypesByName(String userId,
                                                                            String name,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getGovernanceActionTypesByName";
        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-types/by-name?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceActionTypesResponse restResult = restClient.callGovernanceTypesPostRESTCall(methodName,
                                                                                              urlTemplate,
                                                                                              requestBody,
                                                                                              serverName,
                                                                                              serviceURLMarker,
                                                                                              userId,
                                                                                              Integer.toString(startFrom),
                                                                                              Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param userId                   calling user
     * @param governanceActionTypeGUID unique identifier of the governance action type
     * @return requested metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionTypeElement getGovernanceActionTypeByGUID(String userId,
                                                                     String governanceActionTypeGUID) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "getGovernanceActionTypeByGUID";
        final String guidParameterName = "governanceActionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-types/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionTypeGUID, guidParameterName, methodName);

        GovernanceActionTypeResponse restResult = restClient.callGovernanceActionTypeGetRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 serverName,
                                                                                                 serviceURLMarker,
                                                                                                 userId,
                                                                                                 governanceActionTypeGUID);

        return restResult.getElement();
    }


    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */

    /**
     * Create a new metadata element to represent a governance action process.
     *
     * @param userId calling user
     * @param processProperties properties about the process to store
     * @param initialStatus status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceActionProcess(String                            userId,
                                                GovernanceActionProcessProperties processProperties,
                                                ProcessStatus                     initialStatus) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "createGovernanceActionProcess";
        final String propertiesParameterName = "processProperties";
        final String qualifiedNameParameterName = "processProperties.getQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(processProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(processProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        NewGovernanceActionProcessRequestBody requestBody = new NewGovernanceActionProcessRequestBody();

        requestBody.setProcessStatus(initialStatus);
        requestBody.setProperties(processProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }



    /**
     * Update the metadata element representing a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processStatus new status for the process
     * @param processProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceActionProcess(String                            userId,
                                              String                            processGUID,
                                              boolean                           isMergeUpdate,
                                              ProcessStatus                     processStatus,
                                              GovernanceActionProcessProperties processProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "updateGovernanceActionProcess";
        final String guidParameterName = "processGUID";
        final String propertiesParameterName = "processProperties";
        final String qualifiedNameParameterName = "processProperties.getQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateObject(processProperties, propertiesParameterName, methodName);
            invalidParameterHandler.validateName(processProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        UpdateGovernanceActionProcessRequestBody requestBody = new UpdateGovernanceActionProcessRequestBody();

        requestBody.setMergeUpdate(isMergeUpdate);
        requestBody.setProcessStatus(processStatus);
        requestBody.setProperties(processProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processGUID);
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Governance Engine OMAS).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void publishGovernanceActionProcess(String userId,
                                               String processGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "publishGovernanceActionProcess";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}/publish";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processGUID);
    }



    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Governance Engine OMAS.  This is the setting when the process is first created).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void withdrawGovernanceActionProcess(String userId,
                                                String processGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "withdrawGovernanceActionProcess";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}/withdraw";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processGUID);
    }



    /**
     * Remove the metadata element representing a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceActionProcess(String userId,
                                              String processGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "removeGovernanceActionProcess";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processGUID);
    }



    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
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
    public List<GovernanceActionProcessElement> findGovernanceActionProcesses(String userId,
                                                                              String searchString,
                                                                              int    startFrom,
                                                                              int    pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "findGovernanceActionProcesses";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/by-search-string?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceActionProcessElementsResponse restResult = restClient.callGovernanceActionProcessElementsPostRESTCall(methodName,
                                                                                                                        urlTemplate,
                                                                                                                        requestBody,
                                                                                                                        serverName,
                                                                                                                        serviceURLMarker,
                                                                                                                        userId,
                                                                                                                        Integer.toString(startFrom),
                                                                                                                        Integer.toString(pageSize));

        return restResult.getElements();
    }



    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
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
    public List<GovernanceActionProcessElement> getGovernanceActionProcessesByName(String userId,
                                                                                   String name,
                                                                                   int    startFrom,
                                                                                   int    pageSize) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessesByName";
        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/by-name?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceActionProcessElementsResponse restResult = restClient.callGovernanceActionProcessElementsPostRESTCall(methodName,
                                                                                                                        urlTemplate,
                                                                                                                        requestBody,
                                                                                                                        serverName,
                                                                                                                        serviceURLMarker,
                                                                                                                        userId,
                                                                                                                        Integer.toString(startFrom),
                                                                                                                        Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionProcessElement getGovernanceActionProcessByGUID(String userId,
                                                                           String processGUID) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessByGUID";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        GovernanceActionProcessElementResponse restResult = restClient.callGovernanceActionProcessGetRESTCall(methodName,
                                                                                                              urlTemplate,
                                                                                                              serverName,
                                                                                                              serviceURLMarker,
                                                                                                              userId,
                                                                                                              processGUID);

        return restResult.getElement();
    }



    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with its process flow implementation.  This includes the process steps and the links
     * between them
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionProcessGraph getGovernanceActionProcessGraph(String userId,
                                                                        String processGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessGraph";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}/graph";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        GovernanceActionProcessGraphResponse restResult = restClient.callGovernanceActionProcessGraphGetRESTCall(methodName,
                                                                                                                 urlTemplate,
                                                                                                                 serverName,
                                                                                                                 serviceURLMarker,
                                                                                                                 userId,
                                                                                                                 processGUID);

        return restResult.getElement();
    }



    /* =====================================================================================================================
     * A governance action process step describes a step in a governance action process
     */

    /**
     * Create a new metadata element to represent a governance action process step.
     *
     * @param userId calling user
     * @param processStepProperties properties about the process step to store
     *
     * @return unique identifier of the new governance action process step
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceActionProcessStep(String                                userId,
                                                    GovernanceActionProcessStepProperties processStepProperties) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        final String methodName = "createGovernanceActionProcessStep";
        final String propertiesParameterName = "processStepProperties";
        final String qualifiedNameParameterName = "processStepProperties.getQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(processStepProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(processStepProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  processStepProperties,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a governance action process step.
     *
     * @param userId calling user
     * @param processStepGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processStepProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceActionProcessStep(String                         userId,
                                                  String                         processStepGUID,
                                                  boolean                        isMergeUpdate,
                                                  GovernanceActionProcessStepProperties processStepProperties) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        final String methodName = "updateGovernanceActionProcess";
        final String guidParameterName = "processStepGUID";
        final String propertiesParameterName = "processStepProperties";
        final String qualifiedNameParameterName = "processStepProperties.getQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/{3}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processStepGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(processStepProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(processStepProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        UpdateGovernanceActionProcessStepRequestBody requestBody = new UpdateGovernanceActionProcessStepRequestBody();

        requestBody.setMergeUpdate(isMergeUpdate);
        requestBody.setProperties(processStepProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processStepGUID);
    }


    /**
     * Remove the metadata element representing a governance action process step.
     *
     * @param userId calling user
     * @param processStepGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceActionProcessStep(String userId,
                                                  String processStepGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "removeGovernanceActionProcessStep";
        final String guidParameterName = "processStepGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/{3}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processStepGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processStepGUID);
    }


    /**
     * Retrieve the list of governance action process step metadata elements that contain the search string.
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
    public List<GovernanceActionProcessStepElement> findGovernanceActionProcessSteps(String userId,
                                                                                     String searchString,
                                                                                     int    startFrom,
                                                                                     int    pageSize) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "findGovernanceActionProcessSteps";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/by-search-string?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceActionProcessStepsResponse restResult = restClient.callGovernanceProcessStepsPostRESTCall(methodName,
                                                                                                            urlTemplate,
                                                                                                            requestBody,
                                                                                                            serverName,
                                                                                                            serviceURLMarker,
                                                                                                            userId,
                                                                                                            Integer.toString(startFrom),
                                                                                                            Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the list of governance action process step metadata elements with a matching qualified or display name.
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
    public List<GovernanceActionProcessStepElement> getGovernanceActionProcessStepsByName(String userId,
                                                                                          String name,
                                                                                          int    startFrom,
                                                                                          int    pageSize) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessStepsByName";
        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/by-name?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceActionProcessStepsResponse restResult = restClient.callGovernanceProcessStepsPostRESTCall(methodName,
                                                                                                            urlTemplate,
                                                                                                            requestBody,
                                                                                                            serverName,
                                                                                                            serviceURLMarker,
                                                                                                            userId,
                                                                                                            Integer.toString(startFrom),
                                                                                                            Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the governance action process step metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processStepGUID unique identifier of the governance action process step
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionProcessStepElement getGovernanceActionProcessStepByGUID(String userId,
                                                                                   String processStepGUID) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessStepByGUID";
        final String guidParameterName = "processStepGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processStepGUID, guidParameterName, methodName);

        GovernanceActionProcessStepResponse restResult = restClient.callGovernanceActionProcessStepGetRESTCall(methodName,
                                                                                                               urlTemplate,
                                                                                                               serverName,
                                                                                                               serviceURLMarker,
                                                                                                               userId,
                                                                                                               processStepGUID);

        return restResult.getElement();
    }



    /**
     * Set up a link between a governance action process and a governance action process step.  This defines the first
     * step in the process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param processStepGUID unique identifier of the governance action process step
     * @param guard optional guard for the first governance service to run
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupFirstActionProcessStep(String userId,
                                            String processGUID,
                                            String processStepGUID,
                                            String guard) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "setupFirstActionProcessStep";
        final String processGUIDParameterName = "processGUID";
        final String processStepGUIDParameterName = "processStepGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}/first-action-process-step/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(processStepGUID, processStepGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        guard,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processGUID,
                                        processStepGUID);
    }


    /**
     * Return the governance action process step that is the first step in a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the first governance action process step
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public FirstGovernanceActionProcessStepElement getFirstActionProcessStep(String userId,
                                                                             String processGUID) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getFirstActionProcessStep";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}/first-action-process-step";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        FirstGovernanceActionProcessStepResponse restResult = restClient.callFirstGovernanceActionProcessStepGetRESTCall(methodName,
                                                                                                                         urlTemplate,
                                                                                                                         serverName,
                                                                                                                         serviceURLMarker,
                                                                                                                         userId,
                                                                                                                         processGUID);

        return restResult.getElement();
    }


    /**
     * Remove the link between a governance process and that governance action process step that defines its first step.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeFirstActionProcessStep(String userId,
                                             String processGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "removeFirstActionProcessStep";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/{3}/first-action-process-step/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processGUID);
    }



    /**
     * Add a link between two governance action process steps to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param userId calling user
     * @param currentActionProcessStepGUID unique identifier of the governance action process step that defines the previous step in the governance action process
     * @param nextActionProcessStepGUID unique identifier of the governance action process step that defines the next step in the governance action process
     * @param guard guard required for this next step to proceed - or null for always run the next step.
     * @param mandatoryGuard means that no next steps can run if this guard is not returned
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     *
     * @return unique identifier of the new link
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupNextActionProcessStep(String  userId,
                                             String  currentActionProcessStepGUID,
                                             String  nextActionProcessStepGUID,
                                             String  guard,
                                             boolean mandatoryGuard,
                                             boolean ignoreMultipleTriggers) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "setupNextActionProcessStep";
        final String currentGUIDParameterName = "currentActionProcessStepGUID";
        final String nextGUIDParameterName = "nextActionProcessStepGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/{3}/next-process-steps/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentActionProcessStepGUID, currentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextActionProcessStepGUID, nextGUIDParameterName, methodName);

        NextGovernanceActionProcessStepRequestBody requestBody = new NextGovernanceActionProcessStepRequestBody();

        requestBody.setGuard(guard);
        requestBody.setMandatoryGuard(mandatoryGuard);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId,
                                                                  currentActionProcessStepGUID,
                                                                  nextActionProcessStepGUID);

        return restResult.getGUID();
    }


    /**
     * Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param userId calling user
     * @param nextProcessStepLinkGUID unique identifier of the relationship between the governance action process steps
     * @param guard guard required for this next step to proceed - or null for always run the next step.
     * @param mandatoryGuard means that no next steps can run if this guard is not returned
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateNextActionProcessStep(String  userId,
                                            String  nextProcessStepLinkGUID,
                                            String  guard,
                                            boolean mandatoryGuard,
                                            boolean ignoreMultipleTriggers) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "updateNextActionProcessStep";
        final String guidParameterName = "nextActionLinkGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/next-process-steps/{3}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(nextProcessStepLinkGUID, guidParameterName, methodName);

        NextGovernanceActionProcessStepRequestBody requestBody = new NextGovernanceActionProcessStepRequestBody();

        requestBody.setGuard(guard);
        requestBody.setMandatoryGuard(mandatoryGuard);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        nextProcessStepLinkGUID);
    }


    /**
     * Return the list of next action process step defined for the governance action process.
     *
     * @param userId calling user
     * @param processStepGUID unique identifier of the current governance action process step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action process steps.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<NextGovernanceActionProcessStepElement> getNextGovernanceActionProcessSteps(String userId,
                                                                                            String processStepGUID,
                                                                                            int    startFrom,
                                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        final String methodName = "getNextGovernanceActionProcessSteps";
        final String guidParameterName = "processStepGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/{3}/next-process-step?startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processStepGUID, guidParameterName, methodName);

        NextGovernanceActionProcessStepsResponse restResult = restClient.callNextGovernanceActionProcessStepsGetRESTCall(methodName,
                                                                                                                         urlTemplate,
                                                                                                                         serverName,
                                                                                                                         serviceURLMarker,
                                                                                                                         userId,
                                                                                                                         processStepGUID,
                                                                                                                         Integer.toString(startFrom),
                                                                                                                         Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Remove a follow-on step from a governance action process.
     *
     * @param userId calling user
     * @param processStepLinkGUID unique identifier of the relationship between the governance action process steps
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeNextActionProcessStep(String userId,
                                            String processStepLinkGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "removeFirstActionProcessStep";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-process-steps/next-process-step/{3}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processStepLinkGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        processStepLinkGUID);
    }
}
