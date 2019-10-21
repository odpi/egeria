/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator.model;

/**
 * MappedColumn is used to find mapped columns which contain business term in a table
 */
public class MappedColumn {
    /**
     * business name assigned to the column
     */
    private String businessName;
    /**
     * data type of the column. The type sometimes is different from the original
     * data source. This information is necessary for creating Logical Table in Gaian.
     */
    private String type;
    /**
     * technical name of the column. It is the name stored in database.
     */
    private String technicalName;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }
}
