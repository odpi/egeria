/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.handlers;

import org.odpi.openmetadata.accessservices.dataplatform.beans.View;
import org.odpi.openmetadata.accessservices.dataplatform.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.dataplatform.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.dataplatform.events.NewViewEvent;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.accessservices.dataplatform.properties.BusinessTerm;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DerivedColumn;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.accessservices.dataplatform.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataplatform.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * The type View handler.
 */
public class ViewHandler implements Callable<View> {


    private static final Logger log = LoggerFactory.getLogger(ViewHandler.class);
    private NewViewEvent event;
    private OMEntityDao omEntityDao;
    private OMRSRepositoryHelper helper;
    private final OMRSAuditLog auditLog;

    /**
     * Instantiates a new View handler.
     *
     * @param event       the event
     * @param omEntityDao the om entity dao
     * @param helper      the helper
     */
    public ViewHandler(NewViewEvent event, OMEntityDao omEntityDao, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        this.event = event;
        this.omEntityDao = omEntityDao;
        this.helper = helper;
        this.auditLog = auditLog;
    }

    @Override
    public View call() throws Exception {

        View view = new View();

        if (event.getDerivedColumns() == null || event.getDerivedColumns().isEmpty()) {
            log.debug("Delete existing view as event received has no derived columns");
            deleteView(event);
        } else {
            String qualifiedNameForInformationView = QualifiedNameUtils.buildQualifiedNameForInformationView(event.getTableSource().getDatabaseSource().getEndpointSource().getNetworkAddress().split(":")[0], event.getTableSource().getDatabaseSource().getName(), event.getTableSource().getSchemaName());
            String qualifiedNameForTableType = QualifiedNameUtils.buildQualifiedName(qualifiedNameForInformationView, Constants.RELATIONAL_TABLE_TYPE, event.getTableSource().getName() + Constants.TYPE_SUFFIX);
            InstanceProperties tableTypeProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTableType)
                    .withStringProperty(Constants.DISPLAY_NAME, event.getTableSource().getName() + Constants.TYPE_SUFFIX)
                    .withStringProperty(Constants.AUTHOR, "")
                    .withStringProperty(Constants.USAGE, "")
                    .withStringProperty(Constants.ENCODING_STANDARD, "")
                    .build();
            OMEntityWrapper tableTypeEntityWrapper = omEntityDao.createOrUpdateEntity(Constants.RELATIONAL_TABLE_TYPE,
                    qualifiedNameForTableType,
                    tableTypeProperties,
                    null,
                    false,
                    false);

            String qualifiedNameForTable = QualifiedNameUtils.buildQualifiedName(qualifiedNameForInformationView, Constants.RELATIONAL_TABLE, event.getTableSource().getName());
            InstanceProperties tableProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTable)
                    .withStringProperty(Constants.ATTRIBUTE_NAME, event.getTableSource().getName())
                    .build();


            if (event.getOriginalTableSource() != null) {
                HashMap<String, String> prop = new HashMap<>();
                prop.put(Constants.DISPLAY_NAME, event.getOriginalTableSource().getName());
                tableProperties = helper.addStringMapPropertyToInstance(Constants.DATA_PLATFORM_OMAS_NAME, tableProperties, Constants.ADDITIONAL_PROPERTIES, prop, "ViewHandler.call");
            }

            EntityDetail tableEntity = omEntityDao.addEntity(Constants.RELATIONAL_TABLE,
                    qualifiedNameForTable,
                    tableProperties,
                    false);

            view.setViewEntity(tableEntity);

            EntityDetail tableTypeEntity = tableTypeEntityWrapper.getEntityDetail();
            addRelationship(tableEntity.getGUID(), tableTypeEntity.getGUID(), Constants.SCHEMA_ATTRIBUTE_TYPE);

