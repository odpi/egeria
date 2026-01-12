/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.connector;

import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.List;


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

    /**
     * Constructor where subclass sets up the connector provider.
     */
    public RepositoryGovernanceProvider()
    {
        super();
    }


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of recognized configuration property names
     */
    public RepositoryGovernanceProvider(OpenConnectorDefinition openConnectorDescription,
                                        String                  connectorClassName,
                                        List<String> recognizedConfigurationPropertyNames)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames);
    }
}
