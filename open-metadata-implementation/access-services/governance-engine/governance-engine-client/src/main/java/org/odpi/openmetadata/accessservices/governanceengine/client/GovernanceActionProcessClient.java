/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.odpi.openmetadata.accessservices.governanceengine.api.GovernanceActionProcessInterface;
import org.odpi.openmetadata.accessservices.governanceengine.client.rest.GovernanceEngineRESTClient;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionProcessElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.NextGovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionProcessProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionTypeProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.ProcessStatus;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * GovernanceEngineClient sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is however shared by all the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public class GovernanceActionProcessClient implements GovernanceActionProcessInterface
{
    private final String                     serverName;               /* Initialized in constructor */
    private final String                     serverPlatformURLRoot;    /* Initialized in constructor */
    private final GovernanceEngineRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final NullRequestBody         nullRequestBody         = new NullRequestBody();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceActionProcessClient(String serverName,
                                         String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new GovernanceEngineRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId caller's userId embedded in all HTTP requests
     * @param serverPassword caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceActionProcessClient(String serverName,
                                         String serverPlatformURLRoot,
                                         String serverUserId,
                                         String serverPassword) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new GovernanceEngineRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
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
    public GovernanceActionProcessClient(String                     serverName,
                                         String                     serverPlatformURLRoot,
                                         GovernanceEngineRESTClient restClient,
                                         int                        maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = restClient;
    }


    /* =====================================================================================================================
     * A governance action process describes a well defined series of steps that gets something done.
     * The steps are defined using GovernanceActionTypes.
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/new";

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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/{2}/update";

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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/{2}/publish";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/{2}/withdraw";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/{2}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceActionProcessElementsResponse restResult = restClient.callGovernanceActionProcessElementsPostRESTCall(methodName,
                                                                                                                        urlTemplate,
                                                                                                                        requestBody,
                                                                                                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceActionProcessElementsResponse restResult = restClient.callGovernanceActionProcessElementsPostRESTCall(methodName,
                                                                                                                        urlTemplate,
                                                                                                                        requestBody,
                                                                                                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        GovernanceActionProcessElementResponse restResult = restClient.callGovernanceActionProcessGetRESTCall(methodName,
                                                                                                              urlTemplate,
                                                                                                              serverName,
                                                                                                              userId,
                                                                                                              processGUID);

        return restResult.getElement();
    }




    /* =====================================================================================================================
     * A governance action type describes a step in a governance action process
     */

    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param userId calling user
     * @param actionTypeProperties properties about the process to store
     *
     * @return unique identifier of the new governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceActionType(String                         userId,
                                             GovernanceActionTypeProperties actionTypeProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "createGovernanceActionType";
        final String propertiesParameterName = "actionTypeProperties";
        final String qualifiedNameParameterName = "actionTypeProperties.getQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/new";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(actionTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(actionTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  actionTypeProperties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }



    /**
     * Update the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param actionTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceActionType(String                         userId,
                                           String                         actionTypeGUID,
                                           boolean                        isMergeUpdate,
                                           GovernanceActionTypeProperties actionTypeProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "updateGovernanceActionProcess";
        final String guidParameterName = "actionTypeGUID";
        final String propertiesParameterName = "actionTypeProperties";
        final String qualifiedNameParameterName = "actionTypeProperties.getQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(actionTypeProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(actionTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        UpdateGovernanceActionTypeRequestBody requestBody = new UpdateGovernanceActionTypeRequestBody();

        requestBody.setMergeUpdate(isMergeUpdate);
        requestBody.setProperties(actionTypeProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        actionTypeGUID);
    }


    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceActionType(String userId,
                                           String actionTypeGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "removeGovernanceActionType";
        final String guidParameterName = "actionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/{2}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        actionTypeGUID);
    }


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
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
    public List<GovernanceActionTypeElement> findGovernanceActionTypes(String userId,
                                                                       String searchString,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "findGovernanceActionTypes";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceActionTypeElementsResponse restResult = restClient.callGovernanceActionTypeElementsPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  userId,
                                                                                                                  Integer.toString(startFrom),
                                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
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
    public List<GovernanceActionTypeElement> getGovernanceActionTypesByName(String userId,
                                                                            String name,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getGovernanceActionTypesByName";
        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceActionTypeElementsResponse restResult = restClient.callGovernanceActionTypeElementsPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  userId,
                                                                                                                  Integer.toString(startFrom),
                                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionTypeElement getGovernanceActionTypeByGUID(String userId,
                                                                     String actionTypeGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getGovernanceActionTypeByGUID";
        final String guidParameterName = "actionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        GovernanceActionTypeElementResponse restResult = restClient.callGovernanceActionTypeGetRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        actionTypeGUID);

        return restResult.getElement();
    }



    /**
     * Set up a link between a governance action process and a governance action type.  This defines the first
     * step in the process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param actionTypeGUID unique identifier of the governance action type
     * @param guard optional guard for the first governance service to run
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupFirstActionType(String userId,
                                     String processGUID,
                                     String actionTypeGUID,
                                     String guard) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName = "setupFirstActionType";
        final String processGUIDParameterName = "processGUID";
        final String actionTypeGUIDParameterName = "actionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/{2}/first-action-type/{3}/new";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, actionTypeGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        guard,
                                        serverName,
                                        userId,
                                        processGUID,
                                        actionTypeGUID);
    }


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionTypeElement getFirstActionType(String userId,
                                                          String processGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getFirstActionType";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/{2}/first-action-type";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        GovernanceActionTypeElementResponse restResult = restClient.callGovernanceActionTypeGetRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        processGUID);

        return restResult.getElement();
    }


    /**
     * Remove the link between a governance process and that governance action type that defines its first step.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeFirstActionType(String userId,
                                      String processGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "removeFirstActionType";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-processes/{2}/first-action-type/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        processGUID);
    }



    /**
     * Add a link between two governance action types to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param userId calling user
     * @param currentActionTypeGUID unique identifier of the governance action type that defines the previous step in the governance action process
     * @param nextActionTypeGUID unique identifier of the governance action type that defines the next step in the governance action process
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
    public String setupNextActionType(String  userId,
                                      String  currentActionTypeGUID,
                                      String  nextActionTypeGUID,
                                      String  guard,
                                      boolean mandatoryGuard,
                                      boolean ignoreMultipleTriggers) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "setupNextActionType";
        final String currentGUIDParameterName = "currentActionTypeGUID";
        final String nextGUIDParameterName = "nextActionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/{2}/next-action-types/{3}/new";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentActionTypeGUID, currentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextActionTypeGUID, nextGUIDParameterName, methodName);

        NextGovernanceActionTypeRequestBody requestBody = new NextGovernanceActionTypeRequestBody();

        requestBody.setGuard(guard);
        requestBody.setMandatoryGuard(mandatoryGuard);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  currentActionTypeGUID,
                                                                  nextActionTypeGUID);

        return restResult.getGUID();
    }


    /**
     * Update the properties of the link between two governance action types that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param userId calling user
     * @param nextActionLinkGUID unique identifier of the relationship between the governance action types
     * @param guard guard required for this next step to proceed - or null for always run the next step.
     * @param mandatoryGuard means that no next steps can run if this guard is not returned
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateNextActionType(String  userId,
                                     String  nextActionLinkGUID,
                                     String  guard,
                                     boolean mandatoryGuard,
                                     boolean ignoreMultipleTriggers) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "updateNextActionType";
        final String guidParameterName = "nextActionLinkGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/next-action-types/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(nextActionLinkGUID, guidParameterName, methodName);

        NextGovernanceActionTypeRequestBody requestBody = new NextGovernanceActionTypeRequestBody();

        requestBody.setGuard(guard);
        requestBody.setMandatoryGuard(mandatoryGuard);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        nextActionLinkGUID);
    }


    /**
     * Return the lust of next action type defined for the governance action process.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the current governance action type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action types.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<NextGovernanceActionTypeElement> getNextGovernanceActionTypes(String userId,
                                                                              String actionTypeGUID,
                                                                              int    startFrom,
                                                                              int    pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "getNextGovernanceActionTypes";
        final String guidParameterName = "actionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/{2}/next-action-type?startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        NextGovernanceActionTypeElementsResponse restResult = restClient.callNextGovernanceActionTypesGetRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  serverName,
                                                                                                                  userId,
                                                                                                                  actionTypeGUID,
                                                                                                                  Integer.toString(startFrom),
                                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Remove a follow-on step from a governance action process.
     *
     * @param userId calling user
     * @param actionLinkGUID unique identifier of the relationship between the governance action types
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeNextActionType(String userId,
                                     String actionLinkGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "removeFirstActionType";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/next-action-type/{2}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionLinkGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        actionLinkGUID);
    }
}
