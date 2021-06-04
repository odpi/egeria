/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.processor;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.*;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyErrorCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.DataEngineConnectorBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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
    private DataEngineClient dataEngineOMASClient;
    private DataEngineConnectorBase connector;
    private String userId;

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
     * @param userId                the user ID used to poll for changes against the connector
     * @param dataEngineProxyConfig configuration of the Data Engine (Proxy)
     * @param dataEngineOMASClient  Data Engine OMAS client through which to push any changes into Egeria
     * @param auditLog              audit log through which to record activities
     */
    public DataEngineProxyChangePoller(DataEngineConnectorBase connector,
                                       String userId,
                                       DataEngineProxyConfig dataEngineProxyConfig,
                                       DataEngineClient dataEngineOMASClient,
                                       OMRSAuditLog auditLog) {

        final String methodName = "DataEngineProxyChangePoller";

        this.connector = connector;
        this.userId = userId;
        this.dataEngineProxyConfig = dataEngineProxyConfig;
        this.dataEngineOMASClient = dataEngineOMASClient;
        this.auditLog = auditLog;

        this.auditLog.logMessage(methodName, DataEngineProxyAuditCode.INIT_POLLING.getMessageDefinition());

        // Retrieve the base information from the connector
        if (connector != null) {
            try {
                SoftwareServerCapability dataEngineDetails = connector.getDataEngineDetails();
                dataEngineOMASClient.createExternalDataEngine(userId, dataEngineDetails);
                dataEngineOMASClient.setExternalSourceName(dataEngineDetails.getQualifiedName());
            } catch (InvalidParameterException | PropertyServerException | ConnectorCheckedException e) {
                this.auditLog.logException(methodName, DataEngineProxyAuditCode.OMAS_CONNECTION_ERROR.getMessageDefinition(), e);
            } catch (UserNotAuthorizedException e) {
                this.auditLog.logMessage(methodName, DataEngineProxyAuditCode.USER_NOT_AUTHORIZED.getMessageDefinition("setup external data engine"));
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

                // Start with the last change synchronization date and time
                Date changesLastSynced = connector.getChangesLastSynced();

                // Then look for the oldest change available in the Data Engine since that time
                Date oldestSinceSync = connector.getOldestChangeSince(changesLastSynced);
                Date changesCutoff = new Date();
                if (oldestSinceSync == null) {
                    // If there were no changes since the last sync time, default to the last sync time
                    oldestSinceSync = changesLastSynced;
                } else {
                    // If there are any changes since that last sync time, calculate a batch window from that oldest
                    // change to the maximum amount of time to include in a batch
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
                upsertProcesses(oldestSinceSync, changesCutoff);
                upsertProcessHierarchies(oldestSinceSync, changesCutoff);
                upsertLineageMappings(oldestSinceSync, changesCutoff);

                // Update the timestamp at which changes were last synced
                connector.setChangesLastSynced(changesCutoff);

                // Sleep for the poll interval before continuing with the next poll
                Thread.sleep(dataEngineProxyConfig.getPollIntervalInSeconds() * 1000L);

            } catch (InvalidParameterException | PropertyServerException | ConnectorCheckedException e ) {
                this.auditLog.logException(methodName, DataEngineProxyAuditCode.OMAS_CONNECTION_ERROR.getMessageDefinition(), e);
            } catch (UserNotAuthorizedException e) {
                this.auditLog.logMessage(methodName, DataEngineProxyAuditCode.USER_NOT_AUTHORIZED.getMessageDefinition("send changes"));
            } catch (Exception e) {
                throw new OCFRuntimeException(DataEngineProxyErrorCode.UNKNOWN_ERROR.getMessageDefinition(), this.getClass().getName(), methodName, e);
            }
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
            if (dataEngineProxyConfig.isEventsClientEnabled()) {
                // If we are using the event-based interface, send the processes one-by-one rather than as an array
                for (Process changedProcess : changedProcesses) {
                    dataEngineOMASClient.createOrUpdateProcesses(userId, Collections.singletonList(changedProcess));
                }
            } else{
                dataEngineOMASClient.createOrUpdateProcesses(userId, changedProcesses);
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
        if (changedLineageMappings != null && changedLineageMappings.size() > 0) {
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
