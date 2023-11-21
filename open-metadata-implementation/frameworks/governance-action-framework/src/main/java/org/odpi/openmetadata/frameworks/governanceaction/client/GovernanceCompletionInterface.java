/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceCompletionInterface provides support for completing a governance service.
 */
public interface GovernanceCompletionInterface
{
    /**
     * Update the status of the engine action - providing the caller is permitted.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param engineActionStatus new status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    void updateEngineActionStatus(String             userId,
                                  String             engineActionGUID,
                                  EngineActionStatus engineActionStatus) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Retrieve the engine actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    List<EngineActionElement> getActiveClaimedEngineActions(String userId,
                                                            String governanceEngineGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Request that execution of a engine action is allocated to the caller.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    void claimEngineAction(String userId,
                           String engineActionGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param userId caller's userId
     * @param actionTargetGUID unique identifier of the target element.
     * @param status status enum to show its progress
     * @param startDate date/time that the governance action service started processing the target
     * @param completionDate date/time that the governance process completed processing this target.
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    void updateActionTargetStatus(String             userId,
                                  String             actionTargetGUID,
                                  EngineActionStatus status,
                                  Date               startDate,
                                  Date               completionDate,
                                  String             completionMessage) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;





    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param userId caller's userId
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
