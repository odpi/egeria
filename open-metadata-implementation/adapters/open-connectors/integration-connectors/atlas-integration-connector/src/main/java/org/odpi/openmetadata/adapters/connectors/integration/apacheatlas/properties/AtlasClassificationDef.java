/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasClassificationDef describes a type of tag.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasClassificationDef extends AtlasStructDef
{
    private Set<String> entityTypes = null;

    private Set<String> superTypes  = null;

    // subTypes field below is derived from 'superTypes' specified in all AtlasClassificationDef
    // this value is ignored during create & update operations
    private Set<String> subTypes    = null;


    public AtlasClassificationDef()
    {
    }


    public Set<String> getSuperTypes()
    {
        return superTypes;
    }


    public void setSuperTypes(Set<String> superTypes)
    {
        this.superTypes = superTypes;
    }


    public Set<String> getSubTypes()
    {
        return subTypes;
    }


    public void setSubTypes(Set<String> subTypes)
    {
        this.subTypes = subTypes;
    }


    public Set<String> getEntityTypes()
    {
        return entityTypes;
    }


    public void setEntityTypes(Set<String> entityTypes)
    {
        this.entityTypes = entityTypes;
    }


    @Override
    public String toString()
    {
        return "AtlasClassificationDef{" +
                       "superTypes=" + superTypes +
                       ", subTypes=" + subTypes +
                       ", entityTypes=" + entityTypes +
                       ", attributeDefs=" + getAttributeDefs() +
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
