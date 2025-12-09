/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ImpactedResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.IncidentReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * StewardshipRequestInterface defines methods that are used to request help for a situation that the caller can not handle.
 */
public interface StewardshipRequestInterface
{
    /**
     * Create an incident report to capture the situation detected by the caller.
     * This incident report will be processed by other governance activities.
     *
     * @param userId caller's userId
     * @param properties unique identifier to give this new incident report and description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createIncidentReport(String                                  userId,
                                IncidentReportProperties                properties,
                                Map<String, ImpactedResourceProperties> impactedResources,
                                Map<String, ReportDependencyProperties> previousIncidents,
                                String                                  originatorGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Create a "To Do" request for someone to work on.
     *
     * @param userId caller's userId
     * @param properties unique name for the to do plus additional  properties
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param sponsorGUID unique identifier of the element that describes the rule, project that this is on behalf of
     * @param originatorGUID unique identifier of the source of the to do
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new to do element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    String openToDo(String                userId,
                    ToDoProperties        properties,
                    String                assignToGUID,
                    String                sponsorGUID,
                    String                originatorGUID,
                    List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Create an action request for someone to work on.
     *
     * @param userId caller's userId
     * @param openMetadataTypeName type of action to create
     * @param properties properties of the action
     * @param actionSourceGUID unique identifier of the source of the action
     * @param actionCauseGUIDs unique identifiers of the cause for the action to be raised
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new action element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    String createActorAction(String                userId,
                             String                openMetadataTypeName,
                             NewElementProperties  properties,
                             String                actionSourceGUID,
                             List<String>          actionCauseGUIDs,
                             String                assignToGUID,
                             List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Create an entry in a note log.
     *
     * @param userId caller's userId
     * @param openMetadataTypeName type of action to create
     * @param properties properties of the action
     * @param actionSourceGUID unique identifier of the source of the action
     * @param actionCauseGUIDs unique identifiers of the cause for the action to be raised
     * @param noteLogGUID unique identifier of the note log
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new action element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    String createNoteLogEntry(String                userId,
                              String                openMetadataTypeName,
                              NewElementProperties  properties,
                              String                actionSourceGUID,
                              List<String>          actionCauseGUIDs,
                              String                noteLogGUID,
                              List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Create a new context event
     *
     * @param userId calling user
     * @param anchorGUID unique identifier for the context event's anchor element
     * @param parentContextEvents which context events should be linked as parents (guid->relationship properties)
     * @param childContextEvents which context events should be linked as children (guid->relationship properties)
     * @param relatedContextEvents which context events should be linked as related (guid->relationship properties)
     * @param impactedElements which elements are impacted by this context event (guid->relationship properties)
     * @param effectedDataResourceGUIDs which data resources are effected by this context event (asset guid->effectivity dates)
     * @param contextEventEvidenceGUIDs which elements provide evidence that the context event is happening (element GUIDs-> effectivity dates)
     * @param contextEventProperties properties for the context event itself
     * @return guid of the new context event
     * @throws InvalidParameterException one of the properties are invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    String registerContextEvent(String                                       userId,
                                String                                       anchorGUID,
                                Map<String, DependentContextEventProperties> parentContextEvents,
                                Map<String, DependentContextEventProperties> childContextEvents,
                                Map<String, RelatedContextEventProperties>   relatedContextEvents,
                                Map<String, ContextEventImpactProperties>    impactedElements,
                                Map<String, RelationshipProperties>          effectedDataResourceGUIDs,
                                Map<String, RelationshipProperties>          contextEventEvidenceGUIDs,
                                ContextEventProperties                       contextEventProperties) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException;
}
