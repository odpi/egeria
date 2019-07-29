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
public class DataViewColumn extends DataViewElement {

    private String regularAggregate;
    private String usage;
    private String expression;
    private String dataType;
    private String hidden;
    private List<BusinessTerm> businessTerms;
    private List<Source> sources;

    public String getRegularAggregate() {
        return regularAggregate;
    }

    public void setRegularAggregate(String regularAggregate) {
        this.regularAggregate = regularAggregate;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
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
                "regularAggregate='" + regularAggregate + '\'' +
                ", usage='" + usage + '\'' +
                ", expression='" + expression + '\'' +
                ", dataType='" + dataType + '\'' +
                ", hidden='" + hidden + '\'' +
                ", businessTerms=" + businessTerms +
                ", sources=" + sources +
                '}';
    }
}
