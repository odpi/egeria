/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.resources;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ResourceListProperties provides a details of why an element providing resources (such as a community) has been attached to an initiative
 * such as a governance domain.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResourceListProperties extends RelationshipBeanProperties
{
    String              resourceUse          = null;
    String              displayName          = null;
    String              description          = null;
    Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public ResourceListProperties()
    {
        super();
        super.typeName = OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ResourceListProperties(ResourceListProperties template)
    {
        super(template);

        if (template != null)
        {
            this.resourceUse          = template.getResourceUse();
            this.displayName          = template.getDisplayName();
            this.description          = template.getDescription();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the identifier that describes the type of resource use. (Use ResourceUse enum in GAF).
     *
     * @return string
     */
    public String getResourceUse()
    {
        return resourceUse;
    }


    /**
     * Set up identifier that describes the type of resource use. (Use ResourceUse enum in GAF).
     *
     * @param resourceUse string
     */
    public void setResourceUse(String resourceUse)
    {
        this.resourceUse = resourceUse;
    }


    /**
     * Return the name of the resource use - used for command menus and buttons.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the name of the resource use - used for command menus and buttons.
     *
     * @param displayName name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of how the resource is used, or why it is useful.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of how the resource is used, or why it is useful.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return any additional properties that explains how to use the resource.
     *
     * @return map
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional properties that explains how to use the resource.
     *
     * @param additionalProperties map
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ResourceListProperties{" +
                "resourceUse='" + resourceUse + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ResourceListProperties that = (ResourceListProperties) objectToCompare;
        return Objects.equals(resourceUse, that.resourceUse) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceUse, displayName, description, additionalProperties);
    }
}
