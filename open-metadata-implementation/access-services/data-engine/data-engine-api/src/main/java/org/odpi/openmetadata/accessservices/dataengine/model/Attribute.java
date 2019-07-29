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

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attribute implements Serializable {
    private static final long serialVersionUID = 1L;
    private String qualifiedName;
    private String displayName;
    private String cardinality;
    private int elementPosition;
    private String defaultValueOverride;
    private String dataType;
    private String defaultValue;

    public Attribute() {
    }

    public Attribute(String qualifiedName, String displayName, String cardinality, int elementPosition,
                     String defaultValueOverride, String dataType, String defaultValue) {
        this.qualifiedName = qualifiedName;
        this.displayName = displayName;
        this.cardinality = cardinality;
        this.elementPosition = elementPosition;
        this.defaultValueOverride = defaultValueOverride;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    public int getElementPosition() {
        return elementPosition;
    }

    public void setElementPosition(int elementPosition) {
        this.elementPosition = elementPosition;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValueOverride() {
        return defaultValueOverride;
    }

    public void setDefaultValueOverride(String defaultValueOverride) {
        this.defaultValueOverride = defaultValueOverride;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", cardinality='" + cardinality + '\'' +
                ", elementPosition=" + elementPosition +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", dataType=" + dataType +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return elementPosition == attribute.elementPosition &&
                Objects.equals(qualifiedName, attribute.qualifiedName) &&
                Objects.equals(displayName, attribute.displayName) &&
                Objects.equals(cardinality, attribute.cardinality) &&
                Objects.equals(defaultValueOverride, attribute.defaultValueOverride) &&
                Objects.equals(dataType, attribute.dataType) &&
                Objects.equals(defaultValue, attribute.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, displayName, cardinality, elementPosition, defaultValueOverride,
                dataType, defaultValue);
    }
}
