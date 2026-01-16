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
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * StewardshipAction defines methods that are used to request help for a situation that the caller can not handle.
 */
public  class StewardshipAction extends ConnectorContextClientBase
{
    private final ActionControlInterface actionControlInterface;


    /**
     * Constructor for the connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openGovernanceClient client to access governance functions
     * @param auditLog logging destination
     * @param maxPageSize max elements that can be returned on a query
     */
    public StewardshipAction(ConnectorContextBase parentContext,
                             String               localServerName,
                             String               localServiceName,
                             String               connectorUserId,
                             String               connectorGUID,
                             String               externalSourceGUID,
                             String               externalSourceName,
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

        this.actionControlInterface = openGovernanceClient;
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
     * @throws PropertyServerException  a problem with the metadata store
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
     * @throws PropertyServerException a problem with the metadata store
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
     * @throws PropertyServerException a problem with the metadata store
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
