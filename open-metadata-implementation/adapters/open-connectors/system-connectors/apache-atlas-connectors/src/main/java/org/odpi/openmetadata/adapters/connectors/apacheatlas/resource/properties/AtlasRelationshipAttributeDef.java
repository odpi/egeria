/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasRelationshipAttributeDef describes an attribute introduced to an entity via a relationshipDef
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasRelationshipAttributeDef extends AtlasAttributeDef
{
    private String  relationshipTypeName;
    private boolean isLegacyAttribute;


    public AtlasRelationshipAttributeDef()
    {
    }


    public String getRelationshipTypeName()
    {
        return relationshipTypeName;
    }


    public void setRelationshipTypeName(String relationshipTypeName)
    {
        this.relationshipTypeName = relationshipTypeName;
    }


    public boolean isLegacyAttribute()
    {
        return isLegacyAttribute;
    }


    public void setLegacyAttribute(boolean legacyAttribute)
    {
        isLegacyAttribute = legacyAttribute;
    }


    @Override
    public String toString()
    {
        return "AtlasRelationshipAttributeDef{" +
                       "relationshipTypeName='" + relationshipTypeName + '\'' +
                       ", isLegacyAttribute=" + isLegacyAttribute +
                       ", legacyAttribute=" + isLegacyAttribute() +
                       ", name='" + getName() + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", optional=" + isOptional() +
                       ", cardinality=" + getCardinality() +
                       ", valuesMinCount=" + getValuesMinCount() +
                       ", valuesMaxCount=" + getValuesMaxCount() +
                       ", unique=" + isUnique() +
                       ", indexable=" + isIndexable() +
                       ", includeInNotification=" + isIncludeInNotification() +
                       ", defaultValue='" + getDefaultValue() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", searchWeight=" + getSearchWeight() +
                       ", indexType=" + getIndexType() +
                       ", constraints=" + getConstraints() +
                       ", options=" + getOptions() +
                       ", displayName='" + getDisplayName() + '\'' +
                       '}';
    }
}