            List<Relationship> columnsToBeDeleted = null;
            if (entityIsNotNew(tableTypeEntityWrapper)) {
                columnsToBeDeleted = omEntityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, tableTypeEntity.getGUID());
            }

            if (event.getDerivedColumns() != null && !event.getDerivedColumns().isEmpty()) {
                List<EntityDetail> derivedColumns = event.getDerivedColumns().parallelStream().map(c -> addDerivedColumn(tableTypeEntity, qualifiedNameForTable, c)).collect(Collectors.toList());
                List<String> newDerivedColumnsGuids = derivedColumns.stream().map(e -> e.getGUID()).collect(Collectors.toList());
                if (columnsToBeDeleted != null && !columnsToBeDeleted.isEmpty()) {
                    columnsToBeDeleted = columnsToBeDeleted.stream().filter(c -> !newDerivedColumnsGuids.contains(c.getEntityTwoProxy().getGUID())).collect(Collectors.toList());
                }
            }

            deleteColumns(columnsToBeDeleted);

        }
        return view;
    }

    private boolean entityIsNotNew(OMEntityWrapper tableTypeEntityWrapper) {
        return OMEntityWrapper.EntityStatus.EXISTING.equals(tableTypeEntityWrapper.getEntityStatus()) || OMEntityWrapper.EntityStatus.UPDATED.equals(tableTypeEntityWrapper.getEntityStatus());
    }


    private void deleteView(NewViewEvent event) throws UserNotAuthorizedException,
            EntityNotKnownException,
            EntityNotDeletedException,
            InvalidParameterException,
            RepositoryErrorException,
            FunctionNotSupportedException,
            TypeErrorException,
            PropertyErrorException,
            PagingErrorException {
        String qualifiedNameForInformationView = QualifiedNameUtils.buildQualifiedNameForInformationView(event.getTableSource().getDatabaseSource().getEndpointSource().getNetworkAddress().split(":")[0], event.getTableSource().getName(), event.getTableSource().getSchemaName());
        String qualifiedNameForTableType = QualifiedNameUtils.buildQualifiedName(qualifiedNameForInformationView, Constants.RELATIONAL_TABLE_TYPE, event.getTableSource().getName() + Constants.TYPE_SUFFIX);
        EntityDetail tableTypeEntity = omEntityDao.getEntity(Constants.RELATIONAL_TABLE_TYPE, qualifiedNameForTableType, false);

        if (tableTypeEntity != null) {
            List<Relationship> derivedColumns = omEntityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, tableTypeEntity.getGUID());
            deleteColumns(derivedColumns);
            omEntityDao.purgeEntity(tableTypeEntity);
        }

        String qualifiedNameForTable = QualifiedNameUtils.buildQualifiedName(qualifiedNameForInformationView, Constants.RELATIONAL_TABLE, event.getTableSource().getName());
        EntityDetail tableEntity = omEntityDao.getEntity(Constants.RELATIONAL_TABLE, qualifiedNameForTable, false);
        if (tableEntity != null) {
            omEntityDao.purgeEntity(tableEntity);
        }
    }

    private void deleteColumns(List<Relationship> columns) {
        if (columns != null && !columns.isEmpty()) {
            columns.parallelStream().forEach(c -> deleteEntity(c.getEntityTwoProxy()));
        }
    }

    private void deleteEntity(EntityProxy proxy) {
        try {
            omEntityDao.purgeEntity(proxy);
        } catch (RepositoryErrorException | UserNotAuthorizedException | InvalidParameterException | EntityNotKnownException | EntityNotDeletedException | FunctionNotSupportedException e) {
            log.debug(e.getMessage(), e);
        }
    }


    private EntityDetail addDerivedColumn(EntityDetail tableTypeEntity, String qualifiedNameForTable, DerivedColumn derivedColumn) {

        try {
            String qualifiedNameColumnType = QualifiedNameUtils.buildQualifiedName(qualifiedNameForTable, Constants.RELATIONAL_COLUMN_TYPE, derivedColumn.getName() + Constants.TYPE_SUFFIX);
            InstanceProperties columnTypeProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameColumnType)
                    .withStringProperty(Constants.DISPLAY_NAME, derivedColumn.getName() + Constants.TYPE_SUFFIX)
                    .withStringProperty(Constants.AUTHOR, "")
                    .withStringProperty(Constants.USAGE, "")
                    .withStringProperty(Constants.ENCODING_STANDARD, "")
                    .withStringProperty(Constants.DATA_TYPE, derivedColumn.getType())
                    .build();
            EntityDetail columnTypeEntity = omEntityDao.addEntity(Constants.RELATIONAL_COLUMN_TYPE,
                    qualifiedNameColumnType,
                    columnTypeProperties,
                    false);

            String qualifiedNameForColumn = QualifiedNameUtils.buildQualifiedName(qualifiedNameForTable, Constants.DERIVED_RELATIONAL_COLUMN, derivedColumn.getName());
            InstanceProperties columnProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                    .withStringProperty(Constants.ATTRIBUTE_NAME, derivedColumn.getName())
                    .withStringProperty(Constants.FORMULA, "")
                    .withIntegerProperty(Constants.ELEMENT_POSITION_NAME, derivedColumn.getPosition())
                    .build();
            EntityDetail derivedColumnEntity = omEntityDao.addEntity(Constants.DERIVED_RELATIONAL_COLUMN,
                    qualifiedNameForColumn,
                    columnProperties,
                    false);

            derivedColumn.setGuid(derivedColumnEntity.getGUID());

            addRelationship(tableTypeEntity.getGUID(), derivedColumnEntity.getGUID(), Constants.ATTRIBUTE_FOR_SCHEMA);
            addRelationship(derivedColumnEntity.getGUID(), derivedColumn.getSourceColumn().getGuid(), Constants.SCHEMA_QUERY_IMPLEMENTATION);
            addRelationship(derivedColumnEntity.getGUID(), columnTypeEntity.getGUID(), Constants.SCHEMA_ATTRIBUTE_TYPE);
            for(BusinessTerm businessTerm : derivedColumn.getSourceColumn().getBusinessTerms()){
                addRelationship(derivedColumnEntity.getGUID(), businessTerm.getGuid(), Constants.SEMANTIC_ASSIGNMENT);
            }
            return derivedColumnEntity;

        } catch (InvalidParameterException | PropertyErrorException | RepositoryErrorException | EntityNotKnownException | FunctionNotSupportedException | PagingErrorException | ClassificationErrorException | UserNotAuthorizedException | TypeErrorException | StatusNotSupportedException e) {

            DataPlatformErrorCode errorCode = DataPlatformErrorCode.ADD_ENTITY_EXCEPTION;

            auditLog.logException("addDerivedColumn",
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(derivedColumn.toString(), e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
            throw new RuntimeException("Exception creating derived column", e);
        }

    }

    private void addRelationship(String entityGuid1, String entityGuid2, String relationshipTypeName) {
        try {
            omEntityDao.addRelationship(relationshipTypeName,
                    entityGuid1,
                    entityGuid2,
                    new InstanceProperties());
        } catch (InvalidParameterException | TypeErrorException | PropertyErrorException | EntityNotKnownException | FunctionNotSupportedException | PagingErrorException | UserNotAuthorizedException | RepositoryErrorException | StatusNotSupportedException e) {

            DataPlatformErrorCode errorCode = DataPlatformErrorCode.ADD_RELATIONSHIP_EXCEPTION;

            auditLog.logException("addRelationship",
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(relationshipTypeName, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
            throw new RuntimeException(e);
        }
    }
}
