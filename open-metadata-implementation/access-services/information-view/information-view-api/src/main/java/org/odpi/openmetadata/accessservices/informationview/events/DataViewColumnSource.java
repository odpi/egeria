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
public class DataViewColumnSource extends DataViewSource {


    private String dataViewColumnGuid;
    private String dataViewColumnId;
    private String dataViewColumnName;


    public String getDataViewColumnGuid() {
        return dataViewColumnGuid;
    }

    public void setDataViewColumnGuid(String dataViewColumnGuid) {
        this.dataViewColumnGuid = dataViewColumnGuid;
    }

    public String getDataViewColumnId() {
        return dataViewColumnId;
    }

    public void setDataViewColumnId(String dataViewColumnId) {
        this.dataViewColumnId = dataViewColumnId;
    }

    public String getDataViewColumnName() {
        return dataViewColumnName;
    }

    public void setDataViewColumnName(String dataViewColumnName) {
        this.dataViewColumnName = dataViewColumnName;
    }


    @Override
    public String toString() {
        return "DataViewColumnSource{" +
                "dataViewColumnGuid='" + dataViewColumnGuid + '\'' +
                ", dataViewColumnId='" + dataViewColumnId + '\'' +
                ", dataViewColumnName='" + dataViewColumnName + '\'' +
                ", dataViewId='" + dataViewId + '\'' +
                ", dataViewName='" + dataViewName + '\'' +
                ", dataViewGuid='" + dataViewGuid + '\'' +
                '}';
    }



    @Override
    public String buildQualifiedName() {
        return this.getNetworkAddress() + "." + this.getDataViewColumnId();
    }

}
