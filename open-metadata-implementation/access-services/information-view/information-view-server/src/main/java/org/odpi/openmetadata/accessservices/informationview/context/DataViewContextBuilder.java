/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.context;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DataView;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewColumn;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewElement;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewSource;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewModel;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler;
import org.odpi.openmetadata.accessservices.informationview.lookup.DataViewLookup;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DataViewContextBuilder extends ContextBuilder<DataViewElement>{

    private DataViewLookup dataViewLookup;

    public DataViewContextBuilder(OMRSRepositoryConnector enterpriseConnector, OMEntityDao entityDao, OMRSAuditLog auditLog) {
        super(enterpriseConnector, entityDao, auditLog);
        this.dataViewLookup = new DataViewLookup(enterpriseConnector, entityDao,null, auditLog);
    }


    /**
     *
     * @param dataViewId - id of the data view to retrieve
     * @return bean describing full structure of data view
     */
    public DataView retrieveDataView(String dataViewId) {
        DataViewSource source = new DataViewSource();
        source.setId(dataViewId);
        EntityDetail dataViewEntity = Optional.ofNullable(dataViewLookup.lookupEntity(source))
                                              .orElseThrow(() -> ExceptionHandler.buildEntityNotFoundException(Constants.ID, dataViewId, Constants.INFORMATION_VIEW, this.getClass().getName()));
        return buildDataView(dataViewEntity);
    }

    /**
     *
     * @param dataViewEntity - entity describing the top level properties of the view
     * @return
     */
    private DataView buildDataView(EntityDetail dataViewEntity) {
        DataView dataView = new DataView();
        dataView.setGuid(dataViewEntity.getGUID());
        dataView.setAuthor(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.AUTHOR, dataViewEntity.getProperties(), "retrieveDataView"));
        dataView.setLastModifier(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.LAST_MODIFIER, dataViewEntity.getProperties(), "retrieveDataView"));
        Date lastModifiedDate = enterpriseConnector.getRepositoryHelper().getDateProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.LAST_MODIFIED_TIME, dataViewEntity.getProperties(), "retrieveDataView");
        if(lastModifiedDate!=null) {
            dataView.setLastModifiedTime(lastModifiedDate.getTime());
        }
        Date createdTime = enterpriseConnector.getRepositoryHelper().getDateProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.CREATE_TIME, dataViewEntity.getProperties(), "retrieveDataView");
        if(createdTime!=null) {
            dataView.setCreatedTime(createdTime.getTime());
        }
        dataView.setId(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ID, dataViewEntity.getProperties(), "retrieveDataView"));
        dataView.setNativeClass(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NATIVE_CLASS, dataViewEntity.getProperties(), "retrieveDataView"));
        dataView.setEndpointAddress(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NETWORK_ADDRESS, dataViewEntity.getProperties(), "retrieveDataView"));
        dataView.setName(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, dataViewEntity.getProperties(), "retrieveDataView"));

        addDataViewStructure(dataView, dataViewEntity);

        return dataView;
    }

    /**
     *
     * @param dataView bean representing the data view
     * @param dataViewEntity entity describing the top level entity
     */
    private void addDataViewStructure(DataView dataView, EntityDetail dataViewEntity) {
        String dataViewGuid = dataViewEntity.getGUID();
        List<Relationship> relationships = getAssetSchemaTypeRelationships(dataViewGuid);
        if(!CollectionUtils.isEmpty(relationships)) {
            EntityProxy assetSchemaType = relationships.get(0).getEntityTwoProxy();
            dataView.setElements(getChildrenElements(assetSchemaType.getGUID()));
        }
    }


    /**
     *
     * @param entityDetail entity describing the data view element
     * @return
     */
    DataViewElement buildElement(EntityDetail entityDetail) {

        String methodName = "buildElement";
        if (entityDetail.getType().getTypeDefName().equals(Constants.SCHEMA_ATTRIBUTE)) {
            DataViewModel dataViewElement = new DataViewModel();
            dataViewElement.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.NAME, entityDetail.getProperties(), methodName));
            List<Relationship> schemaType = entityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE,
                                                                        entityDetail.getGUID());
            if (!CollectionUtils.isEmpty(schemaType)) {
                dataViewElement.setElements(getChildrenElements(schemaType.get(0).getEntityTwoProxy().getGUID()));
            }
            return dataViewElement;
        } else {
            DataViewColumn dataViewElement = new DataViewColumn();
            dataViewElement.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.NAME, entityDetail.getProperties(), methodName));
            dataViewElement.setUsage(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.USAGE, entityDetail.getProperties(), methodName));
            dataViewElement.setComment(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.COMMENT, entityDetail.getProperties(), methodName));
            dataViewElement.setRegularAggregate(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.AGGREGATING_FUNCTION, entityDetail.getProperties(), methodName));
            dataViewElement.setBusinessTerms(getAssignedBusinessTerms(entityDetail.getGUID()));
            dataViewElement.setSources(getSources(entityDetail.getGUID()));
            return dataViewElement;
        }
    }

    protected List<Source> getSources(String guid) {
        return null;
    }


}
