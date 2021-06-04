/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Port.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Port extends Referenceable {
    private static final long serialVersionUID = 1L;
    private String displayName;
    @JsonProperty("type")
    private PortType portType;
    private UpdateSemantic updateSemantic;

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets display name.
     *
     * @param displayName the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets port type.
     *
     * @return the port type
     */
    public PortType getPortType() {
        return portType;
    }

    /**
     * Sets port type.
     *
     * @param portType the port type
     */
    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    /**
     * Gets update semantic.
     *
     * @return the update semantic
     */
    public UpdateSemantic getUpdateSemantic() {
        if (updateSemantic == null) {
            return UpdateSemantic.REPLACE;
        }

        return updateSemantic;
    }

    /**
     * Sets update semantic.
     *
     * @param updateSemantic the update semantic
     */
    public void setUpdateSemantic(UpdateSemantic updateSemantic) {
        this.updateSemantic = updateSemantic;
    }

    @Override
    public String toString() {
        return "Port{" +
                "displayName='" + displayName + '\'' +
                ", portType=" + portType +
                ", updateSemantic=" + updateSemantic +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Port port = (Port) o;
        return Objects.equals(displayName, port.displayName) &&
                portType == port.portType &&
                updateSemantic == port.updateSemantic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, portType, updateSemantic);
    }
}
