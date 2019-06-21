/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewColumn;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewElement;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewTable;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


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
            if (element instanceof DataViewTable) {
                addDataViewTable(qualifiedNameForParent, parentGuid, (DataViewTable) element);
            } else if (element instanceof DataViewColumn) {
                addDataViewColumn(qualifiedNameForParent, parentGuid, (DataViewColumn) element);
            }
    }


    /**
     *
     * @param qualifiedNameForParent qualified name for the parent element
     * @param parentGuid guid of the parent element
     * @param dataViewTable current element
     */
    private void addDataViewTable(String qualifiedNameForParent, String parentGuid, DataViewTable dataViewTable) {

        String qualifiedNameForDataViewTable = QualifiedNameUtils.buildQualifiedName( qualifiedNameForParent, Constants.SCHEMA_ATTRIBUTE,  dataViewTable.getId());
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataViewTable)
                .withStringProperty(Constants.ATTRIBUTE_NAME, dataViewTable.getName())
                .withStringProperty(Constants.ID, dataViewTable.getId())
                .withStringProperty(Constants.COMMENT, dataViewTable.getComment())
                .withStringProperty(Constants.NATIVE_CLASS, dataViewTable.getNativeClass())
                .withStringProperty(Constants.DESCRIPTION, dataViewTable.getDescription())
                .build();
        EntityDetail dataViewTableEntity = createSchemaType(Constants.SCHEMA_ATTRIBUTE, qualifiedNameForDataViewTable,
                                                            sectionProperties, Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);

        String qualifiedNameForDataViewTableType = QualifiedNameUtils.buildQualifiedName( qualifiedNameForParent, Constants.COMPLEX_SCHEMA_TYPE,  dataViewTable.getId() + Constants.TYPE_SUFFIX);
        EntityDetail schemaTypeEntity = addSchemaType(qualifiedNameForDataViewTableType, dataViewTableEntity.getGUID(), Constants.COMPLEX_SCHEMA_TYPE, null);
        addElements(qualifiedNameForParent, schemaTypeEntity.getGUID(), dataViewTable.getElements());
    }


    /**
     *
     * @param parentQualifiedName qualified name for the parent element
     * @param parentGuid guid of the parent element
     * @param dataViewColumn element to be created
     * @return
     */
    protected EntityDetail addDataViewColumn(String parentQualifiedName, String parentGuid, DataViewColumn dataViewColumn)  {

        String qualifiedNameForColumn = QualifiedNameUtils.buildQualifiedName(parentQualifiedName, Constants.DERIVED_SCHEMA_ATTRIBUTE,  dataViewColumn.getId());
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

        addBusinessTerm(dataViewColumnEntity.getGUID(), dataViewColumn.getColumnGuid());
        addQueryTarget(dataViewColumnEntity.getGUID(), dataViewColumn.getDataViewSource().getGuid(), "");

        InstanceProperties typeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.DATA_TYPE, dataViewColumn.getDataType())
                .build();
        String qualifiedNameForColumnType = QualifiedNameUtils.buildQualifiedName(parentQualifiedName, Constants.PRIMITIVE_SCHEMA_TYPE,dataViewColumn.getId() + Constants.TYPE_SUFFIX );
        addSchemaType(qualifiedNameForColumnType, dataViewColumnEntity.getGUID(), Constants.PRIMITIVE_SCHEMA_TYPE, typeProperties);

        return dataViewColumnEntity;
    }



}
