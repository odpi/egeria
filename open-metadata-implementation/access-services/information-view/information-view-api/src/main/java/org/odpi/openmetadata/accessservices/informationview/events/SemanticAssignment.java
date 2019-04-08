/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
public class SemanticAssignment extends InformationViewHeader{

    private DatabaseColumn databaseColumn;
    private BusinessTerm businessTerm;

    public BusinessTerm getBusinessTerm() {
        return businessTerm;
    }

    public void setBusinessTerm(BusinessTerm businessTerm) {
        this.businessTerm = businessTerm;
    }

    public DatabaseColumn getDatabaseColumn() {
        return databaseColumn;
    }

    public void setDatabaseColumn(DatabaseColumn databaseColumn) {
        this.databaseColumn = databaseColumn;
    }

    @Override
    public String toString() {
        return "SemanticAssignment{" +
                "businessTerm=" + businessTerm +
                ", databaseColumn=" + databaseColumn +
                '}';
    }
}
