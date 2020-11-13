/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;

import java.util.List;

/**
 * DataPlatform defines an endpoint and connectors for the runtime environment for a collection of data assets.
 */
@Deprecated
public class DataPlatform extends Source
{
    /*
     * Lists of objects that make up the infrastructure for the asset.
     */
    private String                 name = null;
    private Endpoint               dataPlatformEndpoint  = null;
    private List<ConnectorType>    dataPlatformConnectorType = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Endpoint getDataPlatformEndpoint() {
        return dataPlatformEndpoint;
    }

    public void setDataPlatformEndpoint(Endpoint dataPlatformEndpoint) {
        this.dataPlatformEndpoint = dataPlatformEndpoint;
    }

    public List<ConnectorType> getDataPlatformConnectorType() {
        return dataPlatformConnectorType;
    }

    public void setDataPlatformConnectorType(List<ConnectorType> dataPlatformConnectorType) {
        this.dataPlatformConnectorType = dataPlatformConnectorType;
    }

    @Override
    public String toString() {
        return "DataPlatform{" +
                "name='" + name + '\'' +
                ", dataPlatformEndpoint=" + dataPlatformEndpoint +
                ", dataPlatformConnectorType=" + dataPlatformConnectorType +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", guid='" + guid + '\'' +
                "} " + super.toString();
    }
}
