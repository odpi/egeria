/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.Map;
import java.util.Objects;

public class RangerTag extends RangerBaseObject {

    private Boolean isEnabled;
    private Short owner;
    private String type;
    private Map<String, String> attributes;
    private Map<String, Object> options;

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Short getOwner() {
        return owner;
    }

    public void setOwner(Short owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangerTag rangerTag = (RangerTag) o;
        return this.getGuid().equals(rangerTag.getGuid()) &&
                type.equals(rangerTag.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getGuid(), type);
    }

    @Override
    public String toString() {
        return "RangerTag{" +
                "isEnabled=" + isEnabled +
                ", owner=" + owner +
                ", type='" + type + '\'' +
                ", attributes=" + attributes +
                ", options=" + options +
                '}';
    }
}
