/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin;

import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.SecurityOfficerAuditCode;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.SecurityOfficerErrorCode;
import org.odpi.openmetadata.accessservices.securityofficer.server.listener.SecurityOfficerOMRSTopicListener;
import org.odpi.openmetadata.accessservices.securityofficer.server.processors.SecurityOfficerEventProcessor;
import org.odpi.openmetadata.accessservices.securityofficer.server.publisher.SecurityOfficerPublisher;
import org.odpi.openmetadata.accessservices.securityofficer.server.services.SecurityOfficerInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;

import java.util.List;

public class SecurityOfficerAdmin extends AccessServiceAdmin
{

    private AuditLog auditLog;
    private String serverName;
    private SecurityOfficerInstance instance;
    private SecurityOfficerPublisher securityOfficerPublisher;

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         - connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector    - connector for querying the cohort repositories
     * @param auditLog                             - audit log component for logging messages.
     * @param serverUserName                       - user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public synchronized void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                                        OMRSTopicConnector enterpriseOMRSTopicConnector,
                                        OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                                        AuditLog auditLog,
                                        String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, SecurityOfficerAuditCode.SERVICE_INITIALIZING.getMessageDefinition());


        try
        {
            this.auditLog = auditLog;
            String accessServiceName = accessServiceConfigurationProperties.getAccessServiceName();
            List<String> supportedZones = super.extractSupportedZones(accessServiceConfigurationProperties.getAccessServiceOptions(),
                    accessServiceName, auditLog);
            OpenMetadataTopicConnector securityOfficerOutputTopic = initializeSecurityOfficerTopicConnector(
                    accessServiceConfigurationProperties.getAccessServiceOutTopic());
            SecurityOfficerEventProcessor securityOfficerEventProcessor = new SecurityOfficerEventProcessor(enterpriseOMRSRepositoryConnector,
                                                                                                            accessServiceName);

            OMRSRepositoryHelper repositoryHelper = enterpriseOMRSRepositoryConnector.getRepositoryHelper();
            securityOfficerPublisher = new SecurityOfficerPublisher(securityOfficerEventProcessor, securityOfficerOutputTopic,
                                                                    repositoryHelper, accessServiceName, auditLog);
            this.instance = new SecurityOfficerInstance(enterpriseOMRSRepositoryConnector, supportedZones, auditLog, serverUserName,
                                                                enterpriseOMRSRepositoryConnector.getMaxPageSize(),
                                                                accessServiceConfigurationProperties.getAccessServiceOutTopic(),
                                                                securityOfficerPublisher);
            this.serverName = instance.getServerName();
            this.registerWithEnterpriseTopic(AccessServiceDescription.SECURITY_OFFICER_OMAS.getAccessServiceFullName(),
                                             serverName,
                                             enterpriseOMRSTopicConnector,
                                             new SecurityOfficerOMRSTopicListener(
                                                     securityOfficerPublisher,
                                                     repositoryHelper,
                                                     enterpriseOMRSRepositoryConnector.getRepositoryValidator(),
                                                     AccessServiceDescription.SECURITY_OFFICER_OMAS.getAccessServiceFullName(),
                                                     serverName,
                                                     serverUserName,
                                                     supportedZones,
                                                     auditLog),
                                             auditLog);

            auditLog.logMessage(actionDescription, SecurityOfficerAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));

        } catch (OMAGConfigurationErrorException error) {
            auditLog.logException(actionDescription, SecurityOfficerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfigurationProperties.toString(), error);
            throw error;
        } catch (Exception error) {
            auditLog.logException(actionDescription, SecurityOfficerAuditCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                         error.getMessage()), error);

            throw new OMAGConfigurationErrorException(
                        SecurityOfficerErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                           AccessServiceDescription.SECURITY_OFFICER_OMAS.getAccessServiceFullName(),
                                                                                                           serverName, error.getMessage()),
                        this.getClass().getName(), actionDescription, error);
        }
    }


    /**
     * Returns the topic created based on connection properties
     *
     * @param topicConnection properties of the topic
     * @return the topic created based on the connection properties
     */
    private OpenMetadataTopicConnector initializeSecurityOfficerTopicConnector(Connection topicConnection) {
        final String actionDescription = "initialize";
        if (topicConnection != null) {
            try {
                return getTopicConnector(topicConnection);
            } catch (Exception e) {
                auditLog.logException(actionDescription,
                                      SecurityOfficerAuditCode.ERROR_INITIALIZING_TOPIC_CONNECTION.getMessageDefinition(topicConnection.toString(), serverName, e.getMessage()), e);
                throw e;
            }

        }
        return null;
    }

    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
            Connector connector = connectorBroker.getConnector(topicConnection);
            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connector;

            topicConnector.start();

            return topicConnector;
        } catch (Exception error) {
            final String methodName = "getTopicConnector";

            throw new OMRSConfigErrorException(SecurityOfficerErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(methodName),
                    this.getClass().getName(),
                    methodName,
                    error);

        }
    }


    /**
     * Shutdown the access service.
     */
    @Override
    public synchronized void shutdown() {
        final String actionDescription = "shutdown";

        if (this.instance != null)
        {
            this.instance.shutdown();
        }

        if (this.securityOfficerPublisher != null)
        {
            this.securityOfficerPublisher.disconnect();
        }

        auditLog.logMessage(actionDescription, SecurityOfficerAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));

    }
}
