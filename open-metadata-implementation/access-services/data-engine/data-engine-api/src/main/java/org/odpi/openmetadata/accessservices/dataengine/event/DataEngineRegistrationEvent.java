/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Data engine registration event for registering external source as software server capability.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataEngineRegistrationEvent extends DataEngineEventHeader{

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
        DataEngineRegistrationEvent that = (DataEngineRegistrationEvent) o;
        return Objects.equals(softwareServerCapability, that.softwareServerCapability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(softwareServerCapability);
    }

    @Override
    public String toString() {
        return "DataEngineRegistrationEvent{" +
                "softwareServerCapability=" + softwareServerCapability +
                "} " + super.toString();
    }
}
