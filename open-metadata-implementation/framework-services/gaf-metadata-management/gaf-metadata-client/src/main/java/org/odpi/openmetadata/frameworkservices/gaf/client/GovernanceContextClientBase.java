/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceCompletionInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.WatchDogEventInterface;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.frameworkservices.gaf.rest.ActionTargetStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CompletionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionStatusRequestBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceContextClientBase sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is however shared by all the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public class GovernanceContextClientBase extends OpenGovernanceClientBase implements GovernanceCompletionInterface,
                                                                                     WatchDogEventInterface
{
    /**
     * Supports REST calls
     */
    private   final GAFRESTClient           restClient;               /* Initialized in constructor */


    /**
     * Manages registered listeners
     */
    protected       GovernanceListenerManager governanceListenerManager = null;
    protected  String                    listenerId = null;

    protected final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

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
    public GovernanceContextClientBase(String                    serviceURLMarker,
                                       String                    serverName,
                                       String                    serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (no security)";

        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
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
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public GovernanceContextClientBase(String serviceURLMarker,
                                       String serverName,
                                       String serverPlatformURLRoot,
                                       String serverUserId,
                                       String serverPassword) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Client Constructor (with security)";

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
    public GovernanceContextClientBase(String        serviceURLMarker,
                                       String        serverName,
                                       String        serverPlatformURLRoot,
                                       GAFRESTClient restClient,
                                       int           maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Client Constructor (with REST client)";
        
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = restClient;
    }


    /**
     * Set up the listener manager.
     *
     * @param governanceListenerManager aggregates listeners from governance services
     * @param listenerId             identifier used to maintain topic event pointer in event manager
     */
    public void setListenerManager(GovernanceListenerManager governanceListenerManager,
                                   String                    listenerId)
    {
        this.governanceListenerManager = governanceListenerManager;
        this.listenerId = listenerId;
    }


    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata store.
     * There can be only one registered listener.  If this method is called more than once, the new parameters
     * replace the existing parameters.  This means the watchdog governance action service can change the
     * listener and the parameters that control the types of events received while it is running.
     * <br><br>
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     * <br><br>
     * If specific instance, interestingEventTypes or interestingMetadataTypes are null, it defaults to "any".
     * If the listener parameter is null, no more events are passed to the listener.
     *
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types
     * @param specificInstance unique identifier of a specific instance to watch for
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    @Override
    public  void registerListener(WatchdogGovernanceListener listener,
                                  List<WatchdogEventType>    interestingEventTypes,
                                  List<String>               interestingMetadataTypes,
                                  String                     specificInstance) throws InvalidParameterException
    {
        governanceListenerManager.registerListener(listenerId, listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }


    /**
     * Called during the disconnect processing of the watchdog governance action service.
     */
    @Override
    public void disconnectListener()
    {
        governanceListenerManager.removeListener(listenerId);
    }



    /**
     * Update the status of the governance action - providing the caller is permitted.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the governance action request
     * @param engineActionStatus new status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public void updateEngineActionStatus(String             userId,
                                         String             engineActionGUID,
                                         EngineActionStatus engineActionStatus) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateEngineActionStatus";
        final String guidParameterName = "engineActionGUID";
        final String statusParameterName = "engineActionStatus";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-context-service/users/{2}/engine-actions/{3}/status/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);
        invalidParameterHandler.validateEnum(engineActionStatus, statusParameterName, methodName);

        EngineActionStatusRequestBody requestBody = new EngineActionStatusRequestBody();

        requestBody.setStatus(engineActionStatus);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        engineActionGUID);
    }


    /**
     * Request that execution of a governance action is allocated to the caller.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the governance action request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public void claimEngineAction(String userId,
                                  String engineActionGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "claimEngineAction";
        final String guidParameterName = "engineActionGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-context-service/users/{2}/engine-actions/{3}/claim";

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
     * Retrieve the engine actions that are still in process and that have been claimed by this caller's userId.
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
    public List<EngineActionElement>  getActiveClaimedEngineActions(String userId,
                                                                    String governanceEngineGUID,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getActiveClaimedEngineActions";
        final String guidParameterName = "governanceEngineGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-context-service/users/{2}/governance-engines/{3}/active-engine-actions?startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, guidParameterName, methodName);

        EngineActionElementsResponse restResult = restClient.callEngineActionsGetRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          serverName,
                                                                                          serviceURLMarker,
                                                                                          userId,
                                                                                          governanceEngineGUID,
                                                                                          Integer.toString(startFrom),
                                                                                          Integer.toString(pageSize));

        return restResult.getElements();
    }



    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param userId caller's userId
     * @param actionTargetGUID unique identifier of the governance action service.
     * @param status status enum to show its progress
     * @param startDate date/time that the governance action service started processing the target
     * @param completionDate date/time that the governance process completed processing this target.
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public void updateActionTargetStatus(String             userId,
                                         String             actionTargetGUID,
                                         EngineActionStatus status,
                                         Date               startDate,
                                         Date               completionDate,
                                         String             completionMessage) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "updateActionTargetStatus";
        final String guidParameterName = "actionTargetGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-context-service/users/{2}/engine-actions/action-targets/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTargetGUID, guidParameterName, methodName);

        ActionTargetStatusRequestBody requestBody = new ActionTargetStatusRequestBody();

        requestBody.setActionTargetGUID(actionTargetGUID);
        requestBody.setStatus(status);
        requestBody.setStartDate(startDate);
        requestBody.setCompletionDate(completionDate);
        requestBody.setCompletionMessage(completionMessage);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param userId caller's userId
     * @param engineActionGUID unique identifier of the governance action to update
     * @param requestParameters request properties from the caller (will be passed onto any follow-on actions)
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public void recordCompletionStatus(String                userId,
                                       String engineActionGUID,
                                       Map<String, String>   requestParameters,
                                       CompletionStatus      status,
                                       List<String>          outputGuards,
                                       List<NewActionTarget> newActionTargets,
                                       String                completionMessage) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "recordCompletionStatus";
        final String statusParameterName = "status";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-context-service/users/{2}/engine-actions/{3}/completion-status";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateEnum(status, statusParameterName, methodName);

        CompletionStatusRequestBody requestBody = new CompletionStatusRequestBody();

        requestBody.setRequestParameters(requestParameters);
        requestBody.setStatus(status);
        requestBody.setOutputGuards(outputGuards);
        requestBody.setNewActionTargets(newActionTargets);
        requestBody.setCompletionMessage(completionMessage);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        engineActionGUID);
    }



}
