/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.admin;

import org.odpi.openmetadata.accessservices.communityprofile.auditlog.CommunityProfileAuditCode;
import org.odpi.openmetadata.accessservices.communityprofile.listener.CommunityProfileOMRSTopicListener;
import org.odpi.openmetadata.accessservices.communityprofile.server.CommunityProfileServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class CommunityProfileAdmin implements AccessServiceAdmin
{
    private OMRSRepositoryConnector            repositoryConnector = null;
    private OMRSTopicConnector                 omrsTopicConnector  = null;
    private AccessServiceConfig                accessServiceConfig = null;
    private OMRSAuditLog                       auditLog            = null;
    private CommunityProfileServicesInstance   instance            = null;
    private String                             serverName          = null;
    private String                             serverUserName      = null;
    private CommunityProfileOMRSTopicListener  omrsTopicListener   = null;

    /**
     * Default constructor
     */
    public CommunityProfileAdmin()
    {
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties  specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           OMRSAuditLog            auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String              actionDescription = "initialize";
        CommunityProfileAuditCode auditCode;

        auditCode = CommunityProfileAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        try
        {
            this.repositoryConnector = enterpriseOMRSRepositoryConnector;
            this.instance = new CommunityProfileServicesInstance(repositoryConnector);
            this.serverName = instance.getServerName();

            this.accessServiceConfig = accessServiceConfigurationProperties;
            this.omrsTopicConnector = enterpriseOMRSTopicConnector;
            this.serverUserName = serverUserName;

            if (omrsTopicConnector != null)
            {
                auditCode = CommunityProfileAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(serverName),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());

                omrsTopicListener = new CommunityProfileOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                                                          repositoryConnector.getRepositoryHelper(),
                                                                          repositoryConnector.getRepositoryValidator(),
                                                                          accessServiceConfig.getAccessServiceName());

                omrsTopicConnector.registerListener(omrsTopicListener);
            }

            this.auditLog = auditLog;

            auditCode = CommunityProfileAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(serverName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
        catch (Throwable error)
        {
            auditCode = CommunityProfileAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown()
    {
        final String               actionDescription = "shutdown";
        CommunityProfileAuditCode  auditCode;

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditCode = CommunityProfileAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(serverName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}
