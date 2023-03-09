/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.connector;

import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * RepositoryGovernanceProvider implements the base class for the connector provider for a repository governance service.
 */
public abstract class RepositoryGovernanceProvider extends GovernanceActionServiceProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation as well as declare the parameters supported by the archive service.
     */
    public RepositoryGovernanceProvider()
    {
        super();

        super.setConnectorComponentDescription(OMRSAuditingComponent.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR);
    }
}
