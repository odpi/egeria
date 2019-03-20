package org.odpi.openmetadata.accessservices.dataplatform.assets.datavirtualization;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

public class View {
    private EntityDetail viewEntity;

    public EntityDetail getViewEntity() {
        return viewEntity;
    }

    public void setViewEntity(EntityDetail viewEntity) {
        this.viewEntity = viewEntity;
    }
}
