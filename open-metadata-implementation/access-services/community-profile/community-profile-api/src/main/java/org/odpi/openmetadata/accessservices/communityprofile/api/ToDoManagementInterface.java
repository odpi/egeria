/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ToDoElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public interface ToDoManagementInterface
{
    /**
     * Create a new to do action and link it to the supplied role and targets (if applicable).
     *
     * @param userId calling user
     * @param originatorGUID optional originator element (such as a meeting or project or asset)
     * @param assignToRoleGUID optional role to assign the action to
     * @param targetElementGUIDs optional list of elements that the action is to target
     * @param properties properties of the to do action
     *
     * @return unique identifier of the to do
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    String createToDo(String         userId,
                      String         originatorGUID,
                      String         assignToRoleGUID,
                      List<String>   targetElementGUIDs,
                      ToDoProperties properties) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException;


    /**
     * Update the properties associated with a To Do.
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
     * Retrieve a to do by unique identifier.
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
     * Retrieve the to dos that are chained off of an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to start with
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     *
     * @return list of to do beans
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    List<ToDoElement> getActionsForElement(String userId,
                                           String elementGUID,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;


    /**
     * Retrieve the to dos for a particular person role.
     *
     * @param userId calling user
     * @param roleGUID unique identifier of the role
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     *
     * @return list of to do beans
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    List<ToDoElement> getAssignedActions(String userId,
                                         String roleGUID,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;
}
