/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewColumn;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewElement;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewModel;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewModel;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
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

    protected DataViewBasicOperation(OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao, lookupHelper, helper, auditLog);
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
            if (element instanceof DataViewModel) {
                addDataViewModel(qualifiedNameForParent, parentGuid, (DataViewModel) element);
            } else if (element instanceof DataViewColumn) {
                addDataViewColumn(qualifiedNameForParent, parentGuid, (DataViewColumn) element);
            }
    }


    /**
     *
     * @param qualifiedNameForParent qualified name for the parent element
     * @param parentGuid guid of the parent element
     * @param DataViewModel current element
     */
    private void addDataViewModel(String qualifiedNameForParent, String parentGuid, DataViewModel DataViewModel) {

        String qualifiedNameForDataViewModel = QualifiedNameUtils.buildQualifiedName( qualifiedNameForParent, Constants.SCHEMA_ATTRIBUTE,  DataViewModel.getId());
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataViewModel)
                .withStringProperty(Constants.ATTRIBUTE_NAME, DataViewModel.getName())
                .withStringProperty(Constants.ID, DataViewModel.getId())
                .withStringProperty(Constants.COMMENT, DataViewModel.getComment())
                .withStringProperty(Constants.NATIVE_CLASS, DataViewModel.getNativeClass())
                .withStringProperty(Constants.DESCRIPTION, DataViewModel.getDescription())
                .build();
        EntityDetail DataViewModelEntity = createSchemaType(Constants.SCHEMA_ATTRIBUTE, qualifiedNameForDataViewModel,
                                                            sectionProperties, Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);

        String qualifiedNameForDataViewModelType = QualifiedNameUtils.buildQualifiedName( qualifiedNameForParent, Constants.COMPLEX_SCHEMA_TYPE,  DataViewModel.getId() + Constants.TYPE_SUFFIX);
        EntityDetail schemaTypeEntity = addSchemaType(qualifiedNameForDataViewModelType, DataViewModelEntity.getGUID(), Constants.COMPLEX_SCHEMA_TYPE, null);
        addElements(qualifiedNameForParent, schemaTypeEntity.getGUID(), DataViewModel.getElements());
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

        addSemanticAssignments(dataViewColumn.getBusinessTerms(), dataViewColumnEntity);
        addQueryTargets(dataViewColumn.getSources(), dataViewColumnEntity);

        InstanceProperties typeProperties = new EntityPropertiesBuilder()
                                                                    .withStringProperty(Constants.DATA_TYPE, dataViewColumn.getDataType())
                                                                    .build();
        String qualifiedNameForColumnType = QualifiedNameUtils.buildQualifiedName(parentQualifiedName, Constants.PRIMITIVE_SCHEMA_TYPE,dataViewColumn.getId() + Constants.TYPE_SUFFIX );
        addSchemaType(qualifiedNameForColumnType, dataViewColumnEntity.getGUID(), Constants.PRIMITIVE_SCHEMA_TYPE, typeProperties);

        return dataViewColumnEntity;
    }



}
