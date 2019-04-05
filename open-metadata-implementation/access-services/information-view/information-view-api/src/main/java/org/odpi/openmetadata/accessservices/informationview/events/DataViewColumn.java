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
public class DataViewColumn extends DataViewElement {

    private String regularAggregate;
    private String usage;
    private String expression;
    private String dataType;
    private String hidden;
    private String columnGuid;
    private String businessTermGuid;
    private DataViewSource dataViewSource;

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


    public String getColumnGuid() {
        return columnGuid;
    }

    public void setColumnGuid(String columnGuid) {
        this.columnGuid = columnGuid;
    }

    public String getBusinessTermGuid() {
        return businessTermGuid;
    }

    public void setBusinessTermGuid(String businessTermGuid) {
        this.businessTermGuid = businessTermGuid;
    }

    public DataViewSource getDataViewSource() {
        return dataViewSource;
    }

    public void setDataViewSource(DataViewSource dataViewSource) {
        this.dataViewSource = dataViewSource;
    }


    @Override
    public String toString() {
        return "{" +
                "regularAggregate='" + regularAggregate + '\'' +
                ", usage='" + usage + '\'' +
                ", expression='" + expression + '\'' +
                ", dataType='" + dataType + '\'' +
                ", hidden='" + hidden + '\'' +
                ", columnGuid='" + columnGuid + '\'' +
                ", businessTermGuid='" + businessTermGuid + '\'' +
                ", dataViewSource=" + dataViewSource +
                '}';
    }
}
