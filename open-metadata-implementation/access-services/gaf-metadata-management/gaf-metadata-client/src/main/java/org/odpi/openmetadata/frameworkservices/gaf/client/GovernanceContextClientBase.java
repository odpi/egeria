/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.opengovernance.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceCompletionInterface;
import org.odpi.openmetadata.frameworks.opengovernance.client.WatchdogEventInterface;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworkservices.omf.rest.ActionTargetStatusRequestBody;
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
                                                                                     WatchdogEventInterface
{
    /**
     * Manages registered listeners
     */
    protected  GovernanceListenerManager governanceListenerManager = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize           pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public GovernanceContextClientBase(String   serverName,
                                       String   serverPlatformURLRoot,
                                       String   localServerSecretsStoreProvider,
                                       String   localServerSecretsStoreLocation,
                                       String   localServerSecretsStoreCollection,
                                       int      maxPageSize,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, maxPageSize, auditLog);
    }


    /**
     * Set up the listener manager.  Called once for each governance engine handler.
     *
     * @param governanceListenerManager aggregates listeners from governance services
     */
    public void setListenerManager(GovernanceListenerManager governanceListenerManager)
    {
        this.governanceListenerManager = governanceListenerManager;
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
     * @param listenerId             identifier used to maintain topic event pointer in event manager
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    @Override
    public  void registerListener(String                      listenerId,
                                  WatchdogGovernanceListener  listener,
                                  List<OpenMetadataEventType> interestingEventTypes,
                                  List<String>                interestingMetadataTypes,
                                  String                      specificInstance) throws InvalidParameterException
    {
        governanceListenerManager.registerListener(listenerId, listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }


    /**
     * Called during the disconnect processing of the watchdog governance action service.
     *
     * @param listenerId             identifier used to maintain topic event pointer in event manager
     */
    @Override
    public void disconnectListener(String listenerId)
    {
        governanceListenerManager.removeListener(listenerId);
    }



    /**
     * Update the status of the governance action - providing the caller is permitted.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the governance action request
     * @param activityStatus new status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public void updateEngineActionStatus(String         userId,
                                         String         engineActionGUID,
                                         ActivityStatus activityStatus) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "updateEngineActionStatus";
        final String guidParameterName = "engineActionGUID";
        final String statusParameterName = "activityStatus";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-context-service/users/{1}/engine-actions/{2}/status/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);
        invalidParameterHandler.validateEnum(activityStatus, statusParameterName, methodName);

        EngineActionStatusRequestBody requestBody = new EngineActionStatusRequestBody();

        requestBody.setStatus(activityStatus);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-context-service/users/{1}/engine-actions/{2}/claim";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-context-service/users/{1}/governance-engines/{2}/active-engine-actions?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, guidParameterName, methodName);

        EngineActionElementsResponse restResult = restClient.callEngineActionsGetRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          serverName,
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
     * @throws PropertyServerException a problem connecting to the metadata store
     */
    @Override
    public void updateActionTargetStatus(String         userId,
                                         String         actionTargetGUID,
                                         ActivityStatus status,
                                         Date           startDate,
                                         Date           completionDate,
                                         String         completionMessage) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "updateActionTargetStatus";
        final String guidParameterName = "actionTargetGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-context-service/users/{1}/engine-actions/action-targets/update";

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
     * @throws PropertyServerException a problem connecting to the metadata store
     */
    @Override
    public void recordCompletionStatus(String                userId,
                                       String                engineActionGUID,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-context-service/users/{1}/engine-actions/{2}/completion-status";

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
                                        userId,
                                        engineActionGUID);
    }



}
