/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.accessservices.communityprofile.properties.NewActionTargetProperties;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ToDoElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActionTargetProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoStatus;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 *
 */
public interface ToDoManagementInterface
{
    /**
     * Create a new to do action and link it to the supplied role and targets (if applicable).
     *
     * @param userId calling user
     * @param originatorGUID            optional originator element (such as a person or Governance Service)
     * @param actionOwnerGUID           optional element that maintains the "To Do" on their list
     * @param assignToActorGUID optional actor to assign the action to
     * @param newActionTargetProperties optional list of elements that the action is to target
     * @param properties properties of the to do action
     *
     * @return unique identifier of the to do
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    String createToDo(String                          userId,
                      String                          originatorGUID,
                      String                          actionOwnerGUID,
                      String                          assignToActorGUID,
                      List<NewActionTargetProperties> newActionTargetProperties,
                      ToDoProperties                  properties) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;


    /**
     * Update the properties associated with a "To Do".
     *
     * @param userId calling user
     * @param toDoGUID unique identifier of the to do
     * @param isMergeUpdate should the toDoProperties overlay the existing stored properties or replace them
     * @param toDoProperties properties to change
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    void updateToDo(String         userId,
                    String         toDoGUID,
                    boolean        isMergeUpdate,
                    ToDoProperties toDoProperties) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;


    /**
     * Update the properties associated with an Action Target.
     *
     * @param userId calling user
     * @param actionTargetGUID               unique identifier of the action target relationship
     * @param isMergeUpdate should the actionTargetProperties overlay the existing stored properties or replace them
     * @param actionTargetProperties properties to change
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    void updateActionTargetProperties(String                 userId,
                                      String                 actionTargetGUID,
                                      boolean                isMergeUpdate,
                                      ActionTargetProperties actionTargetProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException;


    /**
     * Assign a "To Do" to a new actor.
     *
     * @param userId calling user
     * @param toDoGUID unique identifier of the to do
     * @param actorGUID  actor to assign the action to
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    void reassignToDo(String         userId,
                      String         toDoGUID,
                      String         actorGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException;


    /**
     * Delete an existing to do.
     *
     * @param userId calling user
     * @param toDoGUID unique identifier of the to do
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    void deleteToDo(String userId,
                    String toDoGUID) throws InvalidParameterException,
                                            PropertyServerException,
                                            UserNotAuthorizedException;


    /**
     * Retrieve a "To Do" by unique identifier.
     *
     * @param userId calling user
     * @param toDoGUID unique identifier of the to do
     *
     * @return to do bean
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    ToDoElement getToDo(String userId,
                        String toDoGUID) throws InvalidParameterException,
                                                PropertyServerException,
                                                UserNotAuthorizedException;


    /**
     * Retrieve the "To Dos" that are chained off of an action target element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to start with
     * @param toDoStatus optional "To Do" status
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     *
     * @return list of to do beans
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    List<ToDoElement> getActionsForActionTarget(String     userId,
                                                String     elementGUID,
                                                ToDoStatus toDoStatus,
                                                int        startFrom,
                                                int        pageSize) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;

    /**
     * Retrieve the "To Dos" that are chained off of a sponsor's element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to start with
     * @param toDoStatus optional "To Do" status
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     *
     * @return list of to do beans
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    List<ToDoElement> getActionsForSponsor(String     userId,
                                           String     elementGUID,
                                           ToDoStatus toDoStatus,
                                           int        startFrom,
                                           int        pageSize) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


    /**
     * Retrieve the "To Dos" for a particular actor.
     *
     * @param userId calling user
     * @param actorGUID unique identifier of the role
     * @param toDoStatus optional "To Do" status
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     *
     * @return list of to do beans
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    List<ToDoElement> getAssignedActions(String     userId,
                                         String     actorGUID,
                                         ToDoStatus toDoStatus,
                                         int        startFrom,
                                         int        pageSize) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException;


    /**
     * Retrieve the "To Dos" that match the search string.
     *
     * @param userId calling user
     * @param searchString string to search for (may include RegExs)
     * @param toDoStatus optional "To Do" status
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     *
     * @return list of to do beans
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    List<ToDoElement> findToDos(String     userId,
                                String     searchString,
                                ToDoStatus toDoStatus,
                                int        startFrom,
                                int        pageSize) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;


    /**
     * Retrieve the "To Dos" that match the type name and status.
     *
     * @param userId calling user
     * @param toDoType   type to search for
     * @param toDoStatus optional "To Do" status
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     *
     * @return list of to do beans
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    List<ToDoElement> getToDosByType(String     userId,
                                     String     toDoType,
                                     ToDoStatus toDoStatus,
                                     int        startFrom,
                                     int        pageSize) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;
}
