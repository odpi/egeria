/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.odpi.openmetadata.accessservices.governanceengine.api.GovernanceProcessingInterface;
import org.odpi.openmetadata.accessservices.governanceengine.client.rest.GovernanceEngineRESTClient;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionElement;
import org.odpi.openmetadata.accessservices.governanceengine.rest.GovernanceActionElementResponse;
import org.odpi.openmetadata.accessservices.governanceengine.rest.GovernanceActionElementsResponse;
import org.odpi.openmetadata.accessservices.governanceengine.rest.StatusRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworkservices.gaf.client.OpenGovernanceClientBase;

import java.util.List;

/**
 * GovernanceEngineClient sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is however shared by all the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public class GovernanceEngineClient extends OpenGovernanceClientBase implements GovernanceProcessingInterface
{
    private final GovernanceEngineRESTClient restClient;               /* Initialized in constructor */

    private final NullRequestBody         nullRequestBody         = new NullRequestBody();

    private final static String serviceURLMarker = "governance-engine";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public GovernanceEngineClient(String serverName,
                                  String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        this.restClient = new GovernanceEngineRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public GovernanceEngineClient(String serverName,
                                  String serverPlatformURLRoot,
                                  String serverUserId,
                                  String serverPassword) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, serverUserId, serverPassword);

        this.restClient = new GovernanceEngineRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            pre-initialized REST client
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public GovernanceEngineClient(String                     serverName,
                                  String                     serverPlatformURLRoot,
                                  GovernanceEngineRESTClient restClient,
                                  int                        maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, restClient, maxPageSize);

        this.restClient = restClient;
    }


    /**
     * Update the status of the governance action - providing the caller is permitted.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param governanceActionStatus new status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public void updateGovernanceActionStatus(String                 userId,
                                             String                 governanceActionGUID,
                                             GovernanceActionStatus governanceActionStatus) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "updateGovernanceActionStatus";
        final String guidParameterName = "governanceActionGUID";
        final String statusParameterName = "governanceActionStatus";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-actions/{2}/status/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);
        invalidParameterHandler.validateEnum(governanceActionStatus, statusParameterName, methodName);

        StatusRequestBody requestBody = new StatusRequestBody();

        requestBody.setStatus(governanceActionStatus);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        governanceActionGUID);
    }


    /**
     * Request the status of an executing governance action request.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public GovernanceActionElement getGovernanceAction(String userId,
                                                       String governanceActionGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getGovernanceAction";
        final String guidParameterName = "governanceActionGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-actions/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);

        GovernanceActionElementResponse restResult = restClient.callGovernanceActionGetRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                serverName,
                                                                                                userId,
                                                                                                governanceActionGUID);

        return restResult.getElement();
    }


    /**
     * Request that execution of a governance action is allocated to the caller.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public void claimGovernanceAction(String userId,
                                      String governanceActionGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "claimGovernanceAction";
        final String guidParameterName = "governanceActionGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-actions/{2}/claim";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        governanceActionGUID);
    }


    /**
     * Retrieve the governance actions known to the server.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public List<GovernanceActionElement>  getGovernanceActions(String userId,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "getGovernanceActions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-actions?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceActionElementsResponse restResult = restClient.callGovernanceActionsGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  Integer.toString(startFrom),
                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the governance actions that are still in process.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public List<GovernanceActionElement>  getActiveGovernanceActions(String userId,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "getActiveGovernanceActions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-actions/active?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceActionElementsResponse restResult = restClient.callGovernanceActionsGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  Integer.toString(startFrom),
                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the governance actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public List<GovernanceActionElement>  getActiveClaimedGovernanceActions(String userId,
                                                                            String governanceEngineGUID,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getActiveClaimedGovernanceActions";
        final String guidParameterName = "governanceEngineGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-engines/{2}/active-governance-actions?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, guidParameterName, methodName);

        GovernanceActionElementsResponse restResult = restClient.callGovernanceActionsGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  governanceEngineGUID,
                                                                                                  Integer.toString(startFrom),
                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
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
    public List<GovernanceActionElement> findGovernanceActions(String userId,
                                                               String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "findGovernanceActionTypes";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-actions/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceActionElementsResponse restResult = restClient.callGovernanceActionsGetRESTCall(methodName,
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
    public List<GovernanceActionElement> getGovernanceActionsByName(String userId,
                                                                    String name,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getGovernanceActionsByName";
        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-actions/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceActionElementsResponse restResult = restClient.callGovernanceActionsGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  Integer.toString(startFrom),
                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }
}
