/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.processor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyErrorCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.DataEngineConnectorBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class to handle periodically polling a Data Engine for changes, for those data engines that do not
 * provide any event-based mechanism to notify on changes.
 */
public class DataEngineProxyService implements Runnable {

    private final OMRSAuditLog auditLog;
    private final DataEngineProxyConfig dataEngineProxyConfig;
    private final DataEngineClient dataEngineOMASClient;
    private final DataEngineConnectorBase connector;
    private final String userId;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public void initialize() throws ConnectorCheckedException, UserNotAuthorizedException, InvalidParameterException, PropertyServerException {

        final String methodName = "start";
        this.auditLog.logMessage(methodName, DataEngineProxyAuditCode.INIT_POLLING.getMessageDefinition());

        // Retrieve the base information from the connector
        if (connector != null) {
            Engine dataEngineDetails = connector.getDataEngineDetails();
            dataEngineOMASClient.createExternalDataEngine(userId, dataEngineDetails);
            dataEngineOMASClient.setExternalSourceName(dataEngineDetails.getQualifiedName());
            if (connector.requiresPolling()) {
                Thread worker = new Thread(this);
                worker.setName(DataEngineProxyService.class.getName());
                worker.start();
            }
        }
    }

    public void stop() {
        running.set(false);
    }

