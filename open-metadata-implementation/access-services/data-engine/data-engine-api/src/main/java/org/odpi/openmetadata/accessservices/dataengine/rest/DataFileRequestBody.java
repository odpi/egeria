/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.TabularSchemaType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataFileRequestBody extends DataEngineOMASAPIRequestBody {

    private DataFile dataFile;
    private TabularSchemaType tabularSchemaType;
    private String externalSourceGuid;

    public DataFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public TabularSchemaType getTabularSchemaType() {
        return tabularSchemaType;
    }

    public void setTabularSchemaType(TabularSchemaType tabularSchemaType) {
        this.tabularSchemaType = tabularSchemaType;
    }

    public String getExternalSourceGuid() {
        return externalSourceGuid;
    }

    public void setExternalSourceGuid(String externalSourceGuid) {
        this.externalSourceGuid = externalSourceGuid;
    }

    @Override
    public String toString() {
        return "DataFileRequestBody{" +
                "dataFile=" + dataFile +
                "tabularSchemaType=" + tabularSchemaType +
                "externalSourceGuid=" + externalSourceGuid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFileRequestBody that = (DataFileRequestBody) o;
        return Objects.equals(dataFile, that.dataFile) &&
                Objects.equals(tabularSchemaType, that.tabularSchemaType) &&
                Objects.equals(externalSourceGuid, that.externalSourceGuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataFile, tabularSchemaType, externalSourceGuid);
    }
}
