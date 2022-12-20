/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionTypeElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceProcessingInterface provides support for the execution of Governance Action Processes through Governance Actions.
 */
public interface GovernanceProcessingInterface
{
    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets list of action target names to GUIDs for the resulting governance action service
     * @param receivedGuards list of guards to initiate the governance action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance action service to run
     * @param requestParameters properties to pass to the governance action service
     * @param processName name of the process that this action is a part of
     * @param requestSourceName source of the request
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the requesting governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String initiateGovernanceAction(String                userId,
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
                                                                                       PropertyServerException;


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param userId caller's userId
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible"
     * @param requestParameters request properties to be passed to the first governance action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the first governance action of the process
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String initiateGovernanceActionProcess(String                userId,
                                           String                processQualifiedName,
                                           List<String>          requestSourceGUIDs,
                                           List<NewActionTarget> actionTargets,
                                           Date                  startTime,
                                           Map<String, String>   requestParameters,
                                           String                originatorServiceName,
                                           String                originatorEngineName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;

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
    GovernanceActionElement getGovernanceAction(String userId,
                                                String governanceActionGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


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
    void claimGovernanceAction(String userId,
                               String governanceActionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Retrieve the governance actions that are known to the server.
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
    List<GovernanceActionElement> getGovernanceActions(String userId,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;




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
    List<GovernanceActionElement> getActiveGovernanceActions(String userId,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;





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
    List<GovernanceActionElement> getActiveClaimedGovernanceActions(String userId,
                                                                    String governanceEngineGUID,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;

    /**
     * Retrieve the list of governance action metadata elements that contain the search string.
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
    List<GovernanceActionElement> findGovernanceActions(String userId,
                                                        String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Retrieve the list of governance action metadata elements with a matching qualified or display name.
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
    List<GovernanceActionElement> getGovernanceActionsByName(String userId,
                                                             String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


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
    void updateActionTargetStatus(String                 userId,
                                  String                 actionTargetGUID,
                                  GovernanceActionStatus status,
                                  Date                   startDate,
                                  Date                   completionDate,
                                  String                 completionMessage) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


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
    void updateGovernanceActionStatus(String                 userId,
                                      String                 governanceActionGUID,
                                      GovernanceActionStatus governanceActionStatus) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param requestParameters request properties from the caller (will be passed onto any follow on actions)
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    void recordCompletionStatus(String                userId,
                                String                governanceActionGUID,
                                Map<String, String>   requestParameters,
                                CompletionStatus      status,
                                List<String>          outputGuards,
                                List<NewActionTarget> newActionTargets,
                                String                completionMessage) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;
}
