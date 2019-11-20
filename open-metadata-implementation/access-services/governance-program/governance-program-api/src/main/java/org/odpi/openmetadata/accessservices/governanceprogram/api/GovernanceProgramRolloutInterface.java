/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneDefinition;

/**
 * The GovernanceProgramRolloutInterface covers the definition and management of the campaigns and projects
 * needed to put a governance program in place and keep it vital.
 */
public interface GovernanceProgramRolloutInterface
{
    void createGovernanceZone();
    void updateGovernanceZone();
    void deleteGovernanceZone();

    GovernanceZoneDefinition getGovernanceZone(String  zoneGUID);

    void assignGovernanceDefinitionToZone();

}
