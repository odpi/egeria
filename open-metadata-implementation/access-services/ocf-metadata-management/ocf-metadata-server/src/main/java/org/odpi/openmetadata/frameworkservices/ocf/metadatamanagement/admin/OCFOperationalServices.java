/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.ffdc.OCFServicesAuditCode;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server.OCFServicesInstanceHandler;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server.OCFServicesInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;


/**
 * OCFOperationalServices initializes the REST Services that support the Open Connector Framework (OCF)
 * connected asset properties calls.
 */
public class OCFOperationalServices extends AccessServiceAdmin
{
    private String   localServerName = null;
    private AuditLog auditLog = null;


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig  specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param localServerName name of this server
     * @param localServerUserId  user id to use on OMRS calls where there is no end user.
     * @param localServerPassword  password to use on OMRS calls where there is no end user.
     * @param maxPageSize max number of results to return on single request.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig     accessServiceConfig,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           AuditLog                auditLog,
                           String                  localServerName,
                           String                  localServerUserId,
                           String                  localServerPassword,
                           int                     maxPageSize) throws OMAGConfigurationErrorException
    {
        this.localServerName = localServerName;
        this.auditLog        = auditLog;

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, OCFServicesAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try
        {
            auditLog.logMessage(actionDescription, OCFServicesAuditCode.SERVICE_INITIALIZED.getMessageDefinition(this.localServerName));

            new OCFServicesInstance(enterpriseOMRSRepositoryConnector, auditLog, localServerUserId, maxPageSize);
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OCFServicesAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.OCF_METADATA_MANAGEMENT.getServiceName(),
                                                         error);
        }
    }


    /**
     * Shutdown the service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        this.auditLog.logMessage(actionDescription, OCFServicesAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(localServerName));

        new OCFServicesInstanceHandler().removeServerServiceInstance(localServerName);
    }
}
