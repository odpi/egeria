/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DerivedColumnDetail {

    private String attributeName;
    private Integer position;
    private String cardinality;
    private String defaultValueOverride;
    private String type;
    private ColumnDetails realColumn;

    /**
     * Return the name of the derived column
     *
     * @return name of the derived column
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * set up the name of the derived column
     *
     * @param attributeName - name of the derived column
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * Return the position of the derived column
     *
     * @return position of the derived column
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * set up the position of the derived column
     *
     * @param position - position of the derived column
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Return the cardinality of the derived column
     *
     * @return cardinality of the derived column
     */
    public String getCardinality() {
        return cardinality;
    }

    /**
     * set up the cardinality of the derived column
     *
     * @param cardinality - cardinality of the derived column
     */
    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    /**
     * Return the default value of the derived column
     *
     * @return default value of the derived column
     */
    public String getDefaultValueOverride() {
        return defaultValueOverride;
    }

    /**
     * set up the default value of the derived column
     *
     * @param defaultValueOverride - name of the derived column
     */
    public void setDefaultValueOverride(String defaultValueOverride) {
        this.defaultValueOverride = defaultValueOverride;
    }

    /**
     * Return the real column associated with the derived column
     *
     * @return real column linked to the derived column
     */
    public ColumnDetails getRealColumn() {
        return realColumn;
    }

    /**
     * set up the real column linked to the derived column
     *
     * @param realColumn - real column associated to the derived column
     */
    public void setRealColumn(ColumnDetails realColumn) {
        this.realColumn = realColumn;
    }

    /**
     * Return the type of the derived column
     *
     * @return type of the derived column
     */
    public String getType() {
        return type;
    }

    /**
     * set up the type of the derived column
     *
     * @param type - type of the derived column
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DerivedColumnDetail{" +
                "attributeName='" + attributeName + '\'' +
                ", position=" + position +
                ", cardinality='" + cardinality + '\'' +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", type='" + type + '\'' +
                ", realColumn=" + realColumn +
                '}';
    }
}
