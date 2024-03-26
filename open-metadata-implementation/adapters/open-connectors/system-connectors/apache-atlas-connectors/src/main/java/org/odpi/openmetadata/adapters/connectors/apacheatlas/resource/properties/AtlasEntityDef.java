/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasEntityDef describes the type definition for an entity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasEntityDef extends AtlasStructDef
{
    public static final String OPTION_DISPLAY_TEXT_ATTRIBUTE = "displayTextAttribute";

    private Set<String> superTypes  = null;

    // this is a read-only field, any value provided during create & update operation is ignored
    // the value of this field is derived from 'superTypes' specified in all AtlasEntityDef
    private Set<String> subTypes = null;

    // this is a read-only field, any value provided during create & update operation is ignored
    // the value of this field is derived from all the relationshipDefs this entityType is referenced in
    private List<AtlasRelationshipAttributeDef> relationshipAttributeDefs;

    // this is a read-only field, any value provided during create & update operation is ignored
    // the value of this field is derived from all the businessMetadataDefs this entityType is referenced in
    private Map<String, List<AtlasAttributeDef>> businessAttributeDefs;

    public AtlasEntityDef()
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


    public List<AtlasRelationshipAttributeDef> getRelationshipAttributeDefs()
    {
        return relationshipAttributeDefs;
    }


    public void setRelationshipAttributeDefs(List<AtlasRelationshipAttributeDef> relationshipAttributeDefs)
    {
        this.relationshipAttributeDefs = relationshipAttributeDefs;
    }


    public Map<String, List<AtlasAttributeDef>> getBusinessAttributeDefs()
    {
        return businessAttributeDefs;
    }


    public void setBusinessAttributeDefs(Map<String, List<AtlasAttributeDef>> businessAttributeDefs)
    {
        this.businessAttributeDefs = businessAttributeDefs;
    }


    @Override
    public String toString()
    {
        return "AtlasEntityDef{" +
                       "superTypes=" + superTypes +
                       ", subTypes=" + subTypes +
                       ", relationshipAttributeDefs=" + relationshipAttributeDefs +
                       ", businessAttributeDefs=" + businessAttributeDefs +
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
