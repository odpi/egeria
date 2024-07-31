/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.TeamMembershipElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CommunityMembershipElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * MyPersonalNetworkInterface provides interfaces to query the personal network of the individual.
 * This includes the teams and communities they belong to.
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
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CommunityMembershipElement> getMyCommunities(String userId,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
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
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<TeamMembershipElement> getMyTeams(String userId,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;
}
