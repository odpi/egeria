/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.virtualizationservices.admin;

import org.odpi.openmetadata.accessservices.informationview.events.EndpointSource;
import org.odpi.openmetadata.adminservices.configuration.properties.VirtualizationConfig;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.virtualizationservices.auditlog.VirtualizationAuditCode;
import org.odpi.openmetadata.governanceservers.virtualizationservices.event.VirtualizerTopicListener;
import org.odpi.openmetadata.governanceservers.virtualizationservices.ffdc.VirtualizationErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.derby.ViewGeneratorDerbyConnector;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.derby.ViewGeneratorDerbyConnectorProvider;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VirtualizationOperationalServices {

    private static final Logger log = LoggerFactory.getLogger(VirtualizationOperationalServices.class);

    private String localServerName;               /* Initialized in constructor */
    private String localServerType;               /* Initialized in constructor */
    private String localMetadataCollectionName;   /* Initialized in constructor */
    private String localOrganizationName;         /* Initialized in constructor */
    private String localServerUserId;             /* Initialized in constructor */
    private String localServerURL;                /* Initialized in constructor */

    private OMRSAuditLog auditLog;
    private OpenMetadataTopicConnector virtualizerInboundTopicConnector;
    private OpenMetadataTopicConnector virtualizerOutboundTopicConnector;
    private ViewGeneratorDerbyConnector virtualizationSolutionConnector;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName       name of the local server
     * @param localServerType       type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param localServerUserId     user id for this server to use if processing inbound messages.
     * @param localServerURL        URL root for this server.
     */
    public VirtualizationOperationalServices(String localServerName,
                                             String localServerType,
                                             String localOrganizationName,
                                             String localServerUserId,
                                             String localServerURL) {
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localServerUserId = localServerUserId;
        this.localServerURL = localServerURL;
    }

    /**
     * Initialize the virtualization server
     *
     * @param virtualizationConfig Virtualization server configuration.
     * @param auditLog             Audition Log instance.
     */
    public void initialize(VirtualizationConfig virtualizationConfig, OMRSAuditLog auditLog) {

        if (virtualizationConfig != null) {
            final String actionDescription = "initialize";
            VirtualizationAuditCode auditCode = VirtualizationAuditCode.SERVICE_INITIALIZING;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());

            this.auditLog = auditLog;


            /*
             Configuring the kafka connector for IV Out Topic
             */
            Connection virtualizerInboundTopicConnection = virtualizationConfig.getVirtualizerInboundTopic();

            if (virtualizerInboundTopicConnection != null) {
                try {
                    virtualizerInboundTopicConnector = getTopicConnector(virtualizerInboundTopicConnection);
                } catch (Exception e) {
                    auditCode = VirtualizationAuditCode.ERROR_INITIALIZING_IV_OUT_TOPIC_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            /*
             Configuring the Kafka connector for IV In Topic
             */
            Connection ivInTopicConnection = virtualizationConfig.getVirtualizerOutboundTopic();

            if (ivInTopicConnection != null) {
                try {
                    virtualizerOutboundTopicConnector = getTopicConnector(ivInTopicConnection);
                } catch (Exception e) {
                    auditCode = VirtualizationAuditCode.ERROR_INITIALIZING_IV_IN_TOPIC_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            /*
             * Configuring the derby connector
             */
            Connection virtualizationSolutionConnection = virtualizationConfig.getVirtualizationSolutionConnection();
            if (virtualizationSolutionConnection != null) {
                try {
                    ViewGeneratorDerbyConnectorProvider virtualizationConnectorProvider = new ViewGeneratorDerbyConnectorProvider();
                    virtualizationSolutionConnector = (ViewGeneratorDerbyConnector) virtualizationConnectorProvider.getConnector(virtualizationSolutionConnection);
                } catch (Exception e) {
                    log.error("Error creating derby connector: ", e);
                }
            }


            /*
             Starting the In Topic Connector
             */
            if (virtualizerOutboundTopicConnector != null) {
                try {
                    virtualizerOutboundTopicConnector.start();

                    EndpointSource endpointSource = new EndpointSource();
                    String connectorProviderName = virtualizationSolutionConnection.getEndpoint().getAdditionalProperties().get("connectorProviderName");
                    int lastIndexOf = connectorProviderName.lastIndexOf(".");
                    endpointSource.setConnectorProviderName(connectorProviderName.substring(lastIndexOf + 1, connectorProviderName.length()));
                    String aggregatedAddress = virtualizationSolutionConnection.getEndpoint().getAddress();
                    endpointSource.setProtocol(aggregatedAddress.split("//")[0] + "//");
                    endpointSource.setNetworkAddress(aggregatedAddress.split("//")[1].split(":")[0]);
                    endpointSource.setUser(virtualizationSolutionConnection.getUserId());
                     /*
                     Binding the In Topic connector to the Topic Listener for generating In Topic
                     */
                    VirtualizerTopicListener virtualizerTopicListener = new VirtualizerTopicListener(virtualizerOutboundTopicConnector,
                            virtualizationSolutionConnector,
                            endpointSource,
                            virtualizationSolutionConnection.getAdditionalProperties().get("databaseName"),
                            virtualizationSolutionConnection.getAdditionalProperties().get("dataSchema"));
                    virtualizerInboundTopicConnector.registerListener(virtualizerTopicListener);

                } catch (Exception e) {
                    auditCode = VirtualizationAuditCode.ERROR_INITIALIZING_IV_IN_TOPIC_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            /*
             Starting the Out Topic Connector
             */
            if (virtualizerInboundTopicConnector != null) {
                try {
                    virtualizerInboundTopicConnector.start();
                    auditCode = VirtualizationAuditCode.INBOUND_TOPIC_CONNECTOR_INITIALIZED;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                } catch (Exception e) {
                    auditCode = VirtualizationAuditCode.ERROR_INITIALIZING_IV_OUT_TOPIC_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            if (virtualizationSolutionConnector != null) {
                try {
                    virtualizationSolutionConnector.start();
                } catch (Exception e) {
                    log.error("Error in starting the derby connector: ", e);
                }
            }

        }

        if ((virtualizerInboundTopicConnector != null) && (virtualizerOutboundTopicConnector != null) && (virtualizationSolutionConnector != null) &&
                (virtualizerOutboundTopicConnector.isActive()) && (virtualizerInboundTopicConnector.isActive()) && (virtualizationSolutionConnector.isActive())) {
            VirtualizationAuditCode auditCode = VirtualizationAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord("Initializing",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }

        log.info("Virtualizer has been started!");

    }

    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection properties of the topic connection
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);

            topicConnector.setAuditLog(auditLog.createNewAuditLog(OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR));

            return topicConnector;
        } catch (Throwable error) {
            final String methodName = "getTopicConnector";

            if (log.isDebugEnabled()) {
                log.debug("Unable to create topic connector: " + error.toString());
            }

            throw new OMRSConfigErrorException(VirtualizationErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(methodName),
                    this.getClass().getName(),
                    methodName,
                    error);
        }
    }


    /**
     * Shutdown the Virtualization Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {
        VirtualizationAuditCode auditCode;
        try {
            virtualizerOutboundTopicConnector.disconnect();
            virtualizerInboundTopicConnector.disconnect();
            virtualizationSolutionConnector.disconnect();
            auditCode = VirtualizationAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return true;
        } catch (Exception e) {
            auditCode = VirtualizationAuditCode.ERROR_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return false;
        }
    }

}
