/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
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
}
