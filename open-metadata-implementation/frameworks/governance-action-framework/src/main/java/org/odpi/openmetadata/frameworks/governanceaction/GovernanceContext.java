/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;

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
     * Return the client to access metadata from the open metadata repositories.  This enables the
     * governance action service to retrieve more information about the metadata elements linked to the
     * request source and action target elements.
     *
     * @return  metadata store client
     */
    OpenMetadataStore getOpenMetadataStore();


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     *
     * @return unique identifier of the resulting incident report
     *
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createIncidentReport(String                        qualifiedName,
                                int                           domainIdentifier,
                                String                        background,
                                List<IncidentImpactedElement> impactedResources,
                                List<IncidentDependency>      previousIncidents,
                                Map<String, Integer>          incidentClassifiers,
                                Map<String, String>           additionalProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


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
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    void updateActionTargetStatus(String                 actionTargetGUID,
                                  GovernanceActionStatus status,
                                  Date                   startDate,
                                  Date                   completionDate) throws InvalidParameterException,
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
    void recordCompletionStatus(CompletionStatus    status,
                                List<String>        outputGuards) throws InvalidParameterException,
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
     * Return any completion status from the governance action service.
     *
     * @return completion status enum
     */
    CompletionStatus getCompletionStatus();
}
