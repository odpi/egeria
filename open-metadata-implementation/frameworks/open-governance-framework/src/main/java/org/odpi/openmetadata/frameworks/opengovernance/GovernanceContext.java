/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.connectorcontext.ConnectorConfigClient;
import org.odpi.openmetadata.frameworks.opengovernance.connectorcontext.DuplicateManagementClient;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * GovernanceContext provides the governance action service with access to information about
 * the governance request along with the open metadata repository interfaces.
 * The abstract methods are implemented by the technology that supports the real metadata store.
 */
public interface GovernanceContext
{
    /**
     * Return the unique identifier of the engine action that this service request is associated with.
     *
     * @return string guid
     */
    String getEngineActionGUID();


    /**
     * Return the unique identifier of the asset being discovered.
     *
     * @return string guid
     */
    String getRequestType();


    /**
     * Return the properties that hold the parameters used to drive the governance action service's processing.
     *
     * @return property map
     */
    Map<String, String> getRequestParameters();


    /**
     * Return the userId of the original person, process, service that requested this action.
     *
     * @return string userId
     */
    String getRequesterUserId();


    /**
     * Return the list of metadata elements associated with the request to the governance action service.
     * This list will not change during the lifetime of the service.
     *
     * @return list of request source elements
     */
    List<RequestSourceElement> getRequestSourceElements();


    /**
     * Return the list of elements that this governance action service should work on.
     *
     * @return cached list of action target metadata elements
     */
    List<ActionTargetElement> getActionTargetElements();


    /**
     * Return the maximum number of elements that can be returned on a request.
     *
     * @return int
     */
    int getMaxPageSize();


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
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
    void updateActionTargetStatus(String             actionTargetGUID,
                                  ActivityStatus status,
                                  Date               startDate,
                                  Date               completionDate,
                                  String             completionMessage) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    void recordCompletionStatus(CompletionStatus status,
                                List<String>     outputGuards) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    void recordCompletionStatus(CompletionStatus      status,
                                List<String>          outputGuards,
                                List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;



    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newRequestParameters additional request parameters.  These override/augment any request parameters defined for the next invoked service
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    void recordCompletionStatus(CompletionStatus      status,
                                List<String>          outputGuards,
                                Map<String, String>   newRequestParameters,
                                List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newRequestParameters additional request parameters.  These override/augment any request parameters defined for the next invoked service
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    void recordCompletionStatus(CompletionStatus      status,
                                List<String>          outputGuards,
                                Map<String, String>   newRequestParameters,
                                List<NewActionTarget> newActionTargets,
                                String                completionMessage) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newRequestParameters additional request parameters.  These override/augment any request parameters defined for the next invoked service
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    void recordCompletionStatus(CompletionStatus          status,
                                List<String>              outputGuards,
                                Map<String, String>       newRequestParameters,
                                List<NewActionTarget>     newActionTargets,
                                AuditLogMessageDefinition completionMessage) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Return any completion status from the governance action service.
     *
     * @return completion status enum
     */
    CompletionStatus getCompletionStatus();


    /**
     * Return the client for managing the metadata associated with running connectors, governance engines and governance services.
     *
     * @return connector context client
     */
    ConnectorConfigClient getConnectorConfigClient();


    /**
     * Return the client that manages deduplication.
     *
     * @return deduplication manager
     */
    DuplicateManagementClient getDuplicateManagementClient();
}
