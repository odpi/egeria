/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.admin;


import org.odpi.openmetadata.accessservices.governanceprogram.auditlog.GovernanceProgramAuditCode;
import org.odpi.openmetadata.accessservices.governanceprogram.listener.GovernanceProgramOMRSTopicListener;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceProgramServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class GovernanceProgramAdmin extends AccessServiceAdmin
{
    private OMRSRepositoryConnector           repositoryConnector = null;
    private OMRSTopicConnector                omrsTopicConnector  = null;
    private AccessServiceConfig               accessServiceConfig = null;
    private OMRSAuditLog                      auditLog            = null;
    private GovernanceProgramServicesInstance instance            = null;
    private String                            serverName          = null;
    private String                            serverUserName      = null;

    private GovernanceProgramOMRSTopicListener omrsTopicListener = null;

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
    public void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           OMRSAuditLog            auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String               actionDescription = "initialize";

        GovernanceProgramAuditCode auditCode = GovernanceProgramAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        try
        {
            this.auditLog = auditLog;
            this.repositoryConnector = enterpriseOMRSRepositoryConnector;
            this.instance = new GovernanceProgramServicesInstance(repositoryConnector,
                                                                  auditLog,
                                                                  serverName,
                                                                  enterpriseOMRSRepositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            this.accessServiceConfig = accessServiceConfigurationProperties;
            this.omrsTopicConnector = enterpriseOMRSTopicConnector;
            this.serverUserName = serverUserName;

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceOutTopic() != null)
            {
                GovernanceProgramOMRSTopicListener omrsTopicListener;

                omrsTopicListener = new GovernanceProgramOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                                                           repositoryConnector.getRepositoryHelper(),
                                                                           repositoryConnector.getRepositoryValidator(),
                                                                           accessServiceConfig.getAccessServiceName());

                super.registerWithEnterpriseTopic(accessServiceConfig.getAccessServiceName(),
                                                  serverName,
                                                  omrsTopicConnector,
                                                  omrsTopicListener,
                                                  auditLog);
            }


            auditCode = GovernanceProgramAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(serverName),
                               accessServiceConfigurationProperties.toString(),
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
        catch (Throwable error)
        {
            auditCode = GovernanceProgramAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logException(actionDescription,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(error.getMessage()),
                                  accessServiceConfigurationProperties.toString(),
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceFullName(),
                                                         error);
        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown()
    {
        final String                actionDescription = "shutdown";
        GovernanceProgramAuditCode  auditCode;

        if (this.instance != null)
        {
            this.instance.shutdown();
        }

        auditCode = GovernanceProgramAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(serverName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}
