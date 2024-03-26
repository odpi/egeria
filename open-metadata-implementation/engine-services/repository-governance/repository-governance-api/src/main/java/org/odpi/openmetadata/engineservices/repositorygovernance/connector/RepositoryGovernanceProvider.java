/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.connector;

import org.odpi.openmetadata.frameworks.governanceaction.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;


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
        supportedAssetTypeName = DeployedImplementationType.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR.getAssociatedTypeName();
        supportedDeployedImplementationType = DeployedImplementationType.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR.getDeployedImplementationType();
    }
}
