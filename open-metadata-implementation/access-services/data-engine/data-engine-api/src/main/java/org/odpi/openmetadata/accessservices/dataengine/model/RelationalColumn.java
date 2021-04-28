/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationalColumn extends Attribute {
     private String formula;

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RelationalColumn that = (RelationalColumn) o;
        return Objects.equals(formula, that.formula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), formula);
    }

    @Override
    public String toString() {
        return "RelationalColumn{" +
                ", displayName='" + getDisplayName() + '\'' +
                ", minCardinality='" + getMinCardinality() + '\'' +
                ", maxCardinality='" + getMaxCardinality() + '\'' +
                ", allowsDuplicateValues='" + getAllowsDuplicateValues() + '\'' +
                ", orderedValues='" + getOrderedValues() + '\'' +
                ", position='" + getPosition() + '\'' +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", dataType='" + getDataType() + '\'' +
                ", defaultValue='" + getDefaultValue() + '\'' +
                "formula='" + formula + '\'' +
                '}';
    }
}
