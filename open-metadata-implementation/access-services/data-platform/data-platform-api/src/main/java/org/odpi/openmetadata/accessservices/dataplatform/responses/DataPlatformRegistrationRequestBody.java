/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.responses;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The request body of creating the Software Server Capability for data platform as the external source of metadata.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataPlatformRegistrationRequestBody extends DataPlatformOMASAPIRequestBody {

    @JsonProperty("dataPlatform")
    private SoftwareServerCapability softwareServerCapability;

    /**
     * Instantiates a new Registration request body.
     */
    public DataPlatformRegistrationRequestBody() {
    }

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
        DataPlatformRegistrationRequestBody that = (DataPlatformRegistrationRequestBody) o;
        return Objects.equals(softwareServerCapability, that.softwareServerCapability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(softwareServerCapability);
    }

    @Override
    public String toString() {
        return "DataPlatformRegistrationRequestBody{" +
                "softwareServerCapability=" + softwareServerCapability +
                "} " + super.toString();
    }
}
