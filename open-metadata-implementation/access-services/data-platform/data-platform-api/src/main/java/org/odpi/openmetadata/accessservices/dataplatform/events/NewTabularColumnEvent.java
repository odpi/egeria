/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.accessservices.dataplatform.properties.connection.DataPlatform;
import org.odpi.openmetadata.accessservices.dataplatform.properties.schema.TabularColumn;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
public class NewTabularColumnEvent extends DataPlatformEventHeader {

    private TabularColumn tabularColumn;
    private DataPlatform dataPlatform;

    public TabularColumn getTabularColumn() {
        return tabularColumn;
    }

    public void setTabularColumn(TabularColumn tabularColumn) {
        this.tabularColumn = tabularColumn;
    }

    public DataPlatform getDataPlatform() {
        return dataPlatform;
    }

    public void setDataPlatform(DataPlatform dataPlatform) {
        this.dataPlatform = dataPlatform;
    }

    @Override
    public String toString() {
        return "NewTabularColumnEvent{" +
                "tabularColumn=" + tabularColumn +
                ", dataPlatform=" + dataPlatform +
                "} " + super.toString();
    }
}
