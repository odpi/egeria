/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram;

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
