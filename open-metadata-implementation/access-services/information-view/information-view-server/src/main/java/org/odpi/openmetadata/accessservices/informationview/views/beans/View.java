package org.odpi.openmetadata.accessservices.informationview.views.beans;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

public class View {

    private EntityDetail viewEntity;
//    private EntityDetail originalTableEntity;
//    private List<DerivedColumn> derivedColumnList;


    public EntityDetail getViewEntity() {
        return viewEntity;
    }

    public void setViewEntity(EntityDetail viewTableEntity) {
        this.viewEntity = viewTableEntity;
    }
}
