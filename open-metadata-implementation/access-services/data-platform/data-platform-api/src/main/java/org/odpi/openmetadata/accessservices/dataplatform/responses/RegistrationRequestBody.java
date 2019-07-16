/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.responses;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformEventHeader;
import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Registration request body of Software Server Capability.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequestBody extends DataPlatformOMASAPIResponse {

    private SoftwareServerCapability softwareServerCapability;

    /**
     * Default constructor
     *
     * @param softwareServerCapability the software server capability
     */
    public RegistrationRequestBody(SoftwareServerCapability softwareServerCapability) {
        this.softwareServerCapability = softwareServerCapability;
    }

    /**
     * Copy/clone constructor
     *
     * @param template                 object to copy
     * @param softwareServerCapability the software server capability
     */
    public RegistrationRequestBody(DataPlatformOMASAPIResponse template, SoftwareServerCapability softwareServerCapability) {
        super(template);
        this.softwareServerCapability = softwareServerCapability;
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
    public String toString() {
        return "RegistrationRequestBody{" +
                "softwareServerCapability=" + softwareServerCapability +
                '}';
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), softwareServerCapability);
    }
}
