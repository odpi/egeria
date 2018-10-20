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
public class ReportColumn {


    private String attributeName;
    private String aggregation;
    private String sectionName;
    private String formula;
    private DatabaseColumnReference realColumn;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public DatabaseColumnReference getRealColumn() {
        return realColumn;
    }

    public void setRealColumn(DatabaseColumnReference realColumn) {
        this.realColumn = realColumn;
    }


    @Override
    public String toString() {
        return "ReportColumn{" +
                "attributeName='" + attributeName + '\'' +
                ", aggregation='" + aggregation + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", formula='" + formula + '\'' +
                ", realColumn=" + realColumn +
                '}';
    }
}
