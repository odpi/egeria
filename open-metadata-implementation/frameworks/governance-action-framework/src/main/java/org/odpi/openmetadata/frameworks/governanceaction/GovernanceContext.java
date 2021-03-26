/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GovernanceContext provides the governance action service with access to information about
 * the governance request along with the open metadata repository interfaces.
 * The abstract methods are implemented by the technology that supports the real metadata store.
 */
public class GovernanceContext
{
    private String                     userId;

    private String                     requestType;
    private Map<String, String>        requestParameters;

    private List<RequestSourceElement> requestSourceElements;
    private List<ActionTargetElement>  actionTargetElements;

    private String                     governanceActionGUID;

    private volatile CompletionStatus  completionStatus = null;

    OpenMetadataClient       openMetadataStore;
    PropertyHelper           propertyHelper = new PropertyHelper();

    /**
     * Constructor sets up the key parameters for processing the request to the governance action service.
     *
     * @param userId calling user
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param openMetadataStore client to the metadata store for use by the governance action service
     */
    public GovernanceContext(String                     userId,
                             String                     governanceActionGUID,
                             String                     requestType,
                             Map<String, String>        requestParameters,
                             List<RequestSourceElement> requestSourceElements,
                             List<ActionTargetElement>  actionTargetElements,
                             OpenMetadataClient         openMetadataStore)
    {
        this.userId = userId;
        this.governanceActionGUID = governanceActionGUID;
        this.requestType = requestType;
        this.requestParameters = requestParameters;
        this.requestSourceElements = requestSourceElements;
        this.actionTargetElements = actionTargetElements;
        this.openMetadataStore = openMetadataStore;
    }


    /**
     * Return the unique identifier of the asset being discovered.
     *
     * @return string guid
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Return the properties that hold the parameters used to drive the governance action service's processing.
     *
     * @return property map
     */
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    /**
     * Return the list of metadata elements associated with the request to the governance action service.
     * This list will not change during the lifetime of the service.
     *
     * @return list of request source elements
     */
    public List<RequestSourceElement> getRequestSourceElements()
    {
        return requestSourceElements;
    }


    /**
     * Return the list of elements that this governance action service should work on.
     *
     * @return cached list of action target metadata elements
     */
    public List<ActionTargetElement> getActionTargetElements()
    {
        return actionTargetElements;
    }


    /**
     * Return the client to access metadata from the open metadata repositories.  This enables the
     * governance action service to retrieve more information about the metadata elements linked to the
     * request source and action target elements.
     *
     * @return  metadata store client
     */
    public OpenMetadataStore getOpenMetadataStore()
    {
        return openMetadataStore;
    }


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
    public void updateActionTargetStatus(String                 actionTargetGUID,
                                         GovernanceActionStatus status,
                                         Date                   startDate,
                                         Date                   completionDate) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        openMetadataStore.updateActionTargetStatus(actionTargetGUID, status, startDate, completionDate);
    }


    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public synchronized  void recordCompletionStatus(CompletionStatus    status,
                                                     List<String>        outputGuards) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        this.completionStatus = status;

        openMetadataStore.recordCompletionStatus(status, outputGuards, requestParameters, null);
    }


    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
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
    public synchronized  void recordCompletionStatus(CompletionStatus      status,
                                                     List<String>          outputGuards,
                                                     List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        this.completionStatus = status;

        openMetadataStore.recordCompletionStatus(status, outputGuards, requestParameters, newActionTargets);
    }


    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
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
    public synchronized  void recordCompletionStatus(CompletionStatus      status,
                                                     List<String>          outputGuards,
                                                     Map<String, String>   newRequestParameters,
                                                     List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        this.completionStatus = status;

        Map<String, String> combinedRequestParameters = new HashMap<>();

        if (requestParameters != null)
        {
            combinedRequestParameters.putAll(requestParameters);
        }

        if (newRequestParameters != null)
        {
            combinedRequestParameters.putAll(newRequestParameters);
        }

        openMetadataStore.recordCompletionStatus(status, outputGuards, combinedRequestParameters, newActionTargets);
    }


    /**
     * Return any completion status from the governance action service.
     *
     * @return completion status enum
     */
    public synchronized CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceContext{" +
                       "userId='" + userId + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestParameters=" + requestParameters +
                       ", requestSourceElements=" + requestSourceElements +
                       ", actionTargetElements=" + actionTargetElements +
                       ", completionStatus=" + completionStatus +
                       ", openMetadataStore=" + openMetadataStore +
                       ", propertyHelper=" + propertyHelper +
                       '}';
    }
}
