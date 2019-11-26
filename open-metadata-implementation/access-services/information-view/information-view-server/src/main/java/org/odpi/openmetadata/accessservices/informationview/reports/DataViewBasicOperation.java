/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewColumn;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewElement;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewModel;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
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
     * @param userId id of user submitting the request
     * @param parentQualifiedName qualified name for the parent element
     * @param parentGuid guid of the parent element
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param dataViewElements the list of all nested elements
     */
    protected void addElements(String userId, String parentQualifiedName, String parentGuid, String registrationGuid,
                               String registrationQualifiedName, List<DataViewElement> dataViewElements) {
        if (dataViewElements == null || dataViewElements.isEmpty())
            return;
        dataViewElements.parallelStream().forEach(e -> addDataViewElement(userId, parentQualifiedName, parentGuid, registrationGuid, registrationQualifiedName, e));
    }


    /**
     * @param userId id of user submitting the request
     * @param qualifiedNameForParent qualified name for the parent element
     * @param parentGuid  guid of the parent element
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param element element to be added
     */
    public void addDataViewElement(String userId, String qualifiedNameForParent, String parentGuid,
                                   String registrationGuid, String registrationQualifiedName, DataViewElement element) {
            if (element instanceof DataViewModel) {
                addDataViewModel(userId, qualifiedNameForParent, registrationGuid, registrationQualifiedName, parentGuid, (DataViewModel) element);
            } else if (element instanceof DataViewColumn) {
                addDataViewColumn(userId, qualifiedNameForParent, parentGuid, registrationGuid, registrationQualifiedName, (DataViewColumn) element);
            }
    }


    /**
     * @param userId id of user submitting the request
     * @param qualifiedNameForParent qualified name for the parent element
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param parentGuid guid of the parent element
     * @param dataViewModel current element
     */
    private void addDataViewModel(String userId, String qualifiedNameForParent, String registrationGuid,
                                  String registrationQualifiedName, String parentGuid, DataViewModel dataViewModel) {

        String qualifiedNameForDataViewModel = QualifiedNameUtils.buildQualifiedName( qualifiedNameForParent, Constants.SCHEMA_ATTRIBUTE,  dataViewModel.getId());
        InstanceProperties sectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataViewModel)
                .withStringProperty(Constants.ATTRIBUTE_NAME, dataViewModel.getName())

                .withStringProperty(Constants.NATIVE_CLASS, dataViewModel.getNativeClass())
                .withStringProperty(Constants.DESCRIPTION, dataViewModel.getDescription())
                .build();
        InstanceProperties additionalInstanceProperties = new EntityPropertiesBuilder()
                                                        .withStringProperty(Constants.ID, dataViewModel.getId())
                                                        .withStringProperty(Constants.COMMENT, dataViewModel.getComment())
                                                        .build();
        MapPropertyValue additionalProperties= new MapPropertyValue();
        additionalProperties.setMapValues(additionalInstanceProperties);
        sectionProperties.setProperty(Constants.ADDITIONAL_PROPERTIES, additionalProperties);
        EntityDetail dataViewModelEntity = createSchemaType(userId, Constants.SCHEMA_ATTRIBUTE, qualifiedNameForDataViewModel,
                registrationGuid, registrationQualifiedName, sectionProperties, Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);

        String qualifiedNameForDataViewModelType = QualifiedNameUtils.buildQualifiedName( qualifiedNameForParent, Constants.COMPLEX_SCHEMA_TYPE,  dataViewModel.getId() + Constants.TYPE_SUFFIX);
        EntityDetail schemaTypeEntity = addSchemaType(userId, qualifiedNameForDataViewModelType, registrationGuid, registrationQualifiedName, dataViewModelEntity.getGUID(), Constants.COMPLEX_SCHEMA_TYPE, null);
        addElements(userId, qualifiedNameForParent, schemaTypeEntity.getGUID(), registrationGuid, registrationQualifiedName, dataViewModel.getElements());
    }


    /**
     *
     *
     * @param userId id of user submitting the request
     * @param parentQualifiedName qualified name for the parent element
     * @param parentGuid guid of the parent element
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param dataViewColumn element to be created
     * @return entity describing the column
     */
    protected EntityDetail addDataViewColumn(String userId, String parentQualifiedName, String parentGuid,
                                             String registrationGuid, String registrationQualifiedName, DataViewColumn dataViewColumn)  {

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

        EntityDetail dataViewColumnEntity = createSchemaType(userId, Constants.DERIVED_SCHEMA_ATTRIBUTE, qualifiedNameForColumn, registrationGuid, registrationQualifiedName, columnProperties, Constants.ATTRIBUTE_FOR_SCHEMA, parentGuid);

        addSemanticAssignments(userId, registrationGuid, registrationQualifiedName, dataViewColumn.getBusinessTerms(), dataViewColumnEntity);
        addQueryTargets(userId, registrationGuid, registrationQualifiedName, dataViewColumn.getSources(), dataViewColumnEntity);

        InstanceProperties typeProperties = new EntityPropertiesBuilder()
                                                                    .withStringProperty(Constants.DATA_TYPE, dataViewColumn.getDataType())
                                                                    .build();
        String qualifiedNameForColumnType = QualifiedNameUtils.buildQualifiedName(parentQualifiedName, Constants.PRIMITIVE_SCHEMA_TYPE,dataViewColumn.getId() + Constants.TYPE_SUFFIX );
        addSchemaType(userId, qualifiedNameForColumnType, registrationGuid, registrationQualifiedName, dataViewColumnEntity.getGUID(), Constants.PRIMITIVE_SCHEMA_TYPE, typeProperties);

        return dataViewColumnEntity;
    }



}
