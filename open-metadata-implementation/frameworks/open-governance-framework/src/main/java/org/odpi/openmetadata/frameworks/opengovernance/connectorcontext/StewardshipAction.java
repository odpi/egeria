/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ImpactedResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.IncidentReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * StewardshipAction defines methods that are used to request help for a situation that the caller can not handle.
 */
public  class StewardshipAction extends ConnectorContextClientBase
{
    private final OpenMetadataClient     openMetadataClient;
    private final ActionControlInterface actionControlInterface;
    private final String                 originatorGUID;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param openGovernanceClient client to access governance functions
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public StewardshipAction(ConnectorContextBase parentContext,
                             String               localServerName,
                             String               localServiceName,
                             String               connectorUserId,
                             String               connectorGUID,
                             String               externalSourceGUID,
                             String               externalSourceName,
                             OpenMetadataClient   openMetadataClient,
                             OpenGovernanceClient openGovernanceClient,
                             AuditLog             auditLog,
                             int                  maxPageSize)
    {
        super(parentContext,
              localServerName,
              localServiceName,
              connectorUserId,
              connectorGUID,
              externalSourceGUID,
              externalSourceName,
              auditLog,
              maxPageSize);

        this.openMetadataClient     = openMetadataClient;
        this.actionControlInterface = openGovernanceClient;
        this.originatorGUID         = connectorGUID;
    }


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param properties unique identifier to give this new incident report and description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createIncidentReport(IncidentReportProperties                properties,
                                       Map<String, ImpactedResourceProperties> impactedResources,
                                       Map<String, ReportDependencyProperties> previousIncidents,
                                       String                                  originatorGUID) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return openMetadataClient.createIncidentReport(connectorUserId,
                                                       properties,
                                                       impactedResources,
                                                       previousIncidents,
                                                       originatorGUID);
    }


    /**
     * Create a "To Do" request for someone to work on.
     *
     * @param properties unique name for the to do plus additional properties
     * @param assignToGUID unique identifier the Actor element for the recipient
     * @param sponsorGUID unique identifier of the element that describes the rule, project that this is on behalf of
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new to do element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a "to do" entity
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String openToDo(ToDoProperties        properties,
                           String                assignToGUID,
                           String                sponsorGUID,
                           List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return openMetadataClient.openToDo(connectorUserId, properties, assignToGUID, sponsorGUID, originatorGUID, actionTargets);
    }


    /**
     * Create a new context event
     *
     * @param anchorGUID unique identifier for the context event's anchor element
     * @param parentContextEvents which context events should be linked as parents (guid->relationship properties)
     * @param childContextEvents which context events should be linked as children (guid->relationship properties)
     * @param relatedContextEvents which context events should be linked as related (guid->relationship properties)
     * @param impactedElements which elements are impacted by this context event (guid->relationship properties)
     * @param effectedDataResourceGUIDs which data resources are effected by this context event (asset guid->relationship properties)
     * @param contextEventEvidenceGUIDs which elements provide evidence that the context event is happening (element GUIDs)
     * @param contextEventProperties properties for the context event itself
     * @return guid of the new context event
     * @throws InvalidParameterException one of the properties are invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String registerContextEvent(String                                       anchorGUID,
                                       Map<String, DependentContextEventProperties> parentContextEvents,
                                       Map<String, DependentContextEventProperties> childContextEvents,
                                       Map<String, RelatedContextEventProperties>   relatedContextEvents,
                                       Map<String, ContextEventImpactProperties>    impactedElements,
                                       Map<String, RelationshipProperties>          effectedDataResourceGUIDs,
                                       Map<String, RelationshipProperties>          contextEventEvidenceGUIDs,
                                       ContextEventProperties                       contextEventProperties) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        return openMetadataClient.registerContextEvent(connectorUserId, anchorGUID, parentContextEvents, childContextEvents, relatedContextEvents, impactedElements, effectedDataResourceGUIDs, contextEventEvidenceGUIDs, contextEventProperties);
    }


    /**
     * Create an engine action in the metadata store which will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param qualifiedName unique identifier to give this engine action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
     * @param receivedGuards list of guards to initiate the engine action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType governance request type from the caller
     * @param requestParameters properties to pass to the governance service
     * @param processName name of the process that this action is a part of
     * @param requestSourceName source of the request
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the requesting governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException the caller is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateEngineAction(String                qualifiedName,
                                       int                   domainIdentifier,
                                       String                displayName,
                                       String                description,
                                       List<String>          actionSourceGUIDs,
                                       List<String>          actionCauseGUIDs,
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
                                                                                          PropertyServerException
    {
        return actionControlInterface.initiateEngineAction(connectorUserId, qualifiedName, domainIdentifier, displayName, description, actionSourceGUIDs, actionCauseGUIDs, actionTargets, receivedGuards, startTime, governanceEngineName, requestType, requestParameters, processName, requestSourceName, originatorServiceName, originatorEngineName);
    }


    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param governanceActionTypeQualifiedName unique name of the governance action type to use
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
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
    public String initiateGovernanceActionType(String                governanceActionTypeQualifiedName,
                                               List<String>          actionSourceGUIDs,
                                               List<String>          actionCauseGUIDs,
                                               List<NewActionTarget> actionTargets,
                                               Date                  startTime,
                                               Map<String, String>   requestParameters,
                                               String                originatorServiceName,
                                               String                originatorEngineName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return actionControlInterface.initiateGovernanceActionType(connectorUserId, governanceActionTypeQualifiedName, actionSourceGUIDs, actionCauseGUIDs, actionTargets, startTime, requestParameters, originatorServiceName, originatorEngineName);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param processQualifiedName unique name of the governance action process to use
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
     * @param startTime future start time or null for "as soon as possible".
     * @param requestParameters request properties to be passed to the first engine action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the governance action process instance
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException the caller is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceActionProcess(String                processQualifiedName,
                                                  List<String>          actionSourceGUIDs,
                                                  List<String>          actionCauseGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Date                  startTime,
                                                  Map<String, String>   requestParameters,
                                                  String                originatorServiceName,
                                                  String                originatorEngineName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return actionControlInterface.initiateGovernanceActionProcess(connectorUserId, processQualifiedName, actionSourceGUIDs, actionCauseGUIDs, actionTargets, startTime, requestParameters, originatorServiceName, originatorEngineName);
    }


    /**
     * Request that execution of an engine action is stopped.
     *
     * @param engineActionGUID identifier of the engine action request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public void cancelEngineAction(String engineActionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        actionControlInterface.cancelEngineAction(connectorUserId, engineActionGUID);
    }



    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<EngineActionElement>  getActiveEngineActions(int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return actionControlInterface.getActiveEngineActions(connectorUserId, startFrom, pageSize);
    }
}
