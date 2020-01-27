/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphConnector;
import org.odpi.openmetadata.governanceservers.openlineage.auditlog.OpenLineageServerAuditCode;
import org.odpi.openmetadata.governanceservers.openlineage.buffergraph.BufferGraph;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;
import org.odpi.openmetadata.governanceservers.openlineage.listeners.OpenLineageInTopicListener;
import org.odpi.openmetadata.governanceservers.openlineage.maingraph.MainGraph;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageServerInstance;
import org.odpi.openmetadata.governanceservers.openlineage.services.StoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private BufferGraph bufferGraphConnector;
    private MainGraph mainGraphConnector;
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

        logRecord(OpenLineageServerAuditCode.SERVER_INITIALIZING, actionDescription);

        if (openLineageServerConfig == null)
            throwError(OpenLineageServerErrorCode.NO_CONFIG_DOC, methodName, OpenLineageServerAuditCode.NO_CONFIG_DOC, actionDescription);

        Connection bufferGraphConnection = openLineageServerConfig.getOpenLineageBufferGraphConnection();
        Connection mainGraphConnection = openLineageServerConfig.getOpenLineageMainGraphConnection();
        Connection inTopicConnection = openLineageServerConfig.getInTopicConnection();

        this.bufferGraphConnector = (BufferGraph) getGraphConnector(bufferGraphConnection, OpenLineageServerAuditCode.ERROR_OBTAINING_BUFFER_GRAPH_CONNNECTOR);
        this.mainGraphConnector = (MainGraph) getGraphConnector(mainGraphConnection, OpenLineageServerAuditCode.ERROR_OBTAINING_MAIN_GRAPH_CONNNECTOR);
        this.inTopicConnector = (OpenMetadataTopicConnector) getGraphConnector(inTopicConnection, OpenLineageServerAuditCode.ERROR_OBTAINING_IN_TOPIC_CONNECTOR);

        initializeAndStartConnectors();
        OpenLineageHandler openLineageHandler = new OpenLineageHandler(mainGraphConnector);

        this.openLineageServerInstance = new
                OpenLineageServerInstance(
                localServerName,
                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                maxPageSize,
                openLineageHandler);

        logRecord(OpenLineageServerAuditCode.SERVER_INITIALIZED, actionDescription);
    }

    /**
     * Use the ConnectorBroker to obtain a graph database connector.
     *
     * @param connection the graph connection as provided by the user in the configure Open Lineage Services postman call.
     * @param auditCode  The auditcode that should be used when the connector can not be obtained.
     * @return The connector returned by the ConnectorBroker
     * @throws OMAGConfigurationErrorException
     */
    private Connector getGraphConnector(Connection connection, OpenLineageServerAuditCode auditCode) throws OMAGConfigurationErrorException {
        String actionDescription = "Obtaining graph database connector";
        Connector connector = null;
        try {
            connector = new ConnectorBroker().getConnector(connection);
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            OCFCheckedExceptionToOMAGConfigurationError(e, auditCode, actionDescription);
        }
        return connector;
    }

    /**
     * Call the initialize() and start() method for all applicable connectors used by the Open Lineage Services.
     *
     * @throws OMAGConfigurationErrorException
     */
    private void initializeAndStartConnectors() throws OMAGConfigurationErrorException {
        initializeGraphConnectorDB(
                bufferGraphConnector,
                "initializeBufferGraphConnector",
                OpenLineageServerErrorCode.ERROR_INITIALIZING_BUFFER_GRAPH_CONNECTOR_DB,
                OpenLineageServerAuditCode.ERROR_INITIALIZING_BUFFER_GRAPH_CONNNECTOR_DB);

        initializeGraphConnectorDB(
                mainGraphConnector,
                "initializeMainGraphConnector",
                OpenLineageServerErrorCode.ERROR_INITIALIZING_MAIN_GRAPH_CONNECTOR_DB,
                OpenLineageServerAuditCode.ERROR_INITIALIZING_MAIN_GRAPH_CONNECTOR_DB);

        Object mainGraph = mainGraphConnector.getMainGraph();
        bufferGraphConnector.setMainGraph(mainGraph);

        startGraphConnector(bufferGraphConnector, "startBufferGraphConnector", OpenLineageServerAuditCode.ERROR_STARTING_BUFFER_GRAPH_CONNECTOR);
        startGraphConnector(mainGraphConnector, "startMainGraphConnector", OpenLineageServerAuditCode.ERROR_STARTING_MAIN_GRAPH_CONNECTOR);
        startIntopicConnector();
    }

    /**
     * Initialize the passed OpenLineageGraphConnector.
     *
     * @param connector         The connector that is to be initialized.
     * @param actionDescription The action taking place in this method, used in error reporting
     * @param errorCode         The potential error that could occur, in a format intended for web users.
     * @param auditCode         The potential error that could occur, in a format intended for system administrators.
     * @throws OMAGConfigurationErrorException
     */
    private void initializeGraphConnectorDB(OpenLineageGraphConnector connector, String actionDescription, OpenLineageServerErrorCode errorCode, OpenLineageServerAuditCode auditCode) throws OMAGConfigurationErrorException {
        final String methodName = "initializeGraphConnectors";
        try {
            connector.initializeGraphDB();
        } catch (OpenLineageException e) {
            throwError(errorCode, methodName, auditCode, actionDescription);
        }
    }

    /**
     * Start the passed OpenLineageGraphConnector.
     *
     * @param connector         The connector that is to be started.
     * @param actionDescription The action taking place in this method, used in error reporting.
     * @param auditCode         The potential error that could occur, in a format intended for system administrators.
     * @throws OMAGConfigurationErrorException
     */
    private void startGraphConnector(OpenLineageGraphConnector connector, String actionDescription, OpenLineageServerAuditCode auditCode) throws OMAGConfigurationErrorException {
        try {
            connector.start();
        } catch (ConnectorCheckedException e) {
            OCFCheckedExceptionToOMAGConfigurationError(e, auditCode, actionDescription);
        }
    }

    /**
     * Start the Open Lineage Services in-topic connector
     *
     * @throws OMAGConfigurationErrorException
     */
    private void startIntopicConnector() throws OMAGConfigurationErrorException {
        String actionDescription = "Start the Open Lineage Services in-topic listener";
        inTopicConnector.setAuditLog(auditLog);
        StoringServices storingServices = new StoringServices(bufferGraphConnector);
        OpenMetadataTopicListener openLineageInTopicListener = new OpenLineageInTopicListener(storingServices, auditLog);
        inTopicConnector.registerListener(openLineageInTopicListener);
        try {
            inTopicConnector.start();
        } catch (ConnectorCheckedException e) {
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_STARTING_IN_TOPIC_CONNECTOR, actionDescription);
        }
        logRecord(OpenLineageServerAuditCode.SERVER_REGISTERED_WITH_AL_OUT_TOPIC, actionDescription);
    }


    /**
     * Write a non-exception record to the audit log.
     *
     * @param auditCode         Details about the exception that occurred, in a format intended for system administrators.
     * @param actionDescription Describes what the user could do to prevent the error from occurring.
     */
    private void logRecord(OpenLineageServerAuditCode auditCode, String actionDescription) {
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(localServerName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
        log.info(auditCode.getSystemAction());
    }

    /**
     * Write an exception to the audit log.
     *
     * @param auditCode         Reference to the specific audit message.
     * @param actionDescription Describes what the user could do to prevent the error from occurring.
     * @param e                 The exception object that was thrown.
     */
    private void logException(OpenLineageServerAuditCode auditCode, String actionDescription, Exception e) {
        auditLog.logException(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(localServerName, openLineageServerConfig.toString()),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction(),
                e);
        log.error(auditCode.getSystemAction(), e);
    }


    /**
     * Throw an OMAGConfigurationErrorException using an OpenLineageServerErrorCode.
     *
     * @param errorCode         Details about the exception that occurred, in a format intended for web users.
     * @param methodName        The name of the calling method.
     * @param auditCode         Details about the exception that occurred, in a format intended for system administrators.
     * @param actionDescription The action that was taking place when the error occurred.
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
        logException(auditCode, actionDescription, e);
        throw e;
    }

    /**
     * Convert an OCFCheckedExceptionBase exception to an OMAGConfigurationErrorException
     *
     * @param exception         The exception object that was thrown
     * @param auditCode         Details about the exception that occurred, in a format intended for system administrators.
     * @param actionDescription The action that was taking place when the exception occurred.
     * @throws OMAGConfigurationErrorException
     */
    private void OCFCheckedExceptionToOMAGConfigurationError(OCFCheckedExceptionBase
                                                                     exception, OpenLineageServerAuditCode auditCode, String actionDescription) throws OMAGConfigurationErrorException {
        OMAGConfigurationErrorException e = new OMAGConfigurationErrorException(exception.getReportedHTTPCode(),
                exception.getReportingClassName(),
                exception.getReportingActionDescription(),
                exception.getErrorMessage(),
                exception.getReportedSystemAction(),
                exception.getReportedUserAction());
        logException(auditCode, actionDescription, e);
        throw e;
    }

    /**
     * Shutdown the Open Lineage Services.
     *
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean shutdown() {
        String actionDescription = "Shutting down the open lineage Services server";
        logRecord(OpenLineageServerAuditCode.SERVER_SHUTTING_DOWN, actionDescription);
        try {
            this.inTopicConnector.disconnect();
            this.bufferGraphConnector.disconnect();
            this.mainGraphConnector.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("An Open Lineage Services connector could not be disconnected", e);
            return false;
        }
        if (openLineageServerInstance != null)
            openLineageServerInstance.shutdown();

        logRecord(OpenLineageServerAuditCode.SERVER_SHUTDOWN, actionDescription);
        return true;
    }
}

