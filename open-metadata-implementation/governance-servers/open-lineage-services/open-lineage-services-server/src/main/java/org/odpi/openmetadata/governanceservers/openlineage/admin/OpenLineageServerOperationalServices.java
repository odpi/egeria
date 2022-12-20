/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.admin;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.AssetLineage;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventListener;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.connector.AssetLineageOutTopicClientConnector;
import org.odpi.openmetadata.adminservices.configuration.properties.OLSBackgroundJob;
import org.odpi.openmetadata.adminservices.configuration.properties.OLSSimplifiedAccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphConnector;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageQueryService;
import org.odpi.openmetadata.governanceservers.openlineage.auditlog.OpenLineageServerAuditCode;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageAssetContextHandler;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;
import org.odpi.openmetadata.governanceservers.openlineage.listeners.OpenLineageInTopicListener;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.AssetLineageUpdateJob;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.AssetLineageUpdateJobConfiguration;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.JobConfiguration;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.JobConstants;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.LineageGraphJob;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageServerInstance;
import org.odpi.openmetadata.governanceservers.openlineage.services.StoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * OpenLineageOperationalServices is responsible for controlling the startup and shutdown of
 * of the open lineage services.
 */
public class OpenLineageServerOperationalServices {
    private static final Logger log = LoggerFactory.getLogger(OpenLineageServerOperationalServices.class);

    private static final String EMPTY_STRING = "";
    private static final int RETRIEVE_OUT_TOPIC_CONNECTION_TIMEOUT = 60_000;
    private static final String KAFKA_OPEN_METADATA_TOPIC_PROVIDER = "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider";

    private final String localServerName;
    private final String localServerUserId;
    private final String localServerPassword;
    private final int maxPageSize;
    private final String localServerId;

    private OpenLineageServerConfig openLineageServerConfig;
    private OpenLineageServerInstance openLineageServerInstance;
    private OMRSAuditLog auditLog;
    private OpenLineageGraphConnector lineageGraphConnector;
    private AssetLineageOutTopicClientConnector inTopicConnector;
    private AssetLineage assetLineageClient;
    private List<JobConfiguration> backgroundJobs;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName     name of the local server
     * @param localServerUserId   user id for this server to use if sending REST requests and
     *                            processing inbound messages.
     * @param localServerPassword password for this server to use if sending REST requests.
     * @param maxPageSize         maximum number of records that can be requested on the pageSize parameter
     */
    public OpenLineageServerOperationalServices(String localServerId,
                                                String localServerName,
                                                String localServerUserId,
                                                String localServerPassword,
                                                int maxPageSize) {
        this.localServerId = localServerId;
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
        this.openLineageServerConfig = openLineageServerConfig;
        this.auditLog = auditLog;
        final String methodName = "initialize";
        final String actionDescription = "Initialize Open lineage Services";

        logRecord(OpenLineageServerAuditCode.SERVER_INITIALIZING, actionDescription);
        if (openLineageServerConfig == null) {
            throwOMAGConfigurationErrorException(OpenLineageServerErrorCode.NO_CONFIG_DOC, localServerName,methodName, OpenLineageServerAuditCode.NO_CONFIG_DOC, actionDescription);
        }
        validateAccessServiceConfig(openLineageServerConfig.getAccessServiceConfig(), methodName);
        try {
            initializeOLS(openLineageServerConfig);
        } catch (OMAGConfigurationErrorException e) {
            throw e;
        } catch (Exception e) {
            exceptionToOMAGConfigurationError(e, OpenLineageServerErrorCode.ERROR_INITIALIZING_OLS, methodName, OpenLineageServerAuditCode.ERROR_INITIALIZING_OLS, actionDescription);
        }
    }

