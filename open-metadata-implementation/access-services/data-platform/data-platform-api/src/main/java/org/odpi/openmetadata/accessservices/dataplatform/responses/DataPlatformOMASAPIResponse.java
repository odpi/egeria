/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.responses;

import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Objects;

/**
 * The response of the Data platform contains the Software Server Capability created by the external source of metadata.
 */
public class DataPlatformOMASAPIResponse extends FFDCResponseBase {

    private static final long serialVersionUID = 1L;

    private SoftwareServerCapability softwareServerCapability;

    /**
     * Gets software server capability.
     *
     * @return the software server capability
     */
    public SoftwareServerCapability getSoftwareServerCapability() {
        return softwareServerCapability;
    }

    /**
     * Sets software server capability.
     *
     * @param softwareServerCapability the software server capability
     */
    public void setSoftwareServerCapability(SoftwareServerCapability softwareServerCapability) {
        this.softwareServerCapability = softwareServerCapability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DataPlatformOMASAPIResponse that = (DataPlatformOMASAPIResponse) o;
        return Objects.equals(softwareServerCapability, that.softwareServerCapability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), softwareServerCapability);
    }

    @Override
    public String toString() {
        return "DataPlatformOMASAPIResponse{" +
                "softwareServerCapability=" + softwareServerCapability +
                '}';
    }
}
