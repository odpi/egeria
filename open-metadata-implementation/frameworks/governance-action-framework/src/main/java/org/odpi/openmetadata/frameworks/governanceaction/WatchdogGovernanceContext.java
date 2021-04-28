/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * WatchdogGovernanceContext provides the watchdog governance action service with access to information about
 * the request, open metadata store, the ability to register a listener to receive governance events and initiate
 * new governance activity.
 */
public class WatchdogGovernanceContext extends GovernanceContext
{
    private WatchdogGovernanceListener registeredListener = null;
    private List<WatchdogEventType>    interestingEvents  = null;
    private List<String>               interestingTypes   = null;


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
    public WatchdogGovernanceContext(String                     userId,
                                     String                     governanceActionGUID,
                                     String                     requestType,
                                     Map<String, String>        requestParameters,
                                     List<RequestSourceElement> requestSourceElements,
                                     List<ActionTargetElement>  actionTargetElements,
                                     OpenMetadataClient openMetadataStore)
    {
        super(userId, governanceActionGUID, requestType, requestParameters, requestSourceElements, actionTargetElements, openMetadataStore);
    }


    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata store.
     * There can be only one registered listener.  If this method is called more than once, the new parameters
     * replace the existing parameters.  This means the watchdog governance action service can change the
     * listener and the parameters that control the types of events received while it is running.
     *
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     *
     * If specific instance, interestingEventTypes or interestingMetadataTypes are null, it defaults to "any".
     * If the listener parameter is null, no more events are passed to the listener.
     * The type name specified in the interestingMetadataTypes refers to the subject of the event - so it is the type of the metadata element
     * for metadata element types, the type of the relationship for related elements events and the name of the classification
     * for classification events.
     *
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types
     * @param specificInstance unique identifier of a specific instance (metadata element or relationship) to watch for
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    public void registerListener(WatchdogGovernanceListener listener,
                                 List<WatchdogEventType>    interestingEventTypes,
                                 List<String>               interestingMetadataTypes,
                                 String                     specificInstance) throws InvalidParameterException
    {
        openMetadataStore.registerListener(listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }


    /**
     * Unregister the listener permanently from the event infrastructure.
     */
    void disconnectListener()
    {
        openMetadataStore.disconnectListener();
    }


    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     * @param governanceEngineName name of the governance engine to run the request
     * @param requestType request type to identify the governance action service to run
     * @param requestParameters properties to pass to the governance action service
     *
     * @return unique identifier of the governance action
     *
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceAction(String                qualifiedName,
                                           int                   domainIdentifier,
                                           String                displayName,
                                           String                description,
                                           List<String>          requestSourceGUIDs,
                                           List<NewActionTarget> actionTargets,
                                           Date                  startTime,
                                           String                governanceEngineName,
                                           String                requestType,
                                           Map<String, String>   requestParameters) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return openMetadataStore.initiateGovernanceAction(qualifiedName,
                                                          domainIdentifier,
                                                          displayName,
                                                          description,
                                                          requestSourceGUIDs,
                                                          actionTargets,
                                                          startTime,
                                                          governanceEngineName,
                                                          requestType,
                                                          requestParameters);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestParameters request parameters to pass to the governance actions called in the governance action process
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     *
     * @return unique identifier of the first governance action of the process
     *
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceActionProcess(String                processQualifiedName,
                                                  Map<String, String>   requestParameters,
                                                  List<String>          requestSourceGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Date                  startTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return openMetadataStore.initiateGovernanceActionProcess(processQualifiedName, requestParameters, requestSourceGUIDs, actionTargets, startTime);
    }


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
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createIncidentReport(String                        qualifiedName,
                                       int                           domainIdentifier,
                                       String                        background,
                                       List<IncidentImpactedElement> impactedResources,
                                       List<IncidentDependency>      previousIncidents,
                                       Map<String, Integer>          incidentClassifiers,
                                       Map<String, String>           additionalProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return openMetadataStore.createIncidentReport(qualifiedName,
                                                      domainIdentifier,
                                                      background,
                                                      impactedResources,
                                                      previousIncidents,
                                                      incidentClassifiers,
                                                      additionalProperties);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "WatchdogGovernanceContext{" +
                       "registeredListener=" + registeredListener +
                       ", interestingEvents=" + interestingEvents +
                       ", interestingTypes=" + interestingTypes +
                       ", requestType='" + getRequestType() + '\'' +
                       ", requestParameters=" + getRequestParameters() +
                       ", requestSourceElements=" + getRequestSourceElements() +
                       ", actionTargetElements=" + getActionTargetElements() +
                       ", openMetadataStore=" + getOpenMetadataStore() +
                       '}';
    }
}
