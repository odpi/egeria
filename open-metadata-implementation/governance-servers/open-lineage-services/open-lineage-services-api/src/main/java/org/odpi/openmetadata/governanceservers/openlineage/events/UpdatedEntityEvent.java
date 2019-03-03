/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
public class UpdatedEntityEvent extends OpenLineageHeader {

    private String guid;
    private InstanceType type;
    private InstanceProperties oldProperties;
    private InstanceProperties newProperties;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }


    public InstanceType getType() {
        return type;
    }

    public void setType(InstanceType type) {
        this.type = type;
    }

    public InstanceProperties getOldProperties() {
        return oldProperties;
    }

    public void setOldProperties(InstanceProperties oldProperties) {
        this.oldProperties = oldProperties;
    }

    public InstanceProperties getNewProperties() {
        return newProperties;
    }

    public void setNewProperties(InstanceProperties newProperties) {
        this.newProperties = newProperties;
    }

    @Override
    public String toString() {
        return "UpdatedEntity{" +
                "guid='" + guid + '\'' +
                ", type=" + type +
                ", oldProperties=" + oldProperties +
                ", newProperties=" + newProperties +
                '}';
    }
}
