/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.connector;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceServiceProviderBase;


/**
 * RepositoryGovernanceProvider implements the base class for the connector provider for a repository governance service.
 */
public abstract class RepositoryGovernanceProvider extends GovernanceServiceProviderBase
{
    /*
     * The type name of the asset that this connector supports.
     */
    static
    {
        supportedAssetTypeName = OpenMetadataType.REPOSITORY_GOVERNANCE_SERVICE.typeName;
    }
}
