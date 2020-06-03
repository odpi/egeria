/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.responses;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DataPlatform;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The request body of creating the Software Server Capability for data platform as the external source of metadata.
 */
@Deprecated
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataPlatformRegistrationRequestBody extends DataPlatformOMASAPIRequestBody {

    @JsonProperty("dataPlatform")
    private DataPlatform dataPlatformProperties;

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
        DataPlatformRegistrationRequestBody that = (DataPlatformRegistrationRequestBody) o;
        return Objects.equals(dataPlatformProperties, that.dataPlatformProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataPlatformProperties);
    }

    @Override
    public String toString() {
        return "DataPlatformRegistrationRequestBody{" +
                "dataPlatformProperties=" + dataPlatformProperties +
                "} " + super.toString();
    }
}
