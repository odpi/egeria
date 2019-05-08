/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.views.beans;

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