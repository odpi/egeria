/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram;

import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneInAction;

import java.util.List;

/**
 * The GovernanceProgramReviewInterface supports the periodic review of the governance program.
 * This includes looking at the metrics and the governance zones.
 */
public interface GovernanceProgramReviewInterface
{

    List<String>     getGovernanceZoneIds();

    GovernanceZoneInAction getGovernanceZoneInAction(String    zoneGUID);

    List<String>     getGovernanceZoneMembers(String   zoneGUID,
                                              int      startOffset,
                                              int      maxPageSize);


}
