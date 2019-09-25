/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adminservices.configuration.properties.DataPlatformConfig;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataPlatformServicesCassandraListener  {


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

   /* @Override
    public void onKeyspaceAdded(KeyspaceMetadata keyspaceMetadata) {

        NewDeployedDatabaseSchemaEvent newDeployedDatabaseSchemaEvent =new NewDeployedDatabaseSchemaEvent();
        String actionDescription = "Sending NewDeployedDatabaseSchemaEvent to Data Platform OMAS in topic.";

        try {
            DeployedDatabaseSchema deployedDatabaseSchema = new DeployedDatabaseSchema();
            DataPlatform dataPlatform = new DataPlatform();

            List<ConnectorType> connectorTypes=new ArrayList<>();
            connectorTypes.add(dataPlatformConfig.getDataPlatformConnection().getConnectorType());
            dataPlatform.setDataPlatformConnectorType(connectorTypes);
            dataPlatform.setDataPlatformEndpoint(dataPlatformConfig.getDataPlatformConnection().getEndpoint());

            if (!keyspaceMetadata.getTables().isEmpty()){
                List<TabularSchema> tabularSchemaList =new ArrayList<>();
                for (TableMetadata tableMetadata:keyspaceMetadata.getTables()){
                    TabularSchema tabularSchema =new TabularSchema();
                    tabularSchema.setName(tableMetadata.getName());
                    tabularSchema.setGuid(tableMetadata.getId().toString());
                    List<TabularColumn> tabularColumnList =new ArrayList<>();
                    for (ColumnMetadata columnMetadata:tableMetadata.getColumns()){
                        TabularColumn tabularColumn = new TabularColumn();
                        tabularColumn.setName(columnMetadata.getName());
                        tabularColumn.setType(columnMetadata.getType().asFunctionParameterString());
                        tabularColumnList.add(tabularColumn);
                    }
                    tabularSchema.setTabularColumns(tabularColumnList);
                    tabularSchemaList.add(tabularSchema);
                }
                deployedDatabaseSchema.setTabularSchemaList(tabularSchemaList);
            }

            deployedDatabaseSchema.setGuid(UUID.randomUUID().toString());
            deployedDatabaseSchema.setName(keyspaceMetadata.getName());
            deployedDatabaseSchema.setAdditionalProperties(keyspaceMetadata.getReplication());

            newDeployedDatabaseSchemaEvent.setDeployedDatabaseSchema(deployedDatabaseSchema);
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

    *//**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     *//*
    @Override
    public void processEvent(String event) {
*/
    }

