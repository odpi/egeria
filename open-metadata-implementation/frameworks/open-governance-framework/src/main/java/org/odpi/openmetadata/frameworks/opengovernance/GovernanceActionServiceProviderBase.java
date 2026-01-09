/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance;

import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.List;


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

    /**
     * Base provider for governance action services that set up their own provider.
     */
    public GovernanceActionServiceProviderBase()
    {
        super();
    }

    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of property names that the connector supports
     *                                             in the configuration properties.
     */
    public GovernanceActionServiceProviderBase(OpenConnectorDefinition openConnectorDescription,
                                               String                  connectorClassName,
                                               List<String>            recognizedConfigurationPropertyNames)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames);
    }
}
