/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

import java.util.List;

/**
 * GovernanceServiceProvider implements the base class for the connector provider for a governance action service.
 */
public abstract class GovernanceServiceProvider extends ConnectorProviderBase
{
    /**
     * The guards describe the output assessment from the governance action service.
     *
     * @return list of guards that maybe output by this governance action service
     */
    public abstract List<String> supportedGuards();
}
