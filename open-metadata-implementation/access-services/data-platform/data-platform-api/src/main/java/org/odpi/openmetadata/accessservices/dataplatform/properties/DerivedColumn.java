/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class DerivedColumn extends TableColumn {

    private TableColumn sourceColumn;

    /**
     * Return the real column associated with the derived column
     *
     * @return real column linked to the derived column
     */
    public TableColumn getSourceColumn() {
        return sourceColumn;
    }

    /**
     * set up the real column linked to the derived column
     *
     * @param sourceColumn - real column associated to the derived column
     */
    public void setSourceColumn(TableColumn sourceColumn) {
        this.sourceColumn = sourceColumn;
    }


    @Override
    public String toString() {
        return "DerivedColumn{" +
                "name='" + getName() + '\'' +
                ", position=" + getPosition() +
                ", cardinality='" + getCardinality() + '\'' +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", type='" + getType() + '\'' +
                ", sourceColumn=" + sourceColumn +
                '}';
    }
}
