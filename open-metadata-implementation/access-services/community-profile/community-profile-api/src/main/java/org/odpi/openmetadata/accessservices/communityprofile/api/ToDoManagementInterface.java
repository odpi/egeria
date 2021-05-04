/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.ToDoElement;
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
     *
     * @param userId
     * @param toDoGUID
     * @param isMergeUpdate
     * @param toDoProperties
     * @throws InvalidParameterException
     * @throws PropertyServerException
     * @throws UserNotAuthorizedException
     */
    void updateToDo(String         userId,
                    String         toDoGUID,
                    boolean        isMergeUpdate,
                    ToDoProperties toDoProperties) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;


    void deleteToDo(String userId,
                    String toDoGUID) throws InvalidParameterException,
                                            PropertyServerException,
                                            UserNotAuthorizedException;


    ToDoElement getToDo(String userId,
                        String toDoGUID) throws InvalidParameterException,
                                                PropertyServerException,
                                                UserNotAuthorizedException;

    List<ToDoElement> getActionsForElement(String userId,
                                           String elementGUID,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;

    List<ToDoElement> getAssignedActions(String userId,
                                         String roleGUID,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;
}
