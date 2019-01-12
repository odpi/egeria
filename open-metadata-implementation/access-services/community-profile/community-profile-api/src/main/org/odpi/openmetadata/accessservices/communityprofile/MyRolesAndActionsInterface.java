/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.NoProfileForUserException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.communityprofile.properties.*;

import java.util.List;

/**
 * MyRolesAndActionsInterface covers the ability to query a person's roles and any actions (to dos) assigned
 * to those roles.
 */
public interface MyRolesAndActionsInterface
{

    /**
     * Return a list of the personal roles that the calling user is appointed to.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of personal role details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<PersonalRole> getMyPersonalRoles(String userId,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                 NoProfileForUserException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;

    /**
     * Return a list of to dos assigned to the calling user.
     *
     * @param userId     userId of user making request.
     * @param status     status of the to do (null means current active)
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of to do details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<ToDo> getMyToDos(String     userId,
                          ToDoStatus status,
                          int        startFrom,
                          int        pageSize) throws InvalidParameterException,
                                                      NoProfileForUserException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;


    /**
     * Return a list of to dos assigned to the calling user.
     *
     * @param userId           userId of user making request.
     * @param personalRoleGUID unique identifier of the user's role.
     * @param status           status of the to do (null means current active)
     * @param startFrom        index of the list to start from (0 for start)
     * @param pageSize         maximum number of elements to return.
     *
     * @return list of to do details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<ToDo> getMyToDosByRole(String     userId,
                                String     personalRoleGUID,
                                ToDoStatus status,
                                int        startFrom,
                                int        pageSize) throws InvalidParameterException,
                                                            NoProfileForUserException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;
}
