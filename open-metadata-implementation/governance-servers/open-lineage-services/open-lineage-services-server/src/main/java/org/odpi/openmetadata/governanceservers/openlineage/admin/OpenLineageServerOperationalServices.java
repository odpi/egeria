/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.openlineage.auditlog.OpenLineageServerAuditCode;
import org.odpi.openmetadata.governanceservers.openlineage.buffergraph.BufferGraph;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;
import org.odpi.openmetadata.governanceservers.openlineage.listeners.InTopicListener;
import org.odpi.openmetadata.governanceservers.openlineage.maingraph.MainGraph;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageServerInstance;
import org.odpi.openmetadata.governanceservers.openlineage.services.StoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode.*;


/**
 * OpenLineageOperationalServices is responsible for controlling the startup and shutdown of
 * of the open lineage services.
 */
public class OpenLineageServerOperationalServices {
    private static final Logger log = LoggerFactory.getLogger(OpenLineageServerOperationalServices.class);

    private String localServerName;
    private String localServerUserId;
    private String localServerPassword;
    private int maxPageSize;

    private OpenLineageServerConfig openLineageServerConfig;
    private OpenLineageServerInstance openLineageServerInstance = null;
    private OMRSAuditLog auditLog = null;
    private OpenMetadataTopicConnector inTopicConnector;


    /**
     * Constructor used at server startup.
     *
     * @param localServerName     name of the local server
     * @param localServerUserId   user id for this server to use if sending REST requests and
     *                            processing inbound messages.
     * @param localServerPassword password for this server to use if sending REST requests.
     * @param maxPageSize         maximum number of records that can be requested on the pageSize parameter
     */
    public OpenLineageServerOperationalServices(String localServerName,
                                                String localServerUserId,
                                                String localServerPassword,
                                                int maxPageSize) {
        this.localServerName = localServerName;
        this.localServerUserId = localServerUserId;
        this.localServerPassword = localServerPassword;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Initialize the service.
     *
     * @param openLineageServerConfig config properties
     * @param auditLog                destination for audit log messages.
     */
    public void initialize(OpenLineageServerConfig openLineageServerConfig, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        final String methodName = "initialize";
        final String actionDescription = "Initialize Open lineage Services";
        this.openLineageServerConfig = openLineageServerConfig;
        this.auditLog = auditLog;

        logRecordToAudit(OpenLineageServerAuditCode.SERVER_INITIALIZING, actionDescription);

        if (openLineageServerConfig == null) {
            throwError(OpenLineageServerErrorCode.NO_CONFIG_DOC, methodName, OpenLineageServerAuditCode.NO_CONFIG_DOC, actionDescription);
        }

        Connection bufferGraphConnection = openLineageServerConfig.getOpenLineageBufferGraphConnection();
        Connection mainGraphConnection = openLineageServerConfig.getOpenLineageMainGraphConnection();

        BufferGraph bufferGraphConnector = getBufferGraphConnector(bufferGraphConnection);
        MainGraph mainGraphConnector = getMainGraphConnector(mainGraphConnection);

        initiateAndStartDBConnectors(bufferGraphConnector, mainGraphConnector);

        StoringServices storingServices = new StoringServices(bufferGraphConnector);
        OpenLineageHandler openLineageHandler = new OpenLineageHandler(mainGraphConnector);

        this.openLineageServerInstance = new
                OpenLineageServerInstance(
                localServerName,
                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                maxPageSize,
                openLineageHandler);
        startEventBus(storingServices);

    }

    /**
     * Use the connectorbroker to obtain a Buffergraph connector.
     *
     * @param bufferGraphConnection the Buffergraph connection as provided by the user in the configure open lineage services postman call.
     * @return The Buffergraph connector
     * @throws OMAGConfigurationErrorException
     */
    private BufferGraph getBufferGraphConnector(Connection bufferGraphConnection) throws OMAGConfigurationErrorException {
        /*
         * Configuring the Graph connectors
         */
        final String actionDescription = "Get Buffergraph connector";
        BufferGraph bufferGraph = null;
        try {
            bufferGraph = (BufferGraph) new ConnectorBroker().getConnector(bufferGraphConnection);
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            log.error("Unable to initialize the graph connector.", e);
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_OBTAINING_BUFFER_GRAPH_CONNNECTOR, actionDescription);
        }
        return bufferGraph;
    }

