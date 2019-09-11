/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.beans;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;

public class KeyspaceAsset {

    private EntityDetail deployedDatabaseSchemaEntity;
    private List<EntityDetail> tabularSchemaTypeEntityList;
    private EntityDetail endpointEntity;
    private EntityDetail connectionEntity;
    private EntityDetail connectorTypeEntity;
    private EntityDetail softwareServerEntity;
    private EntityDetail databaseEntity;

    public EntityDetail getEndpointEntity() {
        return endpointEntity;
    }

    public void setEndpointEntity(EntityDetail endpointEntity) {
        this.endpointEntity = endpointEntity;
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

    public EntityDetail getSoftwareServerEntity() {
        return softwareServerEntity;
    }

    public void setSoftwareServerEntity(EntityDetail softwareServerEntity) {
        this.softwareServerEntity = softwareServerEntity;
    }

    public EntityDetail getDatabaseEntity() {
        return databaseEntity;
    }

    public void setDatabaseEntity(EntityDetail databaseEntity) {
        this.databaseEntity = databaseEntity;
    }

    public EntityDetail getDeployedDatabaseSchemaEntity() {
        return deployedDatabaseSchemaEntity;
    }

    public void setDeployedDatabaseSchemaEntity(EntityDetail deployedDatabaseSchemaEntity) {
        this.deployedDatabaseSchemaEntity = deployedDatabaseSchemaEntity;
    }

    public List<EntityDetail> getTabularSchemaTypeEntityList() {
        return tabularSchemaTypeEntityList;
    }

    public void setTabularSchemaTypeEntityList(List<EntityDetail> tabularSchemaTypeEntityList) {
        this.tabularSchemaTypeEntityList = tabularSchemaTypeEntityList;
    }

    @Override
    public String toString() {
        return "KeyspaceAsset{" +
                "deployedDatabaseSchemaEntity=" + deployedDatabaseSchemaEntity +
                ", tabularSchemaTypeEntityList=" + tabularSchemaTypeEntityList +
                ", endpointEntity=" + endpointEntity +
                ", connectionEntity=" + connectionEntity +
                ", connectorTypeEntity=" + connectorTypeEntity +
                ", softwareServerEntity=" + softwareServerEntity +
                ", databaseEntity=" + databaseEntity +
                '}';
    }
}
