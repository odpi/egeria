/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportColumn extends ReportElement {

    private String aggregation;
    private String formula;
    private List<Source> sources;
    private List<BusinessTerm> businessTerms;

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public List<BusinessTerm> getBusinessTerms() {
        return businessTerms;
    }

    public void setBusinessTerms(List<BusinessTerm> businessTerms) {
        this.businessTerms = businessTerms;
    }

    @Override
    public String toString() {
        return "{" +
                "aggregation='" + aggregation + '\'' +
                ", formula='" + formula + '\'' +
                ", sources=" + sources +
                ", businessTerms=" + businessTerms +
                ", name='" + name + '\'' +
                '}';
    }
}