    private void validateAccessServiceConfig(OLSSimplifiedAccessServiceConfig accessServiceConfig, String methodName) throws OMAGConfigurationErrorException {
        String actionDescription = "Verify the access service configuration";
        if (accessServiceConfig.getServerName() == null || accessServiceConfig.getServerName().isEmpty()) {
            throwOMAGConfigurationErrorException(OpenLineageServerErrorCode.BAD_ACCESS_SERVICE_CONFIG, "serverName",methodName,
                    OpenLineageServerAuditCode.BAD_ACCESS_SERVICE_CONFIG, actionDescription);
        }
        if (accessServiceConfig.getServerPlatformUrlRoot() == null || accessServiceConfig.getServerPlatformUrlRoot().isEmpty()) {
            throwOMAGConfigurationErrorException(OpenLineageServerErrorCode.BAD_ACCESS_SERVICE_CONFIG, "serverPlatformUrlRoot", methodName,
                    OpenLineageServerAuditCode.BAD_ACCESS_SERVICE_CONFIG, actionDescription);
        }
        if (accessServiceConfig.getUser() == null || accessServiceConfig.getUser().isEmpty()) {
            throwOMAGConfigurationErrorException(OpenLineageServerErrorCode.BAD_ACCESS_SERVICE_CONFIG, "user", methodName,
                    OpenLineageServerAuditCode.BAD_ACCESS_SERVICE_CONFIG, actionDescription);
        }
    }

    private void initializeOLS(OpenLineageServerConfig openLineageServerConfig) throws OMAGConfigurationErrorException, InvalidParameterException, InterruptedException {
        final String methodName = "initializeOLS";
        final String actionDescription = "Initialize Open lineage Services";
        Connection lineageGraphConnection = openLineageServerConfig.getLineageGraphConnection();

        Connection inTopicConnection = getAssetLineageOutTopicConnection(openLineageServerConfig, methodName);

        this.lineageGraphConnector = (OpenLineageGraphConnector) getConnector(lineageGraphConnection, OpenLineageServerErrorCode.ERROR_OBTAINING_LINEAGE_GRAPH_CONNECTOR,
                OpenLineageServerAuditCode.ERROR_OBTAINING_LINEAGE_GRAPH_CONNECTOR);
        this.inTopicConnector = (AssetLineageOutTopicClientConnector) getConnector(inTopicConnection, OpenLineageServerErrorCode.ERROR_OBTAINING_IN_TOPIC_CONNECTOR,
                OpenLineageServerAuditCode.ERROR_OBTAINING_IN_TOPIC_CONNECTOR);

        initializeAndStartConnectors();

        OpenLineageQueryService openLineageQueryService = lineageGraphConnector.getLineageQueryService();

        OpenLineageHandler openLineageHandler = new OpenLineageHandler(openLineageQueryService);

        initializeAndStartBackgroundJobs();

        this.openLineageServerInstance = new
                OpenLineageServerInstance(
                localServerName,
                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                maxPageSize,
                openLineageHandler);

        logRecord(OpenLineageServerAuditCode.SERVER_INITIALIZED, actionDescription);
    }

