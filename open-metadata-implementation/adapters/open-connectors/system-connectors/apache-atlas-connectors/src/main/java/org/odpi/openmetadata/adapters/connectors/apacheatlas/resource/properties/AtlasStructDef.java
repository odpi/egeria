/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasStructDef describes a struct type and also acts as a base class for most type defs.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasStructDef extends AtlasTypeDefBase
{
    private List<AtlasAttributeDef> attributeDefs = null;


    public AtlasStructDef()
    {
    }


    public List<AtlasAttributeDef> getAttributeDefs()
    {
        return attributeDefs;
    }


    public void setAttributeDefs(List<AtlasAttributeDef> attributeDefs)
    {
        this.attributeDefs = attributeDefs;
    }


    @Override
    public String toString()
    {
        return "AtlasStructDef{" +
                       "attributeDefs=" + attributeDefs +
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
