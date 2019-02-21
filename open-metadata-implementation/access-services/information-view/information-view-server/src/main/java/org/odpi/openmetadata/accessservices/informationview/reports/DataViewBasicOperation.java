/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class DataViewBasicOperation extends BasicOperation{


    private static final Logger log = LoggerFactory.getLogger(DataViewBasicOperation.class);

    protected DataViewBasicOperation(OMEntityDao omEntityDao, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao, helper, auditLog);
    }


    /**
     *
     * @param parentQualifiedName qualified name for the parent element
     * @param parentGuid guid of the parent element
     * @param dataViewElements the list of all nested elements
     */
    protected void addElements(String parentQualifiedName, String parentGuid, List<DataViewElement> dataViewElements) {
        if (dataViewElements == null || dataViewElements.isEmpty())
            return;
        dataViewElements.parallelStream().forEach(e -> addDataViewElement(parentQualifiedName, parentGuid, e));
    }


    /**
     *
     * @param qualifiedNameForParent qualified name for the parent element
     * @param parentGuid  guid of the parent element
     * @param element element to be added
     */
    public void addDataViewElement(String qualifiedNameForParent, String parentGuid, DataViewElement element) {
        try {
            if (element instanceof DataViewTable) {
                addDataViewTable(qualifiedNameForParent, parentGuid, (DataViewTable) element);
            } else if (element instanceof DataViewColumn) {
                addDataViewColumn(qualifiedNameForParent, parentGuid, (DataViewColumn) element);
            }
        } catch (Exception e) {
            log.error("Exception creating data view element", e);
            String message = MessageFormat.format("Unable to create Data View Element: {0} because of {1}", element.getId(), e.getMessage());
            throw new RuntimeException(message, e);//TODO throw specific exception
        }
    }


    /**
     *
     * @param qualifiedNameForParent qualified name for the parent element
     * @param parentGuid guid of the parent element
     * @param dataViewTable current element
     * @throws InvalidParameterException
     * @throws PropertyErrorException
     * @throws TypeDefNotKnownException
     * @throws RepositoryErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws StatusNotSupportedException
     */
    private void addDataViewTable(String qualifiedNameForParent, String parentGuid, DataViewTable dataViewTable) throws InvalidParameterException, PropertyErrorException, TypeDefNotKnownException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException {

        String normalizedId = dataViewTable.getId().replace(".", ":");
        normalizedId = normalizedId.replace("_", ":");
        String qualifiedNameForDataViewTable = qualifiedNameForParent + ":" + normalizedId;
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataViewTable)
                .withStringProperty(Constants.ATTRIBUTE_NAME, dataViewTable.getName())
                .withStringProperty(Constants.ID, dataViewTable.getId())
                .withStringProperty(Constants.COMMENT, dataViewTable.getComment())
                .withStringProperty(Constants.NATIVE_CLASS, dataViewTable.getNativeClass())
                .withStringProperty(Constants.DESCRIPTION, dataViewTable.getDescription())
                .build();
        EntityDetail dataViewTableEntity = createSchemaType(Constants.SCHEMA_ATTRIBUTE,
                qualifiedNameForDataViewTable, sectionProperties, Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);


        EntityDetail schemaTypeEntity = addSchemaType(qualifiedNameForDataViewTable, dataViewTableEntity, Constants.COMPLEX_SCHEMA_TYPE, null);
        addElements(qualifiedNameForParent, schemaTypeEntity.getGUID(), dataViewTable.getElements());
    }


    /**
     *
     * @param parentQualifiedName qualified name for the parent element
     * @param parentGuid guid of the parent element
     * @param dataViewColumn element to be created
     * @return
     * @throws InvalidParameterException
     * @throws TypeErrorException
     * @throws TypeDefNotKnownException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws StatusNotSupportedException
     */
    protected EntityDetail addDataViewColumn(String parentQualifiedName, String parentGuid, DataViewColumn dataViewColumn) throws InvalidParameterException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, RepositoryErrorException, StatusNotSupportedException {

        String normalizedId = dataViewColumn.getId().replace(".", ":");
        normalizedId = normalizedId.replace("_", ":");
        String qualifiedNameForColumn = parentQualifiedName + ":" + normalizedId;


        InstanceProperties columnProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                .withStringProperty(Constants.ATTRIBUTE_NAME, dataViewColumn.getName())
                .withStringProperty(Constants.DESCRIPTION, dataViewColumn.getDescription())
                .withStringProperty(Constants.COMMENT, dataViewColumn.getComment())
                .withStringProperty(Constants.FORMULA, dataViewColumn.getExpression())
                .withStringProperty(Constants.ID, dataViewColumn.getId())
                .withStringProperty(Constants.NATIVE_CLASS, dataViewColumn.getNativeClass())
                .withStringProperty(Constants.AGGREGATING_FUNCTION, dataViewColumn.getRegularAggregate())
                .build();

        EntityDetail dataViewColumnEntity = createSchemaType(Constants.DERIVED_SCHEMA_ATTRIBUTE, qualifiedNameForColumn, columnProperties, Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);

        addBusinessTerm(dataViewColumn, dataViewColumnEntity);
        addQueryTargets(dataViewColumn, dataViewColumnEntity);

        InstanceProperties typeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.DATA_TYPE, dataViewColumn.getDataType())
                .build();
        addSchemaType(qualifiedNameForColumn, dataViewColumnEntity, Constants.PRIMITIVE_SCHEMA_TYPE, typeProperties);

        return dataViewColumnEntity;
    }


    /**
     *
     * @param qualifiedNameOfSchemaAttribute qualified name of the schema attribute
     * @param schemaAttributeEntity schema attribute entity
     * @param schemaAttributeType schema attribute type entity
     * @param properties properties for the type entity
     * @return
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws TypeErrorException
     * @throws FunctionNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws TypeDefNotKnownException
     * @throws PagingErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws ClassificationErrorException
     */
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


        EntityDetail schemaTypeEntity = createSchemaType(schemaAttributeType, qualifiedNameForType, typeProperties,
                Constants.SCHEMA_ATTRIBUTE_TYPE, schemaAttributeEntity.getGUID());
        return schemaTypeEntity;
    }


    /**
     *
     * @param dataViewColumn element describing the data view column
     * @param derivedColumnEntity the entity representing the derived column
     * @throws UserNotAuthorizedException
     * @throws FunctionNotSupportedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     * @throws PagingErrorException
     * @throws StatusNotSupportedException
     * @throws TypeDefNotKnownException
     * @throws EntityNotKnownException
     */
    private void addBusinessTerm(DataViewColumn dataViewColumn, EntityDetail derivedColumnEntity) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, StatusNotSupportedException, TypeDefNotKnownException, EntityNotKnownException {
        String businessTermGuid = dataViewColumn.getBusinessTermGuid();
        if (!StringUtils.isEmpty(businessTermGuid)) {
            omEntityDao.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
                    derivedColumnEntity.getGUID(),
                    businessTermGuid,
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());
        }
    }

    /**
     *
     * @param dataViewColumn element describing the data view column
     * @param derivedColumnEntity the entity representing the derived column
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws TypeErrorException
     * @throws FunctionNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws TypeDefNotKnownException
     * @throws PagingErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     */
    private void addQueryTargets(DataViewColumn dataViewColumn, EntityDetail derivedColumnEntity) throws InvalidParameterException, StatusNotSupportedException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException {
        String sourceColumnGUID = dataViewColumn.getColumnGuid();

        InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUERY, "")
                .build();

            omEntityDao.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                    derivedColumnEntity.getGUID(),
                    sourceColumnGUID,
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    schemaQueryImplProperties);

    }


}
