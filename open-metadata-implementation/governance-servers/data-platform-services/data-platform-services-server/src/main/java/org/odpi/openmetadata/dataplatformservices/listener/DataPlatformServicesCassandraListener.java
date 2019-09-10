/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.listener;

import com.datastax.driver.core.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformEventType;
import org.odpi.openmetadata.accessservices.dataplatform.events.NewDeployedDatabaseSchemaEvent;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DataPlatform;
import org.odpi.openmetadata.accessservices.dataplatform.properties.cassandra.Column;
import org.odpi.openmetadata.accessservices.dataplatform.properties.cassandra.Keyspace;
import org.odpi.openmetadata.accessservices.dataplatform.properties.cassandra.Table;
import org.odpi.openmetadata.adapters.connectors.cassandra.CassandraStoreListener;
import org.odpi.openmetadata.adminservices.configuration.properties.DataPlatformConfig;
import org.odpi.openmetadata.dataplatformservices.auditlog.DataPlatformServicesAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopic;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DataPlatformServicesCassandraListener implements CassandraStoreListener, OpenMetadataTopicListener {


    private static final Logger log = LoggerFactory.getLogger(DataPlatformServicesCassandraListener.class);

    private OMRSAuditLog auditLog;
    private OpenMetadataTopic dataPlatformServicesOutTopicConnector;
    private DataPlatformConfig dataPlatformConfig;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public DataPlatformServicesCassandraListener(OMRSAuditLog auditLog, OpenMetadataTopic dataPlatformServicesOutTopicConnector, DataPlatformConfig dataPlatformConfig) {
        this.auditLog = auditLog;
        this.dataPlatformServicesOutTopicConnector = dataPlatformServicesOutTopicConnector;
        this.dataPlatformConfig = dataPlatformConfig;
    }

    @Override
    public void onKeyspaceAdded(KeyspaceMetadata keyspaceMetadata) {

        NewDeployedDatabaseSchemaEvent newDeployedDatabaseSchemaEvent =new NewDeployedDatabaseSchemaEvent();
        String actionDescription = "Sending NewDeployedDatabaseSchemaEvent to Data Platform OMAS in topic.";

        try {
            Keyspace keyspace = new Keyspace();
            DataPlatform dataPlatform = new DataPlatform();

            List<ConnectorType> connectorTypes=new ArrayList<>();
            connectorTypes.add(dataPlatformConfig.getDataPlatformConnection().getConnectorType());
            dataPlatform.setDataPlatformConnectorType(connectorTypes);
            dataPlatform.setDataPlatformEndpoint(dataPlatformConfig.getDataPlatformConnection().getEndpoint());

            if (!keyspaceMetadata.getTables().isEmpty()){
                List<Table> tableList=new ArrayList<>();
                for (TableMetadata tableMetadata:keyspaceMetadata.getTables()){
                    Table table=new Table();
                    table.setTableName(tableMetadata.getName());
                    table.setGuid(tableMetadata.getId().toString());
                    List<Column> columnList=new ArrayList<>();
                    for (ColumnMetadata columnMetadata:tableMetadata.getColumns()){
                        Column column = new Column();
                        column.setName(columnMetadata.getName());
                        column.setType(columnMetadata.getType().asFunctionParameterString());
                        columnList.add(column);
                    }
                    table.setColumns(columnList);
                    tableList.add(table);
                }
                keyspace.setTableList(tableList);
            }

            keyspace.setGuid(UUID.randomUUID().toString());
            keyspace.setKeyspaceName(keyspaceMetadata.getName());
            keyspace.setReplication(keyspaceMetadata.getReplication());
            keyspace.setReplicationStrategy(keyspaceMetadata.getReplication().get("class"));

            newDeployedDatabaseSchemaEvent.setKeyspace(keyspace);
            newDeployedDatabaseSchemaEvent.setDataPlatform(dataPlatform);
            newDeployedDatabaseSchemaEvent.setEventType(DataPlatformEventType.NEW_DEPLOYED_DB_SCHEMA_EVENT);

            log.info("Sending event to Data Platform OMAS in topic");
            log.debug("event: ", newDeployedDatabaseSchemaEvent);

            dataPlatformServicesOutTopicConnector.sendEvent(OBJECT_MAPPER.writeValueAsString(newDeployedDatabaseSchemaEvent));

        } catch (ConnectorCheckedException | JsonProcessingException error) {
            log.error("Exception publishing event", error);
            DataPlatformServicesAuditCode errorCode = DataPlatformServicesAuditCode.PUBLISH_EVENT_EXCEPTION;
            auditLog.logException(
                    actionDescription,
                    errorCode.getLogMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedLogMessage(newDeployedDatabaseSchemaEvent.getClass().getName(), error.getMessage()),
                    "newDeployedDatabaseSchemaEvent {" + newDeployedDatabaseSchemaEvent.toString() + "}",
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);
        }
    }

    @Override
    public void onKeyspaceRemoved(KeyspaceMetadata keyspaceMetadata) {

    }

    @Override
    public void onKeyspaceChanged(KeyspaceMetadata keyspaceMetadata, KeyspaceMetadata keyspaceMetadata1) {

    }

    @Override
    public void onTableAdded(TableMetadata tableMetadata) {

    }

    @Override
    public void onTableRemoved(TableMetadata tableMetadata) {

    }

    @Override
    public void onTableChanged(TableMetadata tableMetadata, TableMetadata tableMetadata1) {

    }

    @Override
    public void onUserTypeAdded(UserType userType) {

    }

    @Override
    public void onUserTypeRemoved(UserType userType) {

    }

    @Override
    public void onUserTypeChanged(UserType userType, UserType userType1) {

    }

    @Override
    public void onFunctionAdded(FunctionMetadata functionMetadata) {

    }

    @Override
    public void onFunctionRemoved(FunctionMetadata functionMetadata) {

    }

    @Override
    public void onFunctionChanged(FunctionMetadata functionMetadata, FunctionMetadata functionMetadata1) {

    }

    @Override
    public void onAggregateAdded(AggregateMetadata aggregateMetadata) {

    }

    @Override
    public void onAggregateRemoved(AggregateMetadata aggregateMetadata) {

    }

    @Override
    public void onAggregateChanged(AggregateMetadata aggregateMetadata, AggregateMetadata aggregateMetadata1) {

    }

    @Override
    public void onMaterializedViewAdded(MaterializedViewMetadata materializedViewMetadata) {

    }

    @Override
    public void onMaterializedViewRemoved(MaterializedViewMetadata materializedViewMetadata) {

    }

    @Override
    public void onMaterializedViewChanged(MaterializedViewMetadata materializedViewMetadata, MaterializedViewMetadata materializedViewMetadata1) {

    }

    @Override
    public void onRegister(Cluster cluster) {


        String action = cluster.getMetadata().exportSchemaAsString();
        String action2 = cluster.getConfiguration().getPoolingOptions().toString();
        String action3 = cluster.getConfiguration().getProtocolOptions().getProtocolVersion().toString();
        log.info(action);
        log.info(action2);
        log.info(action3);



    }

    @Override
    public void onUnregister(Cluster cluster) {

    }

    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    @Override
    public void processEvent(String event) {

    }
}
