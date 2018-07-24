/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.admin;


import org.odpi.openmetadata.accessservices.informationview.auditlog.InformationViewAuditCode;
import org.odpi.openmetadata.accessservices.informationview.connectors.InformationViewTopic;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.ColumnContextEventBuilder;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.RepositoryHelper;
import org.odpi.openmetadata.accessservices.informationview.eventprocessor.EventPublisher;
import org.odpi.openmetadata.accessservices.informationview.listeners.InformationViewEnterpriseOmrsEventListener;
import org.odpi.openmetadata.accessservices.informationview.listeners.InformationViewInTopicListener;
import org.odpi.openmetadata.accessservices.informationview.listeners.InformationViewListener;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class InformationViewAdmin implements AccessServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(InformationViewAdmin.class);
    private EventPublisher eventPublisher;
    private OMRSRepositoryConnector enterpriseConnector;
    private InformationViewTopic informationViewInTopicConnector;
    private InformationViewTopic informationViewOutTopicConnector;
    private OMRSTopicConnector enterpriseOMRSTopicConnector = null;
    private InformationViewListener informationViewInTopicListener;
    private EntitiesCreatorHelper entitiesCreatorHelper;
    private ColumnContextEventBuilder columnContextEventBuilder;
    private InformationViewEnterpriseOmrsEventListener informationViewEnterpriseOmrsEventListener;
    private OMRSAuditLog auditLog;


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
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties, OMRSTopicConnector enterpriseOMRSTopicConnector, OMRSRepositoryConnector enterpriseOMRSRepositoryConnector, OMRSAuditLog auditLog, String serverUserName) {
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

        informationViewInTopicConnector = initializeInformationViewTopicConnector(accessServiceConfigurationProperties.getAccessServiceInTopic());
        informationViewOutTopicConnector = initializeInformationViewTopicConnector(accessServiceConfigurationProperties.getAccessServiceOutTopic());

        RepositoryHelper helper = new RepositoryHelper(this.enterpriseConnector);
        entitiesCreatorHelper = new EntitiesCreatorHelper(helper,
                                                          this.enterpriseConnector,
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
            eventPublisher = new EventPublisher(informationViewOutTopicConnector, columnContextEventBuilder, auditLog);
            informationViewEnterpriseOmrsEventListener = new InformationViewEnterpriseOmrsEventListener(eventPublisher, auditLog);
            this.enterpriseOMRSTopicConnector.registerListener(informationViewEnterpriseOmrsEventListener);
        }


        if (informationViewInTopicConnector != null) {
            auditCode = InformationViewAuditCode.SERVICE_REGISTERED_WITH_IV_IN_TOPIC;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            informationViewInTopicListener = new InformationViewInTopicListener(entitiesCreatorHelper, auditLog);
            this.informationViewInTopicConnector.registerListener(informationViewInTopicListener);
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
     * @param topicConnection properties of the topic
     * @return the topic created based on the connection properties
     */
    private InformationViewTopic initializeInformationViewTopicConnector(Connection topicConnection) {

        if (topicConnection != null) {
            Map<String, Object> properties = new HashMap<>();
            String topicName = topicConnection.getEndpoint().getAddress();
            properties.put(Constants.TOPIC_NAME, topicName);
            topicConnection.setAdditionalProperties(properties);
            if (topicConnection != null) {
                return (InformationViewTopic) getTopicConnector(
                        topicConnection);
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
    private Connector getTopicConnector(Connection topicConnection) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            return connectorBroker.getConnector(topicConnection);
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
