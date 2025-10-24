/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.client;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The ActionControlInterface provides the methods used by governance services to initiate new automated actions.
 */
public interface ActionControlInterface
{
    /**
     * Create an engine action in the metadata store which will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
     * @param receivedGuards list of guards to initiate the governance action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance action service to run
     * @param requestParameters properties to pass to the governance action service
     * @param processName name of the process that this action is a part of
     * @param requestSourceName source of the request
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the requesting governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String initiateEngineAction(String                userId,
                                String                qualifiedName,
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
                                                                                   PropertyServerException;


    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param userId caller's userId
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
    String initiateGovernanceActionType(String                userId,
                                        String                governanceActionTypeQualifiedName,
                                        List<String>          actionSourceGUIDs,
                                        List<String>          actionCauseGUIDs,
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
     * @param userId caller's userId
     * @param processQualifiedName unique name of the governance action process to use
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
     * @param startTime future start time or null for "as soon as possible"
     * @param requestParameters request properties to be passed to the first governance action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the first governance action of the process
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String initiateGovernanceActionProcess(String                userId,
                                           String                processQualifiedName,
                                           List<String>          actionSourceGUIDs,
                                           List<String>          actionCauseGUIDs,
                                           List<NewActionTarget> actionTargets,
                                           Date                  startTime,
                                           Map<String, String>   requestParameters,
                                           String                originatorServiceName,
                                           String                originatorEngineName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;

    /**
     * Request the status of an executing engine action request.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    EngineActionElement getEngineAction(String userId,
                                        String engineActionGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Request that execution of an engine action is stopped.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    void cancelEngineAction(String userId,
                            String engineActionGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    List<EngineActionElement> getActiveEngineActions(String userId,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;



    /**
     * Retrieve the list of engine action metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchOptions additional options for the search
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EngineActionElement> findEngineActions(String        userId,
                                                String        searchString,
                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Retrieve the list of engine action metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param queryOptions additional options to control the results of the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EngineActionElement> getEngineActionsByName(String       userId,
                                                     String       name,
                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;
}
