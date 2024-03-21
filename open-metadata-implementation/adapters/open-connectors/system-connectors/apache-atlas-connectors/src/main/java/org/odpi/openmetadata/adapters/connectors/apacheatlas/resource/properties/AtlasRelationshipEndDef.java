/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasRelationshipEndDef describes the entity at one end of a relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasRelationshipEndDef
{
    /**
     * The type associated with the end.
     */
    private String type = null;
    /**
     * The name of the attribute for this end
     */
    private String name = null;

    /**
     * When set this indicates that this end is the container end
     */
    private boolean isContainer = false;
    /**
     * This is the cardinality of the end
     */
    private AtlasCardinality cardinality = null;
    /**
     * When set this indicates that this end is a legacy attribute
     */
    private boolean isLegacyAttribute = false;
    /**
     * Description of the end
     */
    private String description = null;


    public AtlasRelationshipEndDef()
    {
    }


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public boolean isContainer()
    {
        return isContainer;
    }


    public void setContainer(boolean container)
    {
        isContainer = container;
    }


    public AtlasCardinality getCardinality()
    {
        return cardinality;
    }


    public void setCardinality(AtlasCardinality cardinality)
    {
        this.cardinality = cardinality;
    }


    public boolean isLegacyAttribute()
    {
        return isLegacyAttribute;
    }


    public void setLegacyAttribute(boolean legacyAttribute)
    {
        isLegacyAttribute = legacyAttribute;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }
}