    /**
     * Default constructor
     *
     * @param connector             Data Engine Connector through which to connect to the data engine to poll
     * @param userId                the user ID used to poll for changes against the connector
     * @param dataEngineProxyConfig configuration of the Data Engine (Proxy)
     * @param dataEngineOMASClient  Data Engine OMAS client through which to push any changes into Egeria
     * @param auditLog              audit log through which to record activities
     */
    public DataEngineProxyService(DataEngineConnectorBase connector,
                                  String userId,
                                  DataEngineProxyConfig dataEngineProxyConfig,
                                  DataEngineClient dataEngineOMASClient,
                                  OMRSAuditLog auditLog) {
        this.connector = connector;
        this.userId = userId;
        this.dataEngineProxyConfig = dataEngineProxyConfig;
        this.dataEngineOMASClient = dataEngineOMASClient;
        this.auditLog = auditLog;

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
                Date changesCutoff = new Date();
                Date oldestSinceSync = getProcessingState();
                if(oldestSinceSync != null) {
                    long window = oldestSinceSync.getTime() + (dataEngineProxyConfig.getBatchWindowInSeconds() * 1000L);
                    long now = changesCutoff.getTime();
                    // We will look for changes up to that batch window size or the current moment, whichever is sooner
                    changesCutoff = new Date(Math.min(window, now));
                }
                ensureSourceNameIsSet();

                this.auditLog.logMessage(methodName,
                        DataEngineProxyAuditCode.POLLING.getMessageDefinition(
                                oldestSinceSync == null ? "0" : oldestSinceSync.toString(),
                                changesCutoff.toString()
                        ));

                // Send the changes, and ordering here is important
                upsertSchemaTypes(oldestSinceSync, changesCutoff);
                upsertDataStores(oldestSinceSync, changesCutoff);
                upsertProcesses(oldestSinceSync, changesCutoff);
                upsertProcessHierarchies(oldestSinceSync, changesCutoff);
                upsertLineageMappings(oldestSinceSync, changesCutoff);

                // Update the timestamp at which changes were last synced
                upsertProcessingState(changesCutoff);

                // Sleep for the poll interval before continuing with the next poll
                sleep();
            } catch (PropertyServerException e) {
                // Potentially recoverable error. Retry.
                this.auditLog.logException(methodName, DataEngineProxyAuditCode.RUNTIME_EXCEPTION.getMessageDefinition(), e);
                sleep();
            } catch (UserNotAuthorizedException | InvalidParameterException | ConnectorCheckedException e) {
                // Interrupt processing and propagate runtime error.
                this.auditLog.logException(methodName, DataEngineProxyAuditCode.RUNTIME_EXCEPTION.getMessageDefinition(), e);
                throw new OCFRuntimeException(DataEngineProxyErrorCode.UNKNOWN_ERROR.getMessageDefinition(), this.getClass().getName(), methodName, e);
            }
        }

    }

    public void load() {
        final String methodName = "load";
        Date now = Date.from(Instant.now());
        try {

            ensureSourceNameIsSet();
            upsertSchemaTypes(now, now);
            upsertDataStores(now, now);
            upsertProcesses(now, now);
            upsertProcessHierarchies(now, now);
            upsertLineageMappings(now, now);

            upsertProcessingState(now);

        } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException | ConnectorCheckedException e) {
            this.auditLog.logException(methodName, DataEngineProxyAuditCode.RUNTIME_EXCEPTION.getMessageDefinition(), e);
        }
    }

    public void pollProcessChanges(String processId) {
        /*
         * TODO
         * */
    }

    private void upsertProcessingState(Date changesCutoff) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException, ConnectorCheckedException {
        String processingStateKey = connector.getProcessingStateSyncKey();
        Map<String, Long> properties = Collections.singletonMap(processingStateKey, changesCutoff.getTime());
        dataEngineOMASClient.upsertProcessingState(userId, properties);
    }

    private Date getProcessingState() throws PropertyServerException {
        String processingStateKey = connector.getProcessingStateSyncKey();

        Map<String, Long> processingState = dataEngineOMASClient.getProcessingState(userId);
        if(MapUtils.isNotEmpty(processingState)) {
            Long lastSync = processingState.get(processingStateKey);
            return new Date(lastSync);
        }
        return null;
    }

    /**
     * Sleep until the next polling interval comes.
     */
    private void sleep() {
        try {
            Thread.sleep(dataEngineProxyConfig.getPollIntervalInSeconds() * 1000L);
        } catch (InterruptedException e) {
            this.auditLog.logException("sleep", DataEngineProxyAuditCode.RUNTIME_EXCEPTION.getMessageDefinition(), e);
        }
    }

    private void ensureSourceNameIsSet() {
        if (dataEngineOMASClient.getExternalSourceName() == null) {
            dataEngineOMASClient.setExternalSourceName(connector.getDataEngineDetails().getQualifiedName());
        }
    }

    private void upsertSchemaTypes(Date changesLastSynced,
                                   Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            ConnectorCheckedException {
        final String methodName = "upsertSchemaTypes";
        final String type = "SchemaTypes";
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_START.getMessageDefinition(type));
        List<SchemaType> changedSchemaTypes = connector.getChangedSchemaTypes(changesLastSynced, changesCutoff);
        if (changedSchemaTypes != null) {
            for (SchemaType changedSchemaType : changedSchemaTypes) {
                dataEngineOMASClient.createOrUpdateSchemaType(userId, changedSchemaType);
            }
        }
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_FINISH.getMessageDefinition(type));
    }

    private void upsertDataStores(Date changesLastSynced,
                                  Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            ConnectorCheckedException {
        final String methodName = "upsertDataStores";
        final String type = "DataStores";
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_START.getMessageDefinition(type));
        // get  list of incomplete relational tables & data files
        List<? super Referenceable> changedDataStores = connector.getChangedDataStores(changesLastSynced, changesCutoff);
        if (CollectionUtils.isNotEmpty(changedDataStores)) {
            for (Object changedDataStore : changedDataStores) {
                if (changedDataStore instanceof DataFile) {
                    dataEngineOMASClient.upsertDataFile(userId, (DataFile) changedDataStore);
                }
                if (changedDataStore instanceof Database) {
                    Database database = (Database) changedDataStore;
                    // create the database only if it's incomplete
                    // will also create database schemas and relational table
                    if (database.getIncomplete()) {
                        dataEngineOMASClient.upsertDatabase(userId, database);
                    } else {
                        // create the database schema only if it's incomplete
                        if (database.getDatabaseSchema().getIncomplete()) {
                            dataEngineOMASClient.upsertDatabaseSchema(userId, database.getDatabaseSchema(), database.getQualifiedName());
                        }
                        // create the table separately if the database was not created
                        dataEngineOMASClient.upsertRelationalTable(userId, database.getTables().get(0),
                                database.getDatabaseSchema().getQualifiedName());
                    }
                }
            }
        }
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_FINISH.getMessageDefinition(type));
    }

    private void upsertProcesses(Date changesLastSynced,
                                 Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            ConnectorCheckedException {
        final String methodName = "upsertProcesses";
        final String type = "Processes";
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_START.getMessageDefinition(type));
        List<Process> changedProcesses = connector.getChangedProcesses(changesLastSynced, changesCutoff);
        if (changedProcesses != null && !changedProcesses.isEmpty()) {
            for (Process changedProcess : changedProcesses) {
                // We split up the process details (1) and lineage mappings (2) into separate calls to achieve optimal processing in DE OMAS.
                // (1) Send process details
                dataEngineOMASClient.createOrUpdateProcess(userId, changedProcess);

                List<LineageMapping> lineageMappings = changedProcess.getLineageMappings();
                if (lineageMappings != null) {
                    // (2) Send lineage mappings
                    dataEngineOMASClient.addLineageMappings(userId, lineageMappings);
                }
            }

        }
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_FINISH.getMessageDefinition(type));
    }

    private void upsertProcessHierarchies(Date changesLastSynced,
                                          Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            ConnectorCheckedException {
        final String methodName = "upsertProcessHierarchies";
        final String type = "ProcessHierarchies";
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_START.getMessageDefinition(type));
        List<ProcessHierarchy> changedProcessHierarchies = connector.getChangedProcessHierarchies(changesLastSynced, changesCutoff);
        if (changedProcessHierarchies != null) {
            for (ProcessHierarchy changedProcessHierarchy : changedProcessHierarchies) {
                dataEngineOMASClient.addProcessHierarchy(userId, changedProcessHierarchy);
            }
        }
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_FINISH.getMessageDefinition(type));
    }

    private void upsertLineageMappings(Date changesLastSynced,
                                       Date changesCutoff) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            ConnectorCheckedException {
        final String methodName = "upsertLineageMappings";
        final String type = "LineageMappings";
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_START.getMessageDefinition(type));
        List<LineageMapping> changedLineageMappings = connector.getChangedLineageMappings(changesLastSynced, changesCutoff);
        if (CollectionUtils.isNotEmpty(changedLineageMappings)) {
            if (dataEngineProxyConfig.isEventsClientEnabled()) {
                for (LineageMapping changedLineageMapping : changedLineageMappings) {
                    // If we are using the event-based interface, send the lineage mappings one-by-one rather than as
                    // an array
                    dataEngineOMASClient.addLineageMappings(userId, Collections.singletonList(changedLineageMapping));
                }
            } else {
                dataEngineOMASClient.addLineageMappings(userId, changedLineageMappings);
            }
        }
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.POLLING_TYPE_FINISH.getMessageDefinition(type));
    }

}
