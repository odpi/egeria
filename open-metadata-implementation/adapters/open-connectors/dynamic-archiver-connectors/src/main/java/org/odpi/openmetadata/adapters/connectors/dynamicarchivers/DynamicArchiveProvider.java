/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.dynamicarchivers;

import org.odpi.openmetadata.engineservices.repositorygovernance.connector.RepositoryGovernanceProvider;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * DynamicArchiveProvider implements the base class for the connector provider for a archive service.
 */
public abstract class DynamicArchiveProvider extends RepositoryGovernanceProvider
{
    static final String ARCHIVE_NAME_PROPERTY = "archiveName";
    static final String ARCHIVE_GUID_PROPERTY = "archiveGUID";

    static final String SNAPSHOT_REQUEST_TYPE    = "snapshot";
    static final String JOURNALLING_REQUEST_TYPE = "journalling";

    static final String ARCHIVING_COMPLETE_GUARD = "archiving-complete";
    static final String ARCHIVING_FAILED_GUARD   = "archiving-failed";

    protected List<String> recognizedConfigurationProperties = new ArrayList<>();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation as well as declare the parameters supported by the archive service.
     */
    public DynamicArchiveProvider()
    {
        super();

        supportedRequestTypes = new ArrayList<>();
        supportedRequestTypes.add(SNAPSHOT_REQUEST_TYPE);
        supportedRequestTypes.add(JOURNALLING_REQUEST_TYPE);

        supportedRequestParameters = new ArrayList<>();
        supportedRequestParameters.add(ARCHIVE_NAME_PROPERTY);
        supportedRequestParameters.add(ARCHIVE_GUID_PROPERTY);

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(ARCHIVE_NAME_PROPERTY);
        supportedTargetActionNames.add(ARCHIVE_GUID_PROPERTY);

        supportedGuards = new ArrayList<>();
        supportedGuards.add(ARCHIVING_COMPLETE_GUARD);
        supportedGuards.add(ARCHIVING_FAILED_GUARD);

        recognizedConfigurationProperties.add(ARCHIVE_NAME_PROPERTY);
        recognizedConfigurationProperties.add(ARCHIVE_GUID_PROPERTY);

        super.setConnectorComponentDescription(OMRSAuditingComponent.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR);
    }
}