    private Connection getAssetLineageOutTopicConnection(OpenLineageServerConfig openLineageServerConfig, String methodName) throws
                                                                                                                             InvalidParameterException,
                                                                                                                             InterruptedException {
        Connection inTopicConnection = openLineageServerConfig.getInTopicConnection();
        if (inTopicConnection != null) {
            return inTopicConnection;
        }

        OCFRESTClient restClient;
        OLSSimplifiedAccessServiceConfig accessServiceConfig = openLineageServerConfig.getAccessServiceConfig();
        String serverName = accessServiceConfig.getServerName();
        String serverPlatformURLRoot = accessServiceConfig.getServerPlatformUrlRoot();
        String serverPassword = accessServiceConfig.getPassword();
        String serverUserId = accessServiceConfig.getUser();
        if (serverPassword == null) {
            restClient = new OCFRESTClient(serverName, serverPlatformURLRoot);
        } else {
            restClient = new OCFRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword, auditLog);
        }
        ConnectionResponse restResult;
        restResult = getConnection(methodName, restClient, accessServiceConfig);
        while (restResult == null) {
            Thread.sleep(RETRIEVE_OUT_TOPIC_CONNECTION_TIMEOUT);
            restResult = getConnection(methodName, restClient, accessServiceConfig);
        }
        return restResult.getConnection();
    }

    private ConnectionResponse getConnection(String methodName, OCFRESTClient restClient, OLSSimplifiedAccessServiceConfig accessServiceConfig) {
        final String actionDescription = "Retrieve topic Asset Lineage out topic connection";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/asset-lineage/users/{1}/topics/out-topic-connection/{2}";
        String serverName = accessServiceConfig.getServerName();
        String serverPlatformURLRoot = accessServiceConfig.getServerPlatformUrlRoot();
        String serverUserId = accessServiceConfig.getUser();
        ConnectionResponse restResult = null;
        try {
            restResult = restClient.callOCFConnectionGetRESTCall(methodName,
                                                                 serverPlatformURLRoot + urlTemplate,
                                                                 serverName,
                                                                 serverUserId,
                                                                 localServerId);
        } catch (InvalidParameterException | UserNotAuthorizedException | org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            logException(OpenLineageServerAuditCode.COULD_NOT_RETRIEVE_TOPIC_CONNECTOR, actionDescription, e);
        }
        return restResult;
    }

    private void initializeAndStartBackgroundJobs() {
        backgroundJobs = new ArrayList<>();

        Optional<OLSBackgroundJob> lineageGraphJob = getJob(JobConstants.LINEAGE_GRAPH_JOB);
        if (isJobEnabled(lineageGraphJob)) {
            int lineageGraphJobInterval = getJobInterval(lineageGraphJob);
            backgroundJobs.add(new JobConfiguration(lineageGraphConnector, JobConstants.LINEAGE_GRAPH_JOB, LineageGraphJob.class,
                    lineageGraphJobInterval));
        }

        Optional<OLSBackgroundJob> assetLineageUpdateJob = getJob(JobConstants.ASSET_LINEAGE_UPDATE_JOB);
        if (isJobEnabled(assetLineageUpdateJob)) {
            int assetLineageJobInterval = getJobInterval(assetLineageUpdateJob);
            String configAssetLineageLastUpdateTime = getDefaultValue(assetLineageUpdateJob);

            String assetLineageServerName = openLineageServerConfig.getAccessServiceConfig().getServerName();
            backgroundJobs.add(new AssetLineageUpdateJobConfiguration(lineageGraphConnector, JobConstants.ASSET_LINEAGE_UPDATE_JOB,
                    AssetLineageUpdateJob.class, assetLineageJobInterval, configAssetLineageLastUpdateTime, assetLineageClient,
                    assetLineageServerName, localServerUserId));
        }

        backgroundJobs.forEach(JobConfiguration::schedule);
    }

    private int getJobInterval(Optional<OLSBackgroundJob> job) {
        return job.map(OLSBackgroundJob::getJobInterval).orElse(JobConstants.DEFAULT_JOB_INTERVAL_IN_SECONDS);
    }

    private boolean isJobEnabled(Optional<OLSBackgroundJob> job) {
        return job.map(OLSBackgroundJob::isJobEnabled).orElse(Boolean.TRUE);
    }

    private String getDefaultValue(Optional<OLSBackgroundJob> job) {
        return job.map(OLSBackgroundJob::getJobDefaultValue).orElse(EMPTY_STRING);
    }

    private Optional<OLSBackgroundJob> getJob(String name) {
        return openLineageServerConfig.getBackgroundJobs()
                .stream()
                .filter(job -> name.equals(job.getJobName()))
                .findAny();
    }

    /**
     * Use the ConnectorBroker to obtain a connector.
     *
     * @param connection the  connection as provided by the user in the configure Open Lineage Services HTTP call.
     * @param errorCode  The error code that should be used when the connector can not be obtained.
     * @param auditCode  The audit code that should be used when the connector can not be obtained.
     *
     * @return The connector returned by the ConnectorBroker
     *
     * @throws OMAGConfigurationErrorException
     */
    private Connector getConnector(Connection connection, OpenLineageServerErrorCode errorCode, OpenLineageServerAuditCode auditCode) throws
                                                                                                                                      OMAGConfigurationErrorException {
        final String actionDescription = "Obtaining graph database connector";
        final String methodName = "getGraphConnector";
        Connector connector = null;
        try {
            connector = new ConnectorBroker(auditLog).getConnector(connection);
        } catch (OCFCheckedExceptionBase e) {
            OCFCheckedExceptionToOMAGConfigurationError(e, auditCode, actionDescription);
        } catch (Exception e) {
            exceptionToOMAGConfigurationError(e, errorCode, methodName, auditCode, actionDescription);
        }
        return connector;
    }

    /**
     * Call the initialize() and start() method for all applicable connectors used by the Open Lineage Services.
     *
     * @throws OMAGConfigurationErrorException
     */
    private void initializeAndStartConnectors() throws OMAGConfigurationErrorException, InvalidParameterException {
        initializeGraphConnectorDB(
                lineageGraphConnector,
                OpenLineageServerErrorCode.ERROR_INITIALIZING_LINEAGE_GRAPH_CONNECTOR_DB,
                OpenLineageServerAuditCode.ERROR_INITIALIZING_LINEAGE_GRAPH_CONNECTOR_DB,
                "initializeLineageGraphConnector"
        );

        startGraphConnector(lineageGraphConnector,
                OpenLineageServerErrorCode.ERROR_STARTING_LINEAGE_GRAPH_CONNECTOR,
                OpenLineageServerAuditCode.ERROR_STARTING_LINEAGE_GRAPH_CONNECTOR,
                "startLineageGraphConnector");

        startIntopicConnector();
    }

    /**
     * Initialize the passed OpenLineageGraphConnector.
     *
     * @param connector         The connector that is to be initialized.
     * @param errorCode         The error code that should be used when the connector can not be initialized.
     * @param auditCode         The audit code that should be used when the connector can not be initialized.
     * @param actionDescription The action taking place in this method, used in error reporting
     *
     * @throws OMAGConfigurationErrorException
     */
    private void initializeGraphConnectorDB(OpenLineageGraphConnector connector, OpenLineageServerErrorCode errorCode,
                                            OpenLineageServerAuditCode auditCode, String actionDescription) throws OMAGConfigurationErrorException {
        final String methodName = "initializeGraphConnectorDB";
        try {
            connector.initializeGraphDB(this.auditLog);
        } catch (OCFCheckedExceptionBase e) {
            OCFCheckedExceptionToOMAGConfigurationError(e, auditCode, actionDescription);
        } catch (Exception e) {
            exceptionToOMAGConfigurationError(e, errorCode, methodName, auditCode, actionDescription);
        }
    }

    /**
     * Start the passed OpenLineageGraphConnector.
     *
     * @param connector         The connector that is to be started.
     * @param errorCode         The error code that should be used when the connector can not be started.
     * @param auditCode         The audit code that should be used when the connector can not be started.
     * @param actionDescription The action taking place in this method, used in error reporting.
     *
     * @throws OMAGConfigurationErrorException
     */
    private void startGraphConnector(OpenLineageGraphConnector connector, OpenLineageServerErrorCode errorCode, OpenLineageServerAuditCode auditCode, String actionDescription) throws OMAGConfigurationErrorException {
        final String methodName = "startGraphConnector";
        try {
            connector.start();
        } catch (OCFCheckedExceptionBase e) {
            OCFCheckedExceptionToOMAGConfigurationError(e, auditCode, actionDescription);
        } catch (Exception e) {
            exceptionToOMAGConfigurationError(e, errorCode, methodName, auditCode, actionDescription);
        }
    }

    /**
     * Start the Open Lineage Services in-topic connector
     *
     * @throws OMAGConfigurationErrorException
     */
    private void startIntopicConnector() throws OMAGConfigurationErrorException, InvalidParameterException {
        final String actionDescription = "Start the Open Lineage Services in-topic listener";
        final String methodName = "startIntopicConnector";
        final OpenLineageServerAuditCode auditCode = OpenLineageServerAuditCode.ERROR_STARTING_IN_TOPIC_CONNECTOR;
        inTopicConnector.setAuditLog(auditLog);
        LineageGraph lineageGraph = lineageGraphConnector.getLineageStorageService();
        StoringServices storingServices = new StoringServices(lineageGraph);
        OLSSimplifiedAccessServiceConfig accessServiceConfig = openLineageServerConfig.getAccessServiceConfig();
        assetLineageClient = new AssetLineage(accessServiceConfig.getServerName(), accessServiceConfig.getServerPlatformUrlRoot());
        OpenLineageAssetContextHandler assetContextHandler = new OpenLineageAssetContextHandler(localServerUserId, assetLineageClient);
        AssetLineageEventListener openLineageInTopicListener = new OpenLineageInTopicListener(storingServices,
                assetContextHandler, auditLog);
        inTopicConnector.registerListener(localServerUserId, openLineageInTopicListener);
        try {
            inTopicConnector.start();
        } catch (OCFCheckedExceptionBase e) {
            OCFCheckedExceptionToOMAGConfigurationError(e, auditCode, actionDescription);
        } catch (Exception e) {
            exceptionToOMAGConfigurationError(e, OpenLineageServerErrorCode.ERROR_STARTING_IN_TOPIC_CONNECTOR, methodName, auditCode, actionDescription);
        }
        logRecord(OpenLineageServerAuditCode.SERVER_REGISTERED_WITH_IN_TOPIC, actionDescription);
    }


    /**
     * Throw an OMAGConfigurationErrorException using an OpenLineageServerErrorCode.
     *
     * @param errorCode         Details about the exception that occurred, in a format intended for web users.
     * @param methodName        The name of the calling method.
     * @param auditCode         Details about the exception that occurred, in a format intended for system administrators.
     * @param actionDescription The action that was taking place when the error occurred.
     *
     * @throws OMAGConfigurationErrorException
     */
    private void throwOMAGConfigurationErrorException(OpenLineageServerErrorCode errorCode, String errorDetails, String methodName,
                                                      OpenLineageServerAuditCode auditCode, String actionDescription)
            throws OMAGConfigurationErrorException {
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(errorDetails);
        OMAGConfigurationErrorException e = new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                null,
                null,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null,
                null);
        logException(auditCode, actionDescription, e);
        throw e;
    }


    /**
     * Convert an Exception to an OMAGConfigurationErrorException
     *
     * @param e                 The exception object that was thrown
     * @param auditCode         Details about the exception that occurred, in a format intended for system administrators.
     * @param actionDescription The action that was taking place when the exception occurred.
     *
     * @throws OMAGConfigurationErrorException
     */
    private void exceptionToOMAGConfigurationError(Exception e, OpenLineageServerErrorCode errorCode, String methodName, OpenLineageServerAuditCode
            auditCode, String actionDescription) throws OMAGConfigurationErrorException {
        logException(auditCode, actionDescription, e);
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);
        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                null,
                null,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null,
                null);
    }

    /**
     * Convert an Exception to a PropertyServerException
     *
     * @param e                 The exception object that was thrown
     * @param auditCode         Details about the exception that occurred, in a format intended for system administrators.
     * @param actionDescription The action that was taking place when the exception occurred.
     *
     * @throws PropertyServerException The service had trouble shutting down.
     */
    private void exceptionToPropertyServerException(Exception e, OpenLineageServerErrorCode errorCode, String methodName, OpenLineageServerAuditCode
            auditCode, String actionDescription) throws PropertyServerException {
        logException(auditCode, actionDescription, e);
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);
        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                null,
                null,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null,
                null);
    }

    /**
     * Convert an OCFCheckedExceptionBase exception to an OMAGConfigurationErrorException
     *
     * @param e                 The exception object that was thrown
     * @param auditCode         Details about the exception that occurred, in a format intended for system administrators.
     * @param actionDescription The action that was taking place when the exception occurred.
     *
     * @throws OMAGConfigurationErrorException
     */
    private void OCFCheckedExceptionToOMAGConfigurationError(OCFCheckedExceptionBase e, OpenLineageServerAuditCode auditCode, String actionDescription) throws OMAGConfigurationErrorException {
        logException(auditCode, actionDescription, e);
        throw new OMAGConfigurationErrorException(e.getReportedHTTPCode(),
                e.getReportingClassName(),
                e.getReportingActionDescription(),
                e.getReportedErrorMessage(),
                e.getReportedErrorMessageId(),
                e.getReportedErrorMessageParameters(),
                e.getReportedSystemAction(),
                e.getReportedUserAction(),
                e.getReportedCaughtExceptionClassName(),
                e.getRelatedProperties());
    }

    /**
     * Shutdown the Open Lineage Services.
     *
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean shutdown() throws PropertyServerException {
        String actionDescription = "Shutting down the open lineage Services server.";
        logRecord(OpenLineageServerAuditCode.SERVER_SHUTTING_DOWN, actionDescription);


        stopBackgroundJob();

        disconnectInTopicConnector();

        disconnectGraphConnector(lineageGraphConnector,
                OpenLineageServerErrorCode.ERROR_DISCONNECTING_LINEAGE_GRAPH_CONNECTOR,
                OpenLineageServerAuditCode.ERROR_DISCONNECTING_LINEAGE_GRAPH_CONNECTOR,
                "Disconnecting lineage graph connection.");

        if (openLineageServerInstance != null)
            openLineageServerInstance.shutdown();

        logRecord(OpenLineageServerAuditCode.SERVER_SHUTDOWN, actionDescription);
        return true;
    }

    /**
     * Triggers stop sequence on the background jobs implementations
     */
    private void stopBackgroundJob() {
        if (CollectionUtils.isNotEmpty(backgroundJobs)) {
            backgroundJobs.forEach(JobConfiguration::stop);
        }
    }

    /**
     * Disconnect the passed OpenLineageGraphConnector.
     *
     * @param connector         The connector that is to be disconnected.
     * @param auditCode         The potential error that could occur, in a format intended for system administrators.
     * @param actionDescription The action taking place in this method, used in error reporting.
     *
     * @throws PropertyServerException
     */
    private void disconnectGraphConnector(OpenLineageGraphConnector connector, OpenLineageServerErrorCode errorCode, OpenLineageServerAuditCode auditCode, String actionDescription) throws PropertyServerException {
        final String methodName = "disconnectGraphConnector";
        if (connector == null)
            return;
        try {
            connector.disconnect();
        } catch (Exception e) {
            exceptionToPropertyServerException(e, errorCode, methodName, auditCode, actionDescription);
        }
    }

    /**
     * Disconnect the Open Lineage Services in-topic connector
     */
    private void disconnectInTopicConnector() throws PropertyServerException {
        final String methodName = "disconnectInTopicConnector";
        final String actionDescription = "Disconnecting the Open Lineage Services in-topic listener";

        if (inTopicConnector == null)
            return;
        try {
            inTopicConnector.disconnect();
        } catch (Exception e) {
            exceptionToPropertyServerException(e, OpenLineageServerErrorCode.ERROR_DISCONNECTING_IN_TOPIC_CONNECTOR, methodName, OpenLineageServerAuditCode.ERROR_DISCONNECTING_IN_TOPIC_CONNECTOR, actionDescription);
        }
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
}

