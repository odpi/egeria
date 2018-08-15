/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.omas.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DerivedColumnDetail stores basic information of a derived column
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DerivedColumnDetail {

    private String attributeName;
    private Integer position;
    private String cardinality;
    private String defaultValueOverride;
    private String type;
    private ColumnDetails realColumn;


    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    public String getDefaultValueOverride() {
        return defaultValueOverride;
    }

    public void setDefaultValueOverride(String defaultValueOverride) {
        this.defaultValueOverride = defaultValueOverride;
    }

    public ColumnDetails getRealColumn() {
        return realColumn;
    }

    public void setRealColumn(ColumnDetails realColumn) {
        this.realColumn = realColumn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
