/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.views.beans;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

public class InformationViewAsset {

    private EntityDetail softwareServerEntity;
    private EntityDetail endpointProperties;
    private EntityDetail connectionEntity;
    private EntityDetail connectorTypeEntity;
    private EntityDetail dataStore;
    private EntityDetail informationViewEntity;
    private EntityDetail relationalDbSchemaType;

    public EntityDetail getSoftwareServerEntity() {
        return softwareServerEntity;
    }

    public void setSoftwareServerEntity(EntityDetail softwareServerEntity) {
        this.softwareServerEntity = softwareServerEntity;
    }

    public EntityDetail getEndpointProperties() {
        return endpointProperties;
    }

    public void setEndpointProperties(EntityDetail endpointProperties) {
        this.endpointProperties = endpointProperties;
    }

    public EntityDetail getConnectionEntity() {
        return connectionEntity;
    }

    public void setConnectionEntity(EntityDetail connectionEntity) {
        this.connectionEntity = connectionEntity;
    }

    public EntityDetail getConnectorTypeEntity() {
        return connectorTypeEntity;
    }

    public void setConnectorTypeEntity(EntityDetail connectorTypeEntity) {
        this.connectorTypeEntity = connectorTypeEntity;
    }

    public EntityDetail getDataStore() {
        return dataStore;
    }

    public void setDataStore(EntityDetail dataStore) {
        this.dataStore = dataStore;
    }

    public EntityDetail getInformationViewEntity() {
        return informationViewEntity;
    }

    public void setInformationViewEntity(EntityDetail informationViewEntity) {
        this.informationViewEntity = informationViewEntity;
    }

    public EntityDetail getRelationalDbSchemaType() {
        return relationalDbSchemaType;
    }

    public void setRelationalDbSchemaType(EntityDetail relationalDbSchemaType) {
        this.relationalDbSchemaType = relationalDbSchemaType;
    }


}