    /**
     * Use the connectorbroker to obtain a Maingraph connector.
     *
     * @param mainGraphConnection the Buffergraph connection as provided by the user in the configure open lineage services postman call.
     * @return The Maingraph connector
     * @throws OMAGConfigurationErrorException
     */
    private MainGraph getMainGraphConnector(Connection mainGraphConnection) throws OMAGConfigurationErrorException {
        /*
         * Configuring the Graph connectors
         */
        final String actionDescription = "Get Maingraph connector";
        MainGraph mainGraph = null;
        try {
            mainGraph = (MainGraph) new ConnectorBroker().getConnector(mainGraphConnection);
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            log.error("Unable to initialize the graph connector.", e);
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_OBTAINING_MAIN_GRAPH_CONNNECTOR, actionDescription);
        }
        return mainGraph;
    }

    private void initiateAndStartDBConnectors(BufferGraph bufferGraphConnector, MainGraph mainGraphConnector) throws OMAGConfigurationErrorException {
        String methodName = "initiateAndStartDBConnections";
        String actionDescription = "initiateBufferGraphConnector";
        try {
            bufferGraphConnector.initializeGraphDB();
        } catch (OpenLineageException e) {
            log.error("The Buffergraph database connector could not be initialized", e);
            throwError(ERROR_INITIALIZING_BUFFER_GRAPH_CONNECTOR, methodName, OpenLineageServerAuditCode.ERROR_INITIALIZING_BUFFER_GRAPH_CONNNECTOR, actionDescription);
        }
        actionDescription = "initiateMainGraphConnector";
        try {
            mainGraphConnector.initializeGraphDB();
        } catch (OpenLineageException e) {
            log.error("The Maingraph database connector could not be initialized", e);
            throwError(ERROR_INITIALIZING_MAIN_GRAPH_CONNECTOR, methodName, OpenLineageServerAuditCode.ERROR_INITIALIZING_MAIN_GRAPH_CONNECTOR, actionDescription);
        }

        Object mainGraph = mainGraphConnector.getMainGraph();
        bufferGraphConnector.setMainGraph(mainGraph);
        actionDescription = "startBufferGraphConnector";

        try {
            bufferGraphConnector.start();
        } catch (ConnectorCheckedException e) {
            log.error("Could not start the buffer graph connector.", e);
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_STARTING_BUFFER_GRAPH_CONNECTOR, actionDescription);
        }
        actionDescription = "startMainGraphConnector";
        try {
            mainGraphConnector.start();
        } catch (ConnectorCheckedException e) {
            log.error("Could not start the main graph connector.", e);
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_STARTING_MAIN_GRAPH_CONNECTOR, actionDescription);
        }
    }

    /**
     * Start the kafka connector to listen to the asset lineage OMAS out topic.
     *
     * @param storingServices
     * @throws OMAGConfigurationErrorException
     */
    private void startEventBus(StoringServices storingServices) throws OMAGConfigurationErrorException {
        final String actionDescription = "Start event bus";
        final String methodName = "startEventBus";
        inTopicConnector = getTopicConnector(openLineageServerConfig.getInTopicConnection(), auditLog);

        if (inTopicConnector == null)
            throwError(OpenLineageServerErrorCode.NO_IN_TOPIC_CONNECTOR, methodName, OpenLineageServerAuditCode.ERROR_REGISTRATING_WITH_AL_OUT_TOPIC, actionDescription);

        OpenMetadataTopicListener governanceEventListener = new InTopicListener(storingServices, auditLog);
        inTopicConnector.registerListener(governanceEventListener);
        try {
            inTopicConnector.start();
        } catch (ConnectorCheckedException e) {
            log.error("The eventbus could not be started", e);
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_REGISTRATING_WITH_AL_OUT_TOPIC, actionDescription);
        }
        logRecordToAudit(OpenLineageServerAuditCode.SERVER_REGISTERED_WITH_AL_OUT_TOPIC, actionDescription);
    }


    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection properties of the topic connection
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection, OMRSAuditLog auditLog) throws
            OMAGConfigurationErrorException {
        final String actionDescription = "Get Asset Lineage OMAS out topic connector";
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);
            topicConnector.setAuditLog(auditLog);
            return topicConnector;
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            log.error("The connector for the asset lineage OMAS out topic could not be obtained", e);
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_INITIALIZING_KAFKA_CONNECTOR, actionDescription);
            return null;
        }
    }

    /**
     * Write to audit log
     *
     * @param auditCode         Reference to the specific audit message
     * @param actionDescription Describes what the user could do to prevent the error from occuring.
     */
    private void logRecordToAudit(OpenLineageServerAuditCode auditCode, String actionDescription) {
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(localServerName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }

    /**
     * Write an exception to the audit log
     *
     * @param auditCode         Reference to the specific audit message
     * @param actionDescription Describes what the user could do to prevent the error from occuring.
     */
    private void logExceptionToAudit(OpenLineageServerAuditCode auditCode, String actionDescription, Exception e) {
        auditLog.logException(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(localServerName, openLineageServerConfig.toString()),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction(),
                e);
    }

    /**
     * Throw an OMAGConfigurationErrorException using an OpenLineageServerErrorCode.
     *
     * @param errorCode         Reference to the specific error type
     * @param methodName        The name of the calling method
     * @param auditCode
     * @param actionDescription
     * @throws OMAGConfigurationErrorException
     */
    private void throwError(OpenLineageServerErrorCode errorCode, String methodName, OpenLineageServerAuditCode
            auditCode, String actionDescription) throws OMAGConfigurationErrorException {
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);
        OMAGConfigurationErrorException e = new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        logExceptionToAudit(auditCode, actionDescription, e);
        throw e;
    }

    /**
     * Convert an OCFCheckedExceptionBase exception to an OMAGConfigurationErrorException
     *
     * @param error The error to be mapped
     * @throws OMAGConfigurationErrorException
     */
    private void OCFCheckedExceptionToOMAGConfigurationError(OCFCheckedExceptionBase
                                                                     error, OpenLineageServerAuditCode auditCode, String actionDescription) throws OMAGConfigurationErrorException {
        OMAGConfigurationErrorException e = new OMAGConfigurationErrorException(error.getReportedHTTPCode(),
                error.getReportingClassName(),
                error.getReportingActionDescription(),
                error.getErrorMessage(),
                error.getReportedSystemAction(),
                error.getReportedUserAction());
        logExceptionToAudit(auditCode, actionDescription, e);
        throw e;
    }

    /**
     * Shutdown the Open Lineage Services.
     *
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean shutdown() {
        String actionDescription = "Server shutting down";
        logRecordToAudit(OpenLineageServerAuditCode.SERVER_SHUTTING_DOWN, actionDescription);

        try {
            inTopicConnector.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("The Asset Lineage OMAS out topic connector could not be disconnected", e);
            return false;
        }
        if (openLineageServerInstance != null) {
            openLineageServerInstance.shutdown();
        }

        logRecordToAudit(OpenLineageServerAuditCode.SERVER_SHUTDOWN, actionDescription);
        return true;
    }
}

