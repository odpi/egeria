/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DataPlatform;
import org.odpi.openmetadata.accessservices.dataplatform.properties.TabularSchema;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The NewTabularSchemaEvent will create a new TabularSchema from a data platform as an external source.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
public class NewTabularSchemaEvent extends DataPlatformEventHeader {

    private TabularSchema tabularSchema;
    private DataPlatform dataPlatform;

    /**
     * Gets tabular schema.
     *
     * @return the tabular schema
     */
    public TabularSchema getTabularSchema() {
        return tabularSchema;
    }

    /**
     * Sets tabular schema.
     *
     * @param tabularSchema the tabular schema
     */
    public void setTabularSchema(TabularSchema tabularSchema) {
        this.tabularSchema = tabularSchema;
    }

    /**
     * Gets data platform.
     *
     * @return the data platform
     */
    public DataPlatform getDataPlatform() {
        return dataPlatform;
    }

    /**
     * Sets data platform.
     *
     * @param dataPlatform the data platform
     */
    public void setDataPlatform(DataPlatform dataPlatform) {
        this.dataPlatform = dataPlatform;
    }

    @Override
    public String toString() {
        return "NewTabularSchemaEvent{" +
                "tabularSchema=" + tabularSchema +
                ", dataPlatform=" + dataPlatform +
                "} " + super.toString();
    }
}
