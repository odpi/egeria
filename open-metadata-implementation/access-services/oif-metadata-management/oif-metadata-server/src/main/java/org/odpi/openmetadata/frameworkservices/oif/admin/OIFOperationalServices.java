/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.frameworkservices.oif.ffdc.OpenIntegrationAuditCode;
import org.odpi.openmetadata.frameworkservices.oif.server.OIFServicesInstance;
import org.odpi.openmetadata.frameworkservices.oif.server.OIFServicesInstanceHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * OIFOperationalServices initializes the REST Services that support the Open Integration Framework (OIF)
 * open integration calls.
 */
public class OIFOperationalServices extends AccessServiceAdmin
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
        this.auditLog = auditLog;

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, OpenIntegrationAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try
        {
            auditLog.logMessage(actionDescription, OpenIntegrationAuditCode.SERVICE_INITIALIZED.getMessageDefinition(localServerName));

            new OIFServicesInstance(enterpriseOMRSRepositoryConnector, auditLog, localServerUserId, maxPageSize);
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OpenIntegrationAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.OIF_METADATA_MANAGEMENT.getServiceName(),
                                                         error);
        }
    }


    /**
     * Shutdown the service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        this.auditLog.logMessage(actionDescription, OpenIntegrationAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(localServerName));

        new OIFServicesInstanceHandler().removeServerServiceInstance(localServerName);
    }
}
