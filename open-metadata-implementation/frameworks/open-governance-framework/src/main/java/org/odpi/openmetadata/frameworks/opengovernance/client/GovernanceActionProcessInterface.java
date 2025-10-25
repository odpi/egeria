/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.client;

import org.odpi.openmetadata.frameworks.opengovernance.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;

import java.util.Date;
import java.util.List;

/**
 * GovernanceActionProcessInterface maintains Governance Action Processes and their Governance Action Process Steps.
 */
public interface GovernanceActionProcessInterface
{
    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with its process flow implementation.  This includes the process steps and the links
     * between them
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime effective date/time for query
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceActionProcessGraph getGovernanceActionProcessGraph(String userId,
                                                                 String processGUID,
                                                                 Date   effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


}

