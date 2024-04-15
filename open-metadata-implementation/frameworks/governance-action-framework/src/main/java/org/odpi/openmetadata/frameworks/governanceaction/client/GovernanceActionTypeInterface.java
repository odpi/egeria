/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;

import java.util.Date;
import java.util.List;

/**
 * GovernanceActionTypeInterface maintains Governance Action Types.  These are templates for requesting calls
 * to a governance engine.
 */
public interface GovernanceActionTypeInterface
{
    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param userId calling user
     * @param properties properties about the governance action type to store
     *
     * @return unique identifier of the new governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createGovernanceActionType(String                         userId,
                                      GovernanceActionTypeProperties properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Update the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceActionType(String                         userId,
                                    String                         governanceActionTypeGUID,
                                    boolean                        isMergeUpdate,
                                    GovernanceActionTypeProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;

    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceActionType(String userId,
                                    String governanceActionTypeGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime effective date/time for query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceActionTypeElement> findGovernanceActionTypes(String userId,
                                                                String searchString,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime effective date/time for query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceActionTypeElement> getGovernanceActionTypesByName(String  userId,
                                                                     String  name,
                                                                     int     startFrom,
                                                                     int     pageSize,
                                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException;


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceActionTypeElement getGovernanceActionTypeByGUID(String userId,
                                                              String governanceActionTypeGUID) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException;
}

