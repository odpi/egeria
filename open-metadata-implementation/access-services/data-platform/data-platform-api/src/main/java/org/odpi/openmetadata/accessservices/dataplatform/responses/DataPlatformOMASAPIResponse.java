/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.responses;

import org.odpi.openmetadata.accessservices.dataplatform.properties.DataPlatform;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Objects;

/**
 * The response of the Data platform contains the Software Server Capability created by the external source of metadata.
 */
@Deprecated
public class DataPlatformOMASAPIResponse extends FFDCResponseBase {

    private static final long serialVersionUID = 1L;

    private DataPlatform dataPlatformProperties;

    /**
     * Gets software server capability.
     *
     * @return the software server capability
     */
    public DataPlatform getDataPlatformProperties() {
        return dataPlatformProperties;
    }

    /**
     * Sets software server capability.
     *
     * @param dataPlatformProperties the software server capability
     */
    public void setDataPlatformProperties(DataPlatform dataPlatformProperties) {
        this.dataPlatformProperties = dataPlatformProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DataPlatformOMASAPIResponse that = (DataPlatformOMASAPIResponse) o;
        return Objects.equals(dataPlatformProperties, that.dataPlatformProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataPlatformProperties);
    }

    @Override
    public String toString() {
        return "DataPlatformOMASAPIResponse{" +
                "dataPlatformProperties=" + dataPlatformProperties +
                '}';
    }
}
