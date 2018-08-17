/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.admin;


import org.odpi.openmetadata.accessservices.informationview.auditlog.InformationViewAuditCode;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.ColumnContextEventBuilder;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.informationview.eventprocessor.EventPublisher;
import org.odpi.openmetadata.accessservices.informationview.listeners.InformationViewEnterpriseOmrsEventListener;
import org.odpi.openmetadata.accessservices.informationview.listeners.InformationViewInTopicListener;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class InformationViewAdmin implements AccessServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(InformationViewAdmin.class);
    private EventPublisher eventPublisher;
    private OMRSRepositoryConnector enterpriseConnector;
    private OpenMetadataTopicConnector informationViewTopicConnector;
    private OMRSTopicConnector enterpriseOMRSTopicConnector = null;
    private OpenMetadataTopicListener informationViewInTopicListener;
    private EntitiesCreatorHelper entitiesCreatorHelper;
    private ColumnContextEventBuilder columnContextEventBuilder;
    private InformationViewEnterpriseOmrsEventListener informationViewEnterpriseOmrsEventListener;
    private OMRSAuditLog auditLog;


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
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties, OMRSTopicConnector enterpriseOMRSTopicConnector, OMRSRepositoryConnector enterpriseOMRSRepositoryConnector, OMRSAuditLog auditLog, String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";
        InformationViewAuditCode auditCode;

        auditCode = InformationViewAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());


        this.enterpriseOMRSTopicConnector = enterpriseOMRSTopicConnector;
        this.enterpriseConnector = enterpriseOMRSRepositoryConnector;
        this.auditLog = auditLog;

        informationViewTopicConnector = initializeInformationViewTopicConnector(accessServiceConfigurationProperties.getAccessServiceInTopic(), accessServiceConfigurationProperties.getAccessServiceOutTopic());


        entitiesCreatorHelper = new EntitiesCreatorHelper(this.enterpriseConnector,
                auditLog);

        if (enterpriseOMRSTopicConnector != null) {
            auditCode = InformationViewAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            columnContextEventBuilder = new ColumnContextEventBuilder(this.enterpriseConnector);
            eventPublisher = new EventPublisher(informationViewTopicConnector, columnContextEventBuilder, auditLog);
            informationViewEnterpriseOmrsEventListener = new InformationViewEnterpriseOmrsEventListener(eventPublisher, auditLog);
            this.enterpriseOMRSTopicConnector.registerListener(informationViewEnterpriseOmrsEventListener);
        }


        if (informationViewTopicConnector != null) {
            auditCode = InformationViewAuditCode.SERVICE_REGISTERED_WITH_IV_TOPIC;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            informationViewInTopicListener = new InformationViewInTopicListener(entitiesCreatorHelper, auditLog);
            this.informationViewTopicConnector.registerListener(informationViewInTopicListener);
            try {
                informationViewTopicConnector.start();
            } catch (ConnectorCheckedException e) {
                auditCode = InformationViewAuditCode.ERROR_INITIALIZING_INFORMATION_VIEW_TOPIC_CONNECTION;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
                throw new OMAGConfigurationErrorException(400,
                        InformationViewAdmin.class.getSimpleName(),
                        actionDescription,
                        auditCode.getFormattedLogMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction()
                );
            }
        }

        auditCode = InformationViewAuditCode.SERVICE_INITIALIZED;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }


    /**
     * Returns the topic created based on connection properties
     *
     * @param accessServiceInTopic  properties of the topic
     * @param accessServiceOutTopic
     * @return the topic created based on the connection properties
     */
    private OpenMetadataTopicConnector initializeInformationViewTopicConnector(Connection accessServiceInTopic, Connection accessServiceOutTopic) {
        final String actionDescription = "initialize";
        if (accessServiceInTopic != null) {
            Map<String, Object> properties = accessServiceInTopic.getAdditionalProperties();
            ((Map) properties.get("consumer")).replace("kafka.omrs.topic.id", accessServiceInTopic.getQualifiedName());
            ((Map) properties.get("producer")).replace("kafka.omrs.topic.id", accessServiceOutTopic.getQualifiedName());
            accessServiceInTopic.setAdditionalProperties(properties);
            if (accessServiceInTopic != null) {
                try {
                    return getTopicConnector(accessServiceInTopic);
                } catch (Exception e) {
                    InformationViewAuditCode auditCode = InformationViewAuditCode.ERROR_INITIALIZING_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(accessServiceInTopic.toString()),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                    throw e;
                }

            }
        }
        return null;

    }


    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection - properties of the topic connection
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            return (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);
        } catch (Throwable error) {
            String methodName = "getTopicConnector()";

            if (log.isDebugEnabled()) {
                log.debug("Unable to create topic connector: " + error.toString());
            }

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage("getTopicConnector");

            throw new OMRSConfigErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);

        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown() {
        try {
            informationViewTopicConnector.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("Error disconnecting information view topic connector");
        }
        final String actionDescription = "shutdown";
        InformationViewAuditCode auditCode;

        auditCode = InformationViewAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}
