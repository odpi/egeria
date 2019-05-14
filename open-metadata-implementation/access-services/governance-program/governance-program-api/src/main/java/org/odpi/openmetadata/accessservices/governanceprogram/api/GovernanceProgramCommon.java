/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionStatus;

/**
 * GovernanceProgramExtra provides support for common operations that are ued throughout the governance
 * program's lifecycle.
 */
public interface GovernanceProgramCommon
{

    void createExternalReference();
    void updateExternalReference();
    void deleteExternalReference();
    ExternalReference getExternalReference();

    void assignExternalReferenceToGovernanceDefinition();


    void setGovernanceDefinitionStatus(String         governanceDefinitionGUID,
                                       GovernanceDefinitionStatus newStatus);


}
