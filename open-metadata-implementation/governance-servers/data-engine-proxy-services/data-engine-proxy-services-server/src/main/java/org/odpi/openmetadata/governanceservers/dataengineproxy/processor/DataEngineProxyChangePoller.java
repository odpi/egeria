/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.processor;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineImpl;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.DataEngineConnectorBase;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.DataEngineConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class to handle periodically polling a Data Engine for changes, for those data engines that do not
 * provide any event-based mechanism to notify on changes.
 */
public class DataEngineProxyChangePoller implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DataEngineProxyChangePoller.class);

    private OMRSAuditLog auditLog;
    private DataEngineProxyConfig dataEngineProxyConfig;
    private DataEngineImpl dataEngineOMASClient;
    private DataEngineConnectorBase connector;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public void start() {
        Thread worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }

    /**
     * Default constructor
     *
     * @param connector             Data Engine Connector through which to connect to the data engine to poll
     * @param dataEngineProxyConfig configuration of the Data Engine (Proxy)
     * @param dataEngineOMASClient  Data Engine OMAS client through which to push any changes into Egeria
     * @param auditLog              audit log through which to record activities
     */
    public DataEngineProxyChangePoller(DataEngineConnectorBase connector,
                                       DataEngineProxyConfig dataEngineProxyConfig,
                                       DataEngineImpl dataEngineOMASClient,
                                       OMRSAuditLog auditLog) {

        final String methodName = "DataEngineProxyChangePoller";

        this.connector = connector;
        this.dataEngineProxyConfig = dataEngineProxyConfig;
        this.dataEngineOMASClient = dataEngineOMASClient;
        this.auditLog = auditLog;

        DataEngineProxyAuditCode auditCode;

        // Start the connector
        if (connector != null) {
            try {
                connector.start();
                DataEngineSoftwareServerCapability dataEngineDetails = connector.getDataEngineDetails();
                dataEngineOMASClient.createExternalDataEngine(dataEngineDetails.getUserId(), dataEngineDetails.getSoftwareServerCapability());
                dataEngineOMASClient.setExternalSourceName(dataEngineDetails.getSoftwareServerCapability().getQualifiedName());
            } catch (InvalidParameterException | PropertyServerException e) {
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.OMAS_CONNECTION_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            } catch (UserNotAuthorizedException e) {
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.USER_NOT_AUTHORIZED;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            } catch (ConnectorCheckedException e) {
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.UNKNOWN_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            }
        } else {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.NO_CONFIG;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }

        if (connector.isActive()) {
            auditCode = DataEngineProxyAuditCode.SERVICE_INITIALIZED;
            this.auditLog.logRecord("Initializing",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(connector.getConnection().getConnectorType().getConnectorProviderClassName()),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } else {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.NO_CONFIG;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }

    }

    /**
     * Poll for Process changes.
     */
    @Override
    public void run() {

        final String methodName = "ProcessPollThread::run";

        running.set(true);
        while (running.get()) {
            try {

                Date changesLastSynced = connector.getChangesLastSynced();
                Date changesCutoff = new Date();

                ensureSourceNameIsSet();

                if (log.isInfoEnabled()) { log.info("Polling for changes since: {}", changesLastSynced); }
                DataEngineProxyAuditCode auditCode = DataEngineProxyAuditCode.POLLING;
                this.auditLog.logRecord("Polling",
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(changesLastSynced == null ? "(all changes)" : changesLastSynced.toString()),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());

                // Send the changes, and ordering here is important
                upsertSchemaTypes(changesLastSynced, changesCutoff);
                upsertPortImplementations(changesLastSynced, changesCutoff);
                upsertPortAliases(changesLastSynced, changesCutoff);
                upsertProcesses(changesLastSynced, changesCutoff);
                upsertLineageMappings(changesLastSynced, changesCutoff);

                // Update the timestamp at which changes were last synced
                connector.setChangesLastSynced(changesCutoff);

                // Sleep for the poll interval before continuing with the next poll
                Thread.sleep(dataEngineProxyConfig.getPollIntervalInSeconds() * 1000L);

            } catch (InvalidParameterException | PropertyServerException e) {
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.OMAS_CONNECTION_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            } catch (UserNotAuthorizedException e) {
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.USER_NOT_AUTHORIZED;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            } catch (Exception e) {
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.UNKNOWN_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            }
        }

    }

    private void ensureSourceNameIsSet() {
        if (dataEngineOMASClient.getExternalSourceName() == null) {
            dataEngineOMASClient.setExternalSourceName(connector.getDataEngineDetails().getSoftwareServerCapability().getQualifiedName());
        }
    }

    private void upsertSchemaTypes(Date changesLastSynced,
                                   Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isInfoEnabled()) { log.info(" ... getting changed schema types."); }
        List<DataEngineSchemaType> changedSchemaTypes = connector.getChangedSchemaTypes(changesLastSynced, changesCutoff);
        if (changedSchemaTypes != null) {
            for (DataEngineSchemaType changedSchemaType : changedSchemaTypes) {
                dataEngineOMASClient.createOrUpdateSchemaType(changedSchemaType.getUserId(), changedSchemaType.getSchemaType());
            }
            if (log.isInfoEnabled()) { log.info(" ... completing schema type changes."); }
        }
    }

    private void upsertPortImplementations(Date changesLastSynced,
                                           Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isInfoEnabled()) { log.info(" ... getting changed port implementations."); }
        List<DataEnginePortImplementation> changedPortImplementations = connector.getChangedPortImplementations(changesLastSynced, changesCutoff);
        if (changedPortImplementations != null) {
            for (DataEnginePortImplementation changedPortImplementation : changedPortImplementations) {
                dataEngineOMASClient.createOrUpdatePortImplementation(changedPortImplementation.getUserId(), changedPortImplementation.getPortImplementation());
            }
            if (log.isInfoEnabled()) { log.info(" ... completing port implementation changes."); }
        }
    }

    private void upsertPortAliases(Date changesLastSynced,
                                   Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isInfoEnabled()) { log.info(" ... getting changed port aliases."); }
        List<DataEnginePortAlias> changedPortAliases = connector.getChangedPortAliases(changesLastSynced, changesCutoff);
        if (changedPortAliases != null) {
            for (DataEnginePortAlias changedPortAlias : changedPortAliases) {
                dataEngineOMASClient.createOrUpdatePortAlias(changedPortAlias.getUserId(), changedPortAlias.getPortAlias());
            }
            if (log.isInfoEnabled()) { log.info(" ... completing port alias changes."); }
        }
    }

    private void upsertProcesses(Date changesLastSynced,
                                 Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isInfoEnabled()) { log.info(" ... getting changed processes."); }
        List<DataEngineProcess> changedProcesses = connector.getChangedProcesses(changesLastSynced, changesCutoff);
        if (changedProcesses != null) {
            for (DataEngineProcess changedProcess : changedProcesses) {
                dataEngineOMASClient.createOrUpdateProcess(changedProcess.getUserId(), changedProcess.getProcess());
            }
            if (log.isInfoEnabled()) { log.info(" ... completing process changes."); }
        }
    }

    private void upsertLineageMappings(Date changesLastSynced,
                                       Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isInfoEnabled()) { log.info(" ... getting changed lineage mappings."); }
        List<DataEngineLineageMappings> changedLineageMappings = connector.getChangedLineageMappings(changesLastSynced, changesCutoff);
        if (changedLineageMappings != null) {
            for (DataEngineLineageMappings changedLineageMapping : changedLineageMappings) {
                dataEngineOMASClient.addLineageMappings(changedLineageMapping.getUserId(), new ArrayList<>(changedLineageMapping.getLineageMappings()));
            }
            if (log.isInfoEnabled()) { log.info(" ... completing lineage mapping changes."); }
        }
    }

}
