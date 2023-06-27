/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import org.odpi.openmetadata.accessservices.dataengine.connectors.intopic.DataEngineInTopicClientProvider;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineAuditCode;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.server.listeners.DataEngineInTopicListener;
import org.odpi.openmetadata.accessservices.dataengine.server.processors.DataEngineEventProcessor;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DataEngineAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Data Engine OMAS.  The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 */
public class DataEngineAdmin extends AccessServiceAdmin {

    private AuditLog auditLog;
    private DataEngineServicesInstance instance;
    private String serverName;

    private static final Logger log = LoggerFactory.getLogger(DataEngineAdmin.class);

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig          - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector - connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector          - connector for querying the cohort repositories
     * @param auditLog                     - audit log component for logging messages.
     * @param serverUserName               - user id to use on OMRS calls where there is no end user.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfig, OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector repositoryConnector, AuditLog auditLog, String serverUserName) throws
                                                                                                                  OMAGConfigurationErrorException {
        final String actionDescription = "initialize";


        auditLog.logMessage(actionDescription, DataEngineAuditCode.SERVICE_INITIALIZING.getMessageDefinition());
        try {
            this.auditLog = auditLog;

            List<String> supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                    accessServiceConfig.getAccessServiceName(), auditLog);
            List<String> defaultZones = this.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                    accessServiceConfig.getAccessServiceName(), auditLog);

            instance = new DataEngineServicesInstance(repositoryConnector, supportedZones, defaultZones, auditLog, serverUserName,
                    repositoryConnector.getMaxPageSize(), accessServiceConfig.getAccessServiceInTopic());

            serverName = instance.getServerName();

            if (accessServiceConfig.getAccessServiceInTopic() != null) {
                DataEngineEventProcessor dataEngineEventProcessor = new DataEngineEventProcessor(instance, auditLog);
                DataEngineInTopicListener dataEngineInTopicListener = new DataEngineInTopicListener(auditLog, dataEngineEventProcessor);

                OpenMetadataTopicConnector dataEngineInTopicConnector = initializeDataEngineTopicConnector(
                        accessServiceConfig.getAccessServiceInTopic()); //TODO: Check if this should be moved / done form AccessServicesAdmin (?)
                if (dataEngineInTopicConnector != null) {
                    dataEngineInTopicConnector.registerListener(dataEngineInTopicListener);
                    dataEngineInTopicConnector.start();
                }
            }

            auditLog.logMessage(actionDescription, DataEngineAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));
        } catch (OMAGConfigurationErrorException e) {
            throw e;
        } catch (Exception error) {
            auditLog.logException(actionDescription, DataEngineAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(),
                    serverName), error);

            super.throwUnexpectedInitializationException(actionDescription, AccessServiceDescription.DATA_ENGINE_OMAS.getAccessServiceFullName(),
                    error);
        }
    }

    /**
     * Shutdown the access service.
     */
    @Override
    public void shutdown() {

        if (instance != null) {
            instance.shutdown();
        }

        if (auditLog != null) {
            final String actionDescription = "shutdown";

            auditLog.logMessage(actionDescription, DataEngineAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
        }
    }

    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection properties of the topic connection
     *
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection) throws
                                                                                     OMAGConfigurationErrorException {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);

            topicConnector.setAuditLog(auditLog.createNewAuditLog(OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR));

            return topicConnector;
        } catch (Exception error) {
            String methodName = "getTopicConnector";

            OMAGConfigurationErrorException e = new OMAGConfigurationErrorException(DataEngineErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(),
                    this.getClass().getName(), methodName);
            log.error("Exception in returning the topic connector for Data Engine: ", error);

            throw e;
        }
    }

    /**
     * Returns the topic created based on connection properties
     *
     * @param topicConnection properties of the topic
     *
     * @return the topic created based on the connection properties
     */
    private OpenMetadataTopicConnector initializeDataEngineTopicConnector(Connection topicConnection) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";
        if (topicConnection != null) {
            try {
                return getTopicConnector(topicConnection);
            } catch (Exception e) {
                auditLog.logException(actionDescription, DataEngineAuditCode.ERROR_INITIALIZING_TOPIC_CONNECTION.getMessageDefinition(e.getMessage(),
                        serverName), e);
                throw e;
            }

        }
        return null;
    }
}
