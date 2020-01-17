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
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraph;
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
        final String actionDescription = "Initialize Open lineage Services";
        final String methodName = "OpenLineageServerOperationalServices.initialize";
        this.openLineageServerConfig = openLineageServerConfig;

        OpenLineageServerAuditCode auditCode;

        this.auditLog = auditLog;

        auditCode = OpenLineageServerAuditCode.SERVER_INITIALIZING;
        logRecordToAudit(auditCode, actionDescription);

        if (openLineageServerConfig == null) {
            throwError(OpenLineageServerErrorCode.NO_CONFIG_DOC, methodName, OpenLineageServerAuditCode.NO_CONFIG_DOC, actionDescription);
        }

        Connection bufferGraphConnection = openLineageServerConfig.getOpenLineageBufferGraphConnection();
        Connection mainGraphConnection = openLineageServerConfig.getOpenLineageMainGraphConnection();

        BufferGraph bufferGraphConnector = (BufferGraph) getGraphConnector(bufferGraphConnection);
        MainGraph mainGraphConnector = (MainGraph) getGraphConnector(mainGraphConnection);

        try {
            bufferGraphConnector.initializeGraphDB();
            mainGraphConnector.initializeGraphDB();
        } catch (OpenLineageException e) {
            auditCode = OpenLineageServerAuditCode.CANNOT_OPEN_GRAPH_DB;
            auditLog.logException(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName, openLineageServerConfig.toString()),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw new OMAGConfigurationErrorException(e.getReportedHTTPCode(),
                    e.getReportingClassName(),
                    e.getReportingActionDescription(),
                    e.getErrorMessage(),
                    e.getReportedSystemAction(),
                    e.getReportedUserAction());
        }
        Object mainGraph = mainGraphConnector.getMainGraph();
        bufferGraphConnector.setMainGraph(mainGraph);

        try {
            bufferGraphConnector.start();
        } catch (ConnectorCheckedException e) {
            log.error("Could not start the buffer graph connector.");
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_INITIALIZING_CONNECTOR, actionDescription);
        }
        try {
            mainGraphConnector.start();
        } catch (ConnectorCheckedException e) {
            log.error("Could not start the main graph connector.");
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_INITIALIZING_CONNECTOR, actionDescription);
        }

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
     * Use the connectorbroker to obtain a connector based on an connection object.
     *
     * @param connection the connection as provided by the user in the configure open lineage services postman call.
     * @return The connector
     * @throws OMAGConfigurationErrorException
     */
    private OpenLineageGraph getGraphConnector(Connection connection) throws OMAGConfigurationErrorException {
        /*
         * Configuring the Graph connectors
         */
        final String actionDescription = "Get Open Lineage graph connector";
        OpenLineageGraph openLineageGraph = null;

        log.info("Found connection: {}", connection);
        try {
            openLineageGraph = (OpenLineageGraph) new ConnectorBroker().getConnector(connection);
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            log.error("Unable to initialize graph connector.", e);
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_INITIALIZING_GRAPH_CONNECTOR, actionDescription);
        }
        return openLineageGraph;
    }

    /**
     * Start the kafka connector to listen to the asset lineage OMAS out topic.
     *
     * @param storingServices
     * @throws OMAGConfigurationErrorException
     */
    private void startEventBus(StoringServices storingServices) throws OMAGConfigurationErrorException {
        final String actionDescription = "Start event bus";
        final String methodName = "OpenLineageServerOperationalServices.startEventBus";
        inTopicConnector = getTopicConnector(openLineageServerConfig.getInTopicConnection(), auditLog);

        if (inTopicConnector == null)
            throwError(OpenLineageServerErrorCode.NO_IN_TOPIC_CONNECTOR, methodName, OpenLineageServerAuditCode.ERROR_REGISTRATING_WITH_AL_OUT_TOPIC, actionDescription);

        OpenMetadataTopicListener governanceEventListener = new InTopicListener(storingServices, auditLog);
        inTopicConnector.registerListener(governanceEventListener);
        try {
            inTopicConnector.start();
        } catch (ConnectorCheckedException e) {
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
        final String actionDescription = "getALOmasConnector";
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);
            topicConnector.setAuditLog(auditLog);
            return topicConnector;
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            OCFCheckedExceptionToOMAGConfigurationError(e, OpenLineageServerAuditCode.ERROR_INITIALIZING_CONNECTOR, actionDescription);
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
                auditCode.getFormattedLogMessage(localServerName),
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
            log.error("Error disconnecting Open lineages Services In Topic Connector");
            return false;
        }
        if (openLineageServerInstance != null) {
            openLineageServerInstance.shutdown();
        }

        logRecordToAudit(OpenLineageServerAuditCode.SERVER_SHUTDOWN, actionDescription);
        return true;
    }
}

