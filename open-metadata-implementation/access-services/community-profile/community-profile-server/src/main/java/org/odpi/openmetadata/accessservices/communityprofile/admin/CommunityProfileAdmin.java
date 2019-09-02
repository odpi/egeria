/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.admin;

import org.odpi.openmetadata.accessservices.communityprofile.auditlog.CommunityProfileAuditCode;
import org.odpi.openmetadata.accessservices.communityprofile.intopic.CommunityProfileInTopicProcessor;
import org.odpi.openmetadata.accessservices.communityprofile.omrstopic.CommunityProfileOMRSTopicProcessor;
import org.odpi.openmetadata.accessservices.communityprofile.server.CommunityProfileServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class CommunityProfileAdmin extends AccessServiceAdmin
{
    private OMRSRepositoryConnector            repositoryConnector = null;
    private OMRSTopicConnector                 omrsTopicConnector  = null;
    private AccessServiceConfig                accessServiceConfig = null;
    private OMRSAuditLog                       auditLog            = null;
    private CommunityProfileServicesInstance   instance            = null;
    private String                             serverName          = null;
    private String                             serverUserName      = null;
    private CommunityProfileOMRSTopicProcessor omrsTopicProcessor  = null;
    private CommunityProfileInTopicProcessor   inTopicProcessor    = null;


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
            this.accessServiceConfig = accessServiceConfigurationProperties;
            this.repositoryConnector = enterpriseOMRSRepositoryConnector;
            this.instance = new CommunityProfileServicesInstance(repositoryConnector,
                                                                 this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                                            accessServiceConfig.getAccessServiceName(),
                                                                                            auditLog),
                                                                 auditLog,
                                                                 serverUserName,
                                                                 repositoryConnector.getMaxPageSize());
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

                OpenMetadataTopicConnector outTopicConnector = null;

                if (accessServiceConfig.getAccessServiceOutTopic() != null)
                {
                    outTopicConnector = super.getOutTopicConnector(accessServiceConfig.getAccessServiceOutTopic(),
                                                                   accessServiceConfig.getAccessServiceName(),
                                                                   auditLog);
                }

                omrsTopicProcessor = new CommunityProfileOMRSTopicProcessor(outTopicConnector,
                                                                            super.extractKarmaPointPlateau(accessServiceConfig.getAccessServiceOptions(),
                                                                                                           accessServiceConfig.getAccessServiceName(),
                                                                                                           auditLog),
                                                                            super.extractKarmaPointIncrement(accessServiceConfig.getAccessServiceOptions(),
                                                                                                             accessServiceConfig.getAccessServiceName(),
                                                                                                             auditLog),
                                                                            accessServiceConfig.getAccessServiceName(),
                                                                            serverUserName,
                                                                            auditLog.createNewAuditLog(OMRSAuditingComponent.ENTERPRISE_TOPIC_LISTENER),
                                                                            instance);

                omrsTopicConnector.registerListener(omrsTopicProcessor);
            }


            if (accessServiceConfig.getAccessServiceInTopic() != null)
            {
                inTopicProcessor = new CommunityProfileInTopicProcessor(super.getInTopicConnector(accessServiceConfig.getAccessServiceInTopic(),
                                                                                                  accessServiceConfig.getAccessServiceName(),
                                                                                                  auditLog),
                                                                        instance);
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
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
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
        final String              actionDescription = "shutdown";
        CommunityProfileAuditCode auditCode;

        if (inTopicProcessor != null)
        {
            inTopicProcessor.shutdown();
        }

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
