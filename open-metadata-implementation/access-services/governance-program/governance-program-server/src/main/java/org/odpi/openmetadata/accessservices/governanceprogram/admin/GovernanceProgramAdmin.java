/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.admin;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramAuditCode;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceProgramServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

public class GovernanceProgramAdmin extends AccessServiceAdmin
{
    private OMRSRepositoryConnector           repositoryConnector = null;
    private OMRSTopicConnector                omrsTopicConnector  = null;
    private AccessServiceConfig               accessServiceConfig = null;
    private AuditLog                          auditLog            = null;
    private GovernanceProgramServicesInstance instance            = null;
    private String                            serverName          = null;
    private String                            serverUserName      = null;


    /**
     * Default constructor
     */
    public GovernanceProgramAdmin()
    {
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector - connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector - connector for querying the cohort repositories
     * @param auditLog - audit log component for logging messages.
     * @param serverUserName - user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           AuditLog                auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, GovernanceProgramAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try
        {
            this.auditLog = auditLog;
            this.repositoryConnector = enterpriseOMRSRepositoryConnector;
            this.accessServiceConfig = accessServiceConfigurationProperties;
            this.omrsTopicConnector = enterpriseOMRSTopicConnector;
            this.serverUserName = serverUserName;

            List<String> supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                     accessServiceConfig.getAccessServiceName(),
                                                                     auditLog);

            List<String> defaultZones = this.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                                                                 accessServiceConfig.getAccessServiceName(),
                                                                 auditLog);

            List<String> publishZones = this.extractPublishZones(accessServiceConfig.getAccessServiceOptions(),
                                                                 accessServiceConfig.getAccessServiceName(),
                                                                 auditLog);

            this.instance = new GovernanceProgramServicesInstance(repositoryConnector,
                                                                  supportedZones,
                                                                  defaultZones,
                                                                  publishZones,
                                                                  auditLog,
                                                                  serverUserName,
                                                                  enterpriseOMRSRepositoryConnector.getMaxPageSize());

            this.serverName = instance.getServerName();

            auditLog.logMessage(actionDescription,
                                GovernanceProgramAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfigurationProperties.toString());
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  GovernanceProgramAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfigurationProperties.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceFullName(),
                                                         error);
        }
    }


    /**
     * Shutdown the access service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        if (this.instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, GovernanceProgramAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}
