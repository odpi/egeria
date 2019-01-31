/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.contentmanager;

import org.odpi.openmetadata.accessservices.informationview.events.DataViewColumn;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewElement;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewTable;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static javax.swing.UIManager.put;

public abstract class DataViewBasicOperation {


    private static final Logger log = LoggerFactory.getLogger(DataViewBasicOperation.class);
    protected final EntitiesCreatorHelper entitiesCreatorHelper;
    protected final OMRSAuditLog auditLog;

    protected DataViewBasicOperation(EntitiesCreatorHelper entitiesCreatorHelper, OMRSAuditLog auditLog) {
        this.entitiesCreatorHelper = entitiesCreatorHelper;
        this.auditLog = auditLog;
    }


    protected void addElements(String parentQualifiedName, String parentGuid, List<DataViewElement> dataViewElements) {
        if (dataViewElements == null || dataViewElements.isEmpty())
            return;
        dataViewElements.stream().forEach(e -> addDataViewElement(parentQualifiedName, parentGuid, e));
    }


    public void addDataViewElement(String qualifiedNameForParent, String parentGuid, DataViewElement element) {
        try {
            if (element instanceof DataViewTable) {
                addDataViewTable(qualifiedNameForParent, parentGuid, (DataViewTable) element);
            } else if (element instanceof DataViewColumn) {
                addDataViewColumn(qualifiedNameForParent, parentGuid, (DataViewColumn) element);
            }
        } catch (Exception e) {
            log.error("Exception creating data view element", e);
            throw new RuntimeException("Unable to create Data View Element", e);//TODO throw specific exception
        }
    }


    private void addDataViewTable(String qualifiedNameForParent, String parentGuid, DataViewTable dataViewTable) throws InvalidParameterException, PropertyErrorException, TypeDefNotKnownException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException {

        String qualifiedNameForDataViewTable = qualifiedNameForParent + "." + dataViewTable.getName();
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataViewTable)
                .withStringProperty(Constants.ATTRIBUTE_NAME, dataViewTable.getName())
                .withStringProperty(Constants.ID, dataViewTable.getId())
                .withStringProperty(Constants.COMMENT, dataViewTable.getComment())
                .withStringProperty(Constants.NATIVE_CLASS, dataViewTable.getNativeClass())
                .withStringProperty(Constants.DESCRIPTION, dataViewTable.getDescription())
                .build();
        EntityDetail dataViewTableEntity = entitiesCreatorHelper.addEntity(Constants.DATA_VIEW_SCHEMA_ATTRIBUTE,
                qualifiedNameForDataViewTable,
                sectionProperties);


        entitiesCreatorHelper.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                parentGuid,
                dataViewTableEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());


        EntityDetail schemaTypeEntity = addSchemaType(qualifiedNameForDataViewTable, dataViewTableEntity, Constants.COMPLEX_SCHEMA_TYPE, null);
        addElements(qualifiedNameForDataViewTable, schemaTypeEntity.getGUID(), dataViewTable.getElements());
    }


    protected EntityDetail addDataViewColumn(String parentQualifiedName, String parentGuid, DataViewColumn dataViewColumn) throws InvalidParameterException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, RepositoryErrorException, StatusNotSupportedException {

        String qualifiedNameForColumn = parentQualifiedName + "." + dataViewColumn.getName();

        InstanceProperties columnProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                .withStringProperty(Constants.ATTRIBUTE_NAME, dataViewColumn.getName())
                //.withStringProperty(Constants.FORMULA, dataViewColumn.getRegularAggregate())
                .withStringProperty(Constants.DESCRIPTION, dataViewColumn.getDescription())
                .withStringProperty(Constants.COMMENT, dataViewColumn.getComment())
                .withStringProperty(Constants.FORMULA, dataViewColumn.getRegularAggregate())
                .withStringProperty(Constants.ID, dataViewColumn.getId())
                .withStringProperty(Constants.NATIVE_CLASS, dataViewColumn.getNativeClass())
                .build();
        EntityDetail dataViewColumnEntity = entitiesCreatorHelper.addEntity(Constants.DERIVED_DATA_VIEW_SCHEMA_ATTRIBUTE,
                qualifiedNameForColumn,
                columnProperties);

        entitiesCreatorHelper.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                parentGuid,
                dataViewColumnEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());

        addBusinessTerm(dataViewColumn, dataViewColumnEntity);
        addQueryTargets(dataViewColumn, dataViewColumnEntity);

        InstanceProperties typeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.DATA_TYPE, dataViewColumn.getDataType())
                .build();
        addSchemaType(qualifiedNameForColumn, dataViewColumnEntity, Constants.PRIMITIVE_SCHEMA_TYPE, typeProperties);

        return dataViewColumnEntity;
    }


    protected EntityDetail addSchemaType(String qualifiedNameOfSchemaAttribute, EntityDetail schemaAttributeEntity, String schemaAttributeType, InstanceProperties properties) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, ClassificationErrorException {
        String qualifiedNameForType = qualifiedNameOfSchemaAttribute + Constants.TYPE_SUFFIX;


        InstanceProperties typeProperties;
        if (properties != null) {
            Map<String, InstancePropertyValue> prop = properties.getInstanceProperties();
            prop.put(Constants.QUALIFIED_NAME, EntityPropertiesUtils.createPrimitiveStringPropertyValue(qualifiedNameForType));
            typeProperties = new InstanceProperties();
            typeProperties.setInstanceProperties(prop);
        } else {
            typeProperties = new EntityPropertiesBuilder()
                                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForType)
                                .build();
        }


        EntityDetail schemaTypeEntity = entitiesCreatorHelper.addEntity(schemaAttributeType,
                qualifiedNameForType,
                typeProperties);

        entitiesCreatorHelper.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                schemaAttributeEntity.getGUID(),
                schemaTypeEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());
        return schemaTypeEntity;
    }


    private void addBusinessTerm(DataViewColumn dataViewColumn, EntityDetail derivedColumnEntity) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, StatusNotSupportedException, TypeDefNotKnownException, EntityNotKnownException {
        String businessTermGuid = dataViewColumn.getBusinessTermGuid();
        if (!StringUtils.isEmpty(businessTermGuid)) {
            entitiesCreatorHelper.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                    derivedColumnEntity.getGUID(),
                    businessTermGuid,
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());
        }
    }

    private void addQueryTargets(DataViewColumn dataViewColumn, EntityDetail derivedColumnEntity) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException {
        String sourceColumnGUID = dataViewColumn.getColumnGuid();

        InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUERY, "")
                .build();
        try {
            entitiesCreatorHelper.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                    derivedColumnEntity.getGUID(),
                    sourceColumnGUID,
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    schemaQueryImplProperties);
        } catch (Exception e) {
            log.error("Exception linking source column", e);
        }

    }


}
