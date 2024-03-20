/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.dynamicarchivers;

import org.odpi.openmetadata.engineservices.repositorygovernance.connector.RepositoryGovernanceProvider;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DynamicArchiveProvider implements the base class for the connector provider for an archive service.
 */
public abstract class DynamicArchiveProvider extends RepositoryGovernanceProvider
{
    protected List<String> recognizedConfigurationProperties = new ArrayList<>();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation as well as declare the parameters supported by the archive service.
     */
    public DynamicArchiveProvider()
    {
        super();

        supportedRequestTypes = DynamicArchiveRequestType.getRequestTypeTypes();
        supportedRequestParameters = DynamicArchiveRequestParameter.getRequestParameterTypes();


        producedGuards = DynamicArchiveGuard.getGuardTypes();

        recognizedConfigurationProperties.add(DynamicArchiveRequestParameter.ARCHIVE_NAME.getName());
        recognizedConfigurationProperties.add(DynamicArchiveRequestParameter.ARCHIVE_GUID.getName());

        super.setConnectorComponentDescription(OMRSAuditingComponent.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR);
    }
}
