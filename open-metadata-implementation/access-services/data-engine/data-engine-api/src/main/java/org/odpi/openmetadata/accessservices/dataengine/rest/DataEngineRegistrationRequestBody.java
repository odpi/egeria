/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataEngineRegistrationRequestBody extends DataEngineOMASAPIRequestBody {
    @JsonProperty("dataEngine")
    private SoftwareServerCapability softwareServerCapability;

    public SoftwareServerCapability getSoftwareServerCapability() {
        return softwareServerCapability;
    }

    public void setSoftwareServerCapability(SoftwareServerCapability softwareServerCapability) {
        this.softwareServerCapability = softwareServerCapability;
    }

    @Override
    public String toString() {
        return "DataEngineRegistrationRequestBody{" +
                "softwareServerCapability=" + softwareServerCapability +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataEngineRegistrationRequestBody that = (DataEngineRegistrationRequestBody) o;
        return Objects.equals(softwareServerCapability, that.softwareServerCapability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(softwareServerCapability);
    }
}