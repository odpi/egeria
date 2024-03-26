/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;


/**
 * GovernanceActionServiceProviderBase implements the base class for the connector provider for a governance action service.
 */
public abstract class GovernanceActionServiceProviderBase extends GovernanceServiceProviderBase
{
    static
    {
        supportedAssetTypeName = DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName();
        supportedDeployedImplementationType = DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType();
    }
}
