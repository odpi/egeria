/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.processor;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineImpl;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.DataEngineConnectorBase;
import org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.model.*;
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

        // Retrieve the base information from the connector
        if (connector != null) {
            try {
                DataEngineSoftwareServerCapability dataEngineDetails = connector.getDataEngineDetails();
                dataEngineOMASClient.createExternalDataEngine(dataEngineDetails.getUserId(), dataEngineDetails.getSoftwareServerCapability());
                dataEngineOMASClient.setExternalSourceName(dataEngineDetails.getSoftwareServerCapability().getQualifiedName());
            } catch (InvalidParameterException | PropertyServerException e) {
                auditCode = DataEngineProxyAuditCode.OMAS_CONNECTION_ERROR;
                this.auditLog.logException(methodName,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        e.getErrorMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction(),
                        e);
            } catch (UserNotAuthorizedException e) {
                auditCode = DataEngineProxyAuditCode.USER_NOT_AUTHORIZED;
                this.auditLog.logRecord(methodName,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        e.getErrorMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
            }
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

                DataEngineProxyAuditCode auditCode = DataEngineProxyAuditCode.POLLING;
                this.auditLog.logRecord(methodName,
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
                DataEngineProxyAuditCode auditCode = DataEngineProxyAuditCode.OMAS_CONNECTION_ERROR;
                this.auditLog.logException(methodName,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        e.getErrorMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction(),
                        e);
            } catch (UserNotAuthorizedException e) {
                DataEngineProxyAuditCode auditCode = DataEngineProxyAuditCode.USER_NOT_AUTHORIZED;
                this.auditLog.logRecord(methodName,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        e.getErrorMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
            } catch (Exception e) {
                DataEngineProxyAuditCode auditCode = DataEngineProxyAuditCode.USER_NOT_AUTHORIZED;
                this.auditLog.logException(methodName,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction(),
                        e);
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
        log.info(" ... getting changed schema types.");
        List<DataEngineSchemaType> changedSchemaTypes = connector.getChangedSchemaTypes(changesLastSynced, changesCutoff);
        if (changedSchemaTypes != null) {
            for (DataEngineSchemaType changedSchemaType : changedSchemaTypes) {
                dataEngineOMASClient.createOrUpdateSchemaType(changedSchemaType.getUserId(), changedSchemaType.getSchemaType());
            }
            log.info(" ... completing schema type changes.");
        }
    }

    private void upsertPortImplementations(Date changesLastSynced,
                                           Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        log.info(" ... getting changed port implementations.");
        List<DataEnginePortImplementation> changedPortImplementations = connector.getChangedPortImplementations(changesLastSynced, changesCutoff);
        if (changedPortImplementations != null) {
            for (DataEnginePortImplementation changedPortImplementation : changedPortImplementations) {
                dataEngineOMASClient.createOrUpdatePortImplementation(changedPortImplementation.getUserId(), changedPortImplementation.getPortImplementation());
            }
            log.info(" ... completing port implementation changes.");
        }
    }

    private void upsertPortAliases(Date changesLastSynced,
                                   Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        log.info(" ... getting changed port aliases.");
        List<DataEnginePortAlias> changedPortAliases = connector.getChangedPortAliases(changesLastSynced, changesCutoff);
        if (changedPortAliases != null) {
            for (DataEnginePortAlias changedPortAlias : changedPortAliases) {
                dataEngineOMASClient.createOrUpdatePortAlias(changedPortAlias.getUserId(), changedPortAlias.getPortAlias());
            }
            log.info(" ... completing port alias changes.");
        }
    }

    private void upsertProcesses(Date changesLastSynced,
                                 Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        log.info(" ... getting changed processes.");
        List<DataEngineProcess> changedProcesses = connector.getChangedProcesses(changesLastSynced, changesCutoff);
        if (changedProcesses != null) {
            for (DataEngineProcess changedProcess : changedProcesses) {
                dataEngineOMASClient.createOrUpdateProcess(changedProcess.getUserId(), changedProcess.getProcess());
            }
            log.info(" ... completing process changes.");
        }
    }

    private void upsertLineageMappings(Date changesLastSynced,
                                       Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        log.info(" ... getting changed lineage mappings.");
        List<DataEngineLineageMappings> changedLineageMappings = connector.getChangedLineageMappings(changesLastSynced, changesCutoff);
        if (changedLineageMappings != null) {
            for (DataEngineLineageMappings changedLineageMapping : changedLineageMappings) {
                dataEngineOMASClient.addLineageMappings(changedLineageMapping.getUserId(), new ArrayList<>(changedLineageMapping.getLineageMappings()));
            }
            log.info(" ... completing lineage mapping changes.");
        }
    }

}
