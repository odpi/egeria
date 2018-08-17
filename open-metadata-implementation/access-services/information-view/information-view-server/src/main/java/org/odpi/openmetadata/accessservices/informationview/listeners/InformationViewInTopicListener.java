/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.informationview.events.DerivedColumnDetail;
import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InformationViewInTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(InformationViewInTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private EntitiesCreatorHelper entitiesCreatorHelper;
    private OMRSAuditLog auditLog;

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
                EntityDetail dataStore = entitiesCreatorHelper.getEntity(Constants.DATA_STORE,
                        event.getDatabaseQualifiedName());

                String qualifiedNameForInformationView = Constants.INFO_VIEW_PREFIX + event.getSchemaQualifiedName();
                InstanceProperties ivProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForInformationView)
                        .withStringProperty(Constants.NAME, Constants.INFO_VIEW_PREFIX + event.getSchemaName())
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

                String qualifiedNameForDbSchemaType = Constants.INFO_VIEW_PREFIX + event.getSchemaTypeQualifiedName();
                InstanceProperties dbSchemaTypeProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDbSchemaType)
                        .withStringProperty(Constants.DISPLAY_NAME, Constants.INFO_VIEW_PREFIX + event.getSchemaName() + Constants.TYPE_SUFFIX)
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

                String qualifiedNameForTableType = Constants.INFO_VIEW_PREFIX + event.getTableTypeQualifiedName();
                InstanceProperties tableTypeProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTableType)
                        .withStringProperty(Constants.DISPLAY_NAME, Constants.INFO_VIEW_PREFIX + event.getTableName() + Constants.TYPE_SUFFIX)
                        .withStringProperty(Constants.AUTHOR, "")
                        .withStringProperty(Constants.USAGE, "")
                        .withStringProperty(Constants.ENCODING_STANDARD, "")
                        .build();
                EntityDetail tableTypeEntity = entitiesCreatorHelper.addEntity(Constants.RELATIONAL_TABLE_TYPE,
                        qualifiedNameForTableType,
                        tableTypeProperties);

                String qualifiedNameForTable = Constants.INFO_VIEW_PREFIX + event.getTableQualifiedName();
                InstanceProperties tableProperties = new EntityPropertiesBuilder()
                        .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTable)
                        .withStringProperty(Constants.ATTRIBUTE_NAME, Constants.INFO_VIEW_PREFIX + event.getTableName())
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


                for (DerivedColumnDetail derivedColumn : event.getDerivedColumns()) {

                    String qualifiedNameColumnType = Constants.INFO_VIEW_PREFIX + derivedColumn.getRealColumn().getQualifiedNameColumnType();
                    InstanceProperties columnTypeProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameColumnType)
                            .withStringProperty(Constants.DISPLAY_NAME, Constants.INFO_VIEW_PREFIX + derivedColumn.getAttributeName() + Constants.TYPE_SUFFIX)
                            .withStringProperty(Constants.AUTHOR, "")
                            .withStringProperty(Constants.USAGE, "")
                            .withStringProperty(Constants.ENCODING_STANDARD, "")
                            .withStringProperty(Constants.DATA_TYPE, derivedColumn.getType())
                            .build();
                    EntityDetail columnTypeEntity = entitiesCreatorHelper.addEntity(Constants.RELATIONAL_COLUMN_TYPE,
                            qualifiedNameColumnType,
                            columnTypeProperties);

                    String qualifiedNameForColumn = EntityPropertiesUtils.getStringValueForProperty(columnTypeEntity.getProperties(), Constants.QUALIFIED_NAME) + derivedColumn.getAttributeName();
                    InstanceProperties columnProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                            .withStringProperty(Constants.ATTRIBUTE_NAME, derivedColumn.getAttributeName())
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
                            derivedColumn.getRealColumn().getGuid(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            schemaQueryImplProperties);
                    entitiesCreatorHelper.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                            derivedColumnEntity.getGUID(),
                            columnTypeEntity.getGUID(),
                            Constants.INFORMATION_VIEW_OMAS_NAME,
                            new InstanceProperties());
                    entitiesCreatorHelper.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                            derivedColumnEntity.getGUID(),
                            derivedColumn.getRealColumn().getBusinessTerm().getGuid(),
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
