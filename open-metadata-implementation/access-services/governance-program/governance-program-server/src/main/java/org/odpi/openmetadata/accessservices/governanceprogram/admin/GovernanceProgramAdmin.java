/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.admin;


import org.odpi.openmetadata.accessservices.governanceprogram.auditlog.GovernanceProgramAuditCode;
import org.odpi.openmetadata.accessservices.governanceprogram.listener.GovernanceProgramOMRSTopicListener;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceProgramRESTServices;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class GovernanceProgramAdmin implements AccessServiceAdmin
{
    private OMRSRepositoryConnector repositoryConnector = null;
    private OMRSTopicConnector      omrsTopicConnector  = null;
    private AccessServiceConfig     accessServiceConfig = null;
    private OMRSAuditLog            auditLog            = null;
    private String                  serverUserName      = null;

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
        GovernanceProgramAuditCode auditCode;

        auditCode = GovernanceProgramAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        this.repositoryConnector = enterpriseOMRSRepositoryConnector;
        GovernanceProgramRESTServices.setRepositoryConnector(accessServiceConfigurationProperties.getAccessServiceName(),
                                                             repositoryConnector);

        this.accessServiceConfig = accessServiceConfigurationProperties;
        this.omrsTopicConnector = enterpriseOMRSTopicConnector;

        if (omrsTopicConnector != null)
        {
            auditCode = GovernanceProgramAuditCode.SERVICE_REGISTERED_WITH_TOPIC;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            omrsTopicListener = new GovernanceProgramOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                                                       repositoryConnector.getRepositoryHelper(),
                                                                       repositoryConnector.getRepositoryValidator(),
                                                                       accessServiceConfig.getAccessServiceName());

            omrsTopicConnector.registerListener(omrsTopicListener);
        }

        this.auditLog = auditLog;
        this.serverUserName = serverUserName;

        auditCode = GovernanceProgramAuditCode.SERVICE_INITIALIZED;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown()
    {
        final String                actionDescription = "shutdown";
        GovernanceProgramAuditCode  auditCode;

        auditCode = GovernanceProgramAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}
