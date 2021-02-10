/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Lineage mapping.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageMapping implements Serializable {
    private String sourceAttribute;
    private String targetAttribute;

    /**
     * Gets source attribute.
     *
     * @return the source attribute
     */
    public String getSourceAttribute() {
        return sourceAttribute;
    }

    /**
     * Sets source attribute.
     *
     * @param sourceAttribute the source attribute
     */
    public void setSourceAttribute(String sourceAttribute) {
        this.sourceAttribute = sourceAttribute;
    }

    /**
     * Gets target attribute.
     *
     * @return the target attribute
     */
    public String getTargetAttribute() {
        return targetAttribute;
    }

    /**
     * Sets target attribute.
     *
     * @param targetAttribute the target attribute
     */
    public void setTargetAttribute(String targetAttribute) {
        this.targetAttribute = targetAttribute;
    }

    @Override
    public String toString() {
        return "LineageMapping{" +
                "sourceAttribute='" + sourceAttribute + '\'' +
                ", targetAttribute='" + targetAttribute + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageMapping that = (LineageMapping) o;
        return Objects.equals(sourceAttribute, that.sourceAttribute) &&
                Objects.equals(targetAttribute, that.targetAttribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceAttribute, targetAttribute);
    }
}
