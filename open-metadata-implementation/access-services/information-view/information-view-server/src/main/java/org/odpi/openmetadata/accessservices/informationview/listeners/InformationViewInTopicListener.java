/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.informationview.events.DerivedColumn;
import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InformationViewInTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(InformationViewInTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final EntitiesCreatorHelper entitiesCreatorHelper;
    private final OMRSAuditLog auditLog;

    public InformationViewInTopicListener(EntitiesCreatorHelper entitiesCreatorHelper, OMRSAuditLog auditLog) {
        this.entitiesCreatorHelper = entitiesCreatorHelper;
        this.auditLog = auditLog;
    }

    /**
     * @param eventAsString contains all the information needed to build information view like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        InformationViewEvent event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, InformationViewEvent.class);
        } catch (Exception e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.PARSE_EVENT;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getErrorMessage(),
                    "event {" + eventAsString + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

        }
        if (event != null) {
            try {

                String qualifiedNameForSoftwareServer = event.getTableSource().getNetworkAddress().split(":")[0];
                InstanceProperties softwareServerProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSoftwareServer)
                        .withStringProperty(Constants.NAME, qualifiedNameForSoftwareServer)
                        .build();
                List<Classification> classificationList = new ArrayList();
                classificationList.add(entitiesCreatorHelper.buildClassification(Constants.DATABASE_SERVER, Constants.SOFTWARE_SERVER, new InstanceProperties()));
                EntityDetail softwareServerEntity = entitiesCreatorHelper.addEntity(Constants.SOFTWARE_SERVER,
                        qualifiedNameForSoftwareServer, softwareServerProperties, classificationList);


                String qualifiedNameForEndpoint = event.getTableSource().getProtocol() + event.getTableSource().getNetworkAddress() ;
                InstanceProperties endpointProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForEndpoint)
                        .withStringProperty(Constants.NAME, qualifiedNameForEndpoint)
                        .withStringProperty(Constants.NETWORK_ADDRESS, event.getTableSource().getNetworkAddress() )
                        .withStringProperty(Constants.PROTOCOL, event.getTableSource().getProtocol())
                        .build();
                EntityDetail endpointEntity = entitiesCreatorHelper.addEntity(Constants.ENDPOINT,
                                                                    qualifiedNameForEndpoint,
                                                                    endpointProperties);


                entitiesCreatorHelper.addRelationship(Constants.SERVER_ENDPOINT,
                        softwareServerEntity.getGUID(),
                        endpointEntity.getGUID(),
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());

                String qualifiedNameForConnection = qualifiedNameForEndpoint + "." + event.getTableSource().getUser();
                InstanceProperties connectionProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnection)
                        .withStringProperty(Constants.DESCRIPTION, "Connection to " + qualifiedNameForConnection)
                        .build();
                EntityDetail connectionEntity = entitiesCreatorHelper.addEntity(Constants.CONNECTION,
                        qualifiedNameForConnection, connectionProperties);


                entitiesCreatorHelper.addRelationship(Constants.CONNECTION_TO_ENDPOINT,
                        endpointEntity.getGUID(),
                        connectionEntity.getGUID(),
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());


                String qualifiedNameForConnectorType = event.getTableSource().getConnectorProviderName();
                InstanceProperties connectorTypeProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnectorType)
                        .withStringProperty(Constants.CONNECTOR_PROVIDER_CLASSNAME, event.getTableSource().getConnectorProviderName())
                        .build();
                EntityDetail connectorTypeEntity = entitiesCreatorHelper.addEntity(Constants.CONNECTOR_TYPE,
                        qualifiedNameForConnectorType, connectorTypeProperties);

                entitiesCreatorHelper.addRelationship(Constants.CONNECTION_CONNECTOR_TYPE,
                        connectionEntity.getGUID(),
                        connectorTypeEntity.getGUID(),
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());

                String qualifiedNameForDataStore = qualifiedNameForConnection + "." + event.getTableSource().getDatabaseName();
                InstanceProperties dataStoreProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataStore)
                        .withStringProperty(Constants.NAME, event.getTableSource().getDatabaseName())
                        .build();
                EntityDetail dataStore = entitiesCreatorHelper.addEntity(Constants.DATA_STORE,
                        qualifiedNameForDataStore, dataStoreProperties);


                entitiesCreatorHelper.addRelationship(Constants.CONNECTION_TO_ASSET,
                        connectionEntity.getGUID(),
                        dataStore.getGUID(),
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());


                String qualifiedNameForInformationView = qualifiedNameForDataStore + "." + event.getTableSource().getSchemaName();
                InstanceProperties ivProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForInformationView)
                        .withStringProperty(Constants.NAME, event.getTableSource().getSchemaName())
                        .withStringProperty(Constants.OWNER, "")
                        .withStringProperty(Constants.DESCRIPTION, "This asset is an " + "information " + "view")
                        .build();
                EntityDetail informationViewEntity = entitiesCreatorHelper.addEntity(Constants.INFORMATION_VIEW,
                        qualifiedNameForInformationView, ivProperties);

                entitiesCreatorHelper.addRelationship(Constants.DATA_CONTENT_FOR_DATASET,
                        dataStore.getGUID(),
                        informationViewEntity.getGUID(),
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());

                String qualifiedNameForDbSchemaType = qualifiedNameForInformationView +  Constants.TYPE_SUFFIX;
                InstanceProperties dbSchemaTypeProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDbSchemaType)
                        .withStringProperty(Constants.DISPLAY_NAME, event.getTableSource().getSchemaName() + Constants.TYPE_SUFFIX)
                        .withStringProperty(Constants.AUTHOR, "")
                        .withStringProperty(Constants.USAGE, "")
                        .withStringProperty(Constants.ENCODING_STANDARD, "").build();
                EntityDetail relationalDbSchemaType = entitiesCreatorHelper.addEntity(Constants.RELATIONAL_DB_SCHEMA_TYPE,
                        qualifiedNameForDbSchemaType,
                        dbSchemaTypeProperties);

                entitiesCreatorHelper.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                        informationViewEntity.getGUID(),
                        relationalDbSchemaType.getGUID(),
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());

                String qualifiedNameForTableType = qualifiedNameForInformationView + "." + event.getTableSource().getTableName() + Constants.TYPE_SUFFIX;
                InstanceProperties tableTypeProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTableType)
                        .withStringProperty(Constants.DISPLAY_NAME, event.getTableSource().getTableName() + Constants.TYPE_SUFFIX)
                        .withStringProperty(Constants.AUTHOR, "")
                        .withStringProperty(Constants.USAGE, "")
                        .withStringProperty(Constants.ENCODING_STANDARD, "")
                        .build();
                EntityDetail tableTypeEntity = entitiesCreatorHelper.addEntity(Constants.RELATIONAL_TABLE_TYPE,
                        qualifiedNameForTableType,
                        tableTypeProperties);

                String qualifiedNameForTable = qualifiedNameForInformationView + "." + event.getTableSource().getTableName();
                InstanceProperties tableProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTable)
                        .withStringProperty(Constants.ATTRIBUTE_NAME, event.getTableSource().getTableName())
                        .build();
                EntityDetail tableEntity = entitiesCreatorHelper.addEntity(Constants.RELATIONAL_TABLE,
                        qualifiedNameForTable,
                        tableProperties);

                entitiesCreatorHelper.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                        tableEntity.getGUID(),
                        tableTypeEntity.getGUID(),
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());
                entitiesCreatorHelper.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                        relationalDbSchemaType.getGUID(),
                        tableEntity.getGUID(),
                        Constants.INFORMATION_VIEW_OMAS_NAME,
                        new InstanceProperties());


                for (DerivedColumn derivedColumn : event.getDerivedColumns()) {

                    String qualifiedNameColumnType = qualifiedNameForTable + "." + derivedColumn.getColumnName() + Constants.TYPE_SUFFIX;
                    InstanceProperties columnTypeProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameColumnType)
                            .withStringProperty(Constants.DISPLAY_NAME, derivedColumn.getColumnName() + Constants.TYPE_SUFFIX)
                            .withStringProperty(Constants.AUTHOR, "")
                            .withStringProperty(Constants.USAGE, "")
                            .withStringProperty(Constants.ENCODING_STANDARD, "")
                            .withStringProperty(Constants.DATA_TYPE, derivedColumn.getType())
                            .build();
                    EntityDetail columnTypeEntity = entitiesCreatorHelper.addEntity(Constants.RELATIONAL_COLUMN_TYPE,
                            qualifiedNameColumnType,
                            columnTypeProperties);

                    String qualifiedNameForColumn = qualifiedNameForTable + "." + derivedColumn.getColumnName();
                    InstanceProperties columnProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                            .withStringProperty(Constants.ATTRIBUTE_NAME, derivedColumn.getColumnName())
                            .withStringProperty(Constants.FORMULA, "")
                            .withIntegerProperty(Constants.ELEMENT_POSITION_NAME, derivedColumn.getPosition())
                            .build();
                    EntityDetail derivedColumnEntity = entitiesCreatorHelper.addEntity(Constants.DERIVED_RELATIONAL_COLUMN,
                            qualifiedNameForColumn,
                            columnProperties);

                    InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUERY, "")
                            .build();
                    entitiesCreatorHelper.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                            derivedColumnEntity.getGUID(),
                            derivedColumn.getSourceColumn().getGuid(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            schemaQueryImplProperties);
                    entitiesCreatorHelper.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                            derivedColumnEntity.getGUID(),
                            columnTypeEntity.getGUID(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            new InstanceProperties());
                    entitiesCreatorHelper.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                            derivedColumnEntity.getGUID(),
                            derivedColumn.getSourceColumn().getBusinessTerm().getGuid(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            new InstanceProperties());
                    entitiesCreatorHelper.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                            tableTypeEntity.getGUID(),
                            derivedColumnEntity.getGUID(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            new InstanceProperties());

                }
            } catch (Exception e) {
                log.error("Exception processing event from in topic", e);
                InformationViewErrorCode auditCode = InformationViewErrorCode.PROCESS_EVENT_EXCEPTION;

                auditLog.logException("processEvent",
                        auditCode.getErrorMessageId(),
                        OMRSAuditLogRecordSeverity.EXCEPTION,
                        auditCode.getFormattedErrorMessage(eventAsString),
                        e.getMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction(),
                        e);
            }
        }
    }


}
