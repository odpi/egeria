/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile;


import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.NoProfileForUserException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Community;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonRole;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Team;

import java.util.List;

/**
 * MyPersonalNetworkInterface provides interfaces to query the personal network of the individual.
 * This includes the teams and communities they belong and the roles they have.
 */
public interface MyPersonalNetworkInterface
{
    /**
     * Return a list of communities that the specified user is a member of.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of community details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not be a member of any communities.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<Community> getMyCommunities(String    userId,
                                     int       startFrom,
                                     int       pageSize) throws InvalidParameterException,
                                                                NoProfileForUserException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;

    /**
     * Return a list of roles that the specified user is a member of.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of role details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any roles.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<PersonRole> getMyRoles(String    userId,
                                int       startFrom,
                                int       pageSize) throws InvalidParameterException,
                                                           NoProfileForUserException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException;


    /**
     * Return a list of teams that the specified user is a member of.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of team details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not register as a member of any teams.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<Team> getMyTeams(String    userId,
                          int       startFrom,
                          int       pageSize) throws InvalidParameterException,
                                                     NoProfileForUserException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException;
}
