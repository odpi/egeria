/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.GovernanceServiceProviderBase;


/**
 * GovernanceActionServiceProviderBase implements the base class for the connector provider for a governance action service.
 */
public abstract class GovernanceActionServiceProviderBase extends GovernanceServiceProviderBase
{
    static
    {
        supportedAssetTypeName = OpenMetadataType.GOVERNANCE_ACTION_SERVICE.typeName;
    }
}
