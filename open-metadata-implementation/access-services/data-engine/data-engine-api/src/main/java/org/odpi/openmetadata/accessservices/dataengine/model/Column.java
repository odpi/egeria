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
 * The Column object contains the name and the type of the column where a RelationshipColumn asset can be find in a database.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Column implements Serializable {
    private static final long serialVersionUID = 1L;
    private String qualifiedName;
    private String attributeName;
    private String cardinality;
    private int elementPosition;
    private DataType type;


    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
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

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Column{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", cardinality='" + cardinality + '\'' +
                ", elementPosition=" + elementPosition +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return elementPosition == column.elementPosition &&
                Objects.equals(qualifiedName, column.qualifiedName) &&
                Objects.equals(attributeName, column.attributeName) &&
                Objects.equals(cardinality, column.cardinality) &&
                type == column.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, attributeName, cardinality, elementPosition, type);
    }
}
