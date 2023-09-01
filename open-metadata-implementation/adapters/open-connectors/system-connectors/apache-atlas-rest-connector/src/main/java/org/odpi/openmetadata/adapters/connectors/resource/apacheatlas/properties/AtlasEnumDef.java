/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasEnumDef describes the structure of an EnumDef.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasEnumDef extends AtlasTypeDefBase
{
    private List<AtlasElementDef> elementDefs  = null;
    private String                defaultValue = null;


    public AtlasEnumDef()
    {
    }


    public List<AtlasElementDef> getElementDefs()
    {
        return elementDefs;
    }


    public void setElementDefs(List<AtlasElementDef> elementDefs)
    {
        this.elementDefs = elementDefs;
    }


    public String getDefaultValue()
    {
        return defaultValue;
    }


    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    @Override
    public String toString()
    {
        return "AtlasEnumDef{" +
                       "elementDefs=" + elementDefs +
                       ", defaultValue='" + defaultValue + '\'' +
                       ", category=" + getCategory() +
                       ", guid='" + getGuid() + '\'' +
                       ", createdBy='" + getCreatedBy() + '\'' +
                       ", updateBy='" + getUpdateBy() + '\'' +
                       ", createTime=" + getCreateTime() +
                       ", updateTime=" + getUpdateTime() +
                       ", version=" + getVersion() +
                       ", name='" + getName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", typeVersion='" + getTypeVersion() + '\'' +
                       ", serviceType='" + getServiceType() + '\'' +
                       '}';
    }
}
