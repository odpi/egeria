/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram;

import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZone;

/**
 * The GovernanceProgramRolloutInterface covers the definition and management of the campaigns and projects
 * needed to put a governance program in place and keep it vital.
 */
public interface GovernanceProgramRolloutInterface
{
    void createGovernanceZone();
    void updateGovernanceZone();
    void deleteGovernanceZone();

    GovernanceZone getGovernanceZone(String  zoneGUID);

    void assignGovernanceDefinitionToZone();

}
