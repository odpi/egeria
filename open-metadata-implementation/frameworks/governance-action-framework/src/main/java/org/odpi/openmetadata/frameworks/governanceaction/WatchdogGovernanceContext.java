/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * WatchdogGovernanceContext provides the watchdog governance action service with access to information about
 * the request, open metadata store, the ability to register a listener to receive governance events and initiate
 * new governance activity.
 */
public interface WatchdogGovernanceContext extends GovernanceContext
{
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
    void registerListener(WatchdogGovernanceListener listener,
                          List<WatchdogEventType>    interestingEventTypes,
                          List<String>               interestingMetadataTypes,
                          String                     specificInstance) throws InvalidParameterException;


    /**
     * Unregister the listener permanently from the event infrastructure.
     */
    void disconnectListener();


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
    String initiateEngineAction(String                qualifiedName,
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
                                                                                    PropertyServerException;


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
     * @param processName name of the process that this action is a part of
     *
     * @return unique identifier of the governance action
     *
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String initiateEngineAction(String                qualifiedName,
                                int                   domainIdentifier,
                                String                displayName,
                                String                description,
                                List<String>          requestSourceGUIDs,
                                List<NewActionTarget> actionTargets,
                                Date                  startTime,
                                String                governanceEngineName,
                                String                requestType,
                                Map<String, String>   requestParameters,
                                String                processName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;

    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param governanceActionTypeQualifiedName unique name of the governance action type to use
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param startTime future start time or null for "as soon as possible".
     * @param requestParameters request properties to be passed to the engine action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null or unrecognized qualified name of the type
     * @throws UserNotAuthorizedException the caller is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String initiateGovernanceActionType(String                governanceActionTypeQualifiedName,
                                        List<String>          requestSourceGUIDs,
                                        List<NewActionTarget> actionTargets,
                                        Date                  startTime,
                                        Map<String, String>   requestParameters,
                                        String                originatorServiceName,
                                        String                originatorEngineName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;

    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestParameters request parameters to pass to the governance actions called in the governance action process
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     *
     * @return unique identifier of the governance action process instance
     *
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String initiateGovernanceActionProcess(String                processQualifiedName,
                                           Map<String, String>   requestParameters,
                                           List<String>          requestSourceGUIDs,
                                           List<NewActionTarget> actionTargets,
                                           Date                  startTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;
}
