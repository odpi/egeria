/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.resources;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
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
public class ResourceListProperties extends RelationshipProperties
{
    String              resourceUse            = null;
    String              resourceUseDescription = null;
    Map<String, String> resourceUseProperties  = null;
    boolean             watchResource          = false;

    /**
     * Default constructor
     */
    public ResourceListProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);
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
            this.resourceUse = template.getResourceUse();
            this.resourceUseDescription = template.getResourceUseDescription();
            this.resourceUseProperties = template.getResourceUseProperties();
            this.watchResource = template.getWatchResource();
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
     * Return the description of how the resource is used, or why it is useful.
     *
     * @return string
     */
    public String getResourceUseDescription()
    {
        return resourceUseDescription;
    }


    /**
     * Set up the description of how the resource is used, or why it is useful.
     *
     * @param resourceUseDescription string
     */
    public void setResourceUseDescription(String resourceUseDescription)
    {
        this.resourceUseDescription = resourceUseDescription;
    }


    /**
     * Return any additional properties that explains how to use the resource.
     *
     * @return map
     */
    public Map<String, String> getResourceUseProperties()
    {
        return resourceUseProperties;
    }


    /**
     * Set up any additional properties that explains how to use the resource.
     *
     * @param resourceUseProperties map
     */
    public void setResourceUseProperties(Map<String, String> resourceUseProperties)
    {
        this.resourceUseProperties = resourceUseProperties;
    }


    /**
     * Return whether changes to the resource result in notifications to the initiative.
     *
     * @return flag
     */
    public boolean getWatchResource()
    {
        return watchResource;
    }


    /**
     * Set up whether changes to the resource result in notifications to the initiative.
     *
     * @param watchResource flag
     */
    public void setWatchResource(boolean watchResource)
    {
        this.watchResource = watchResource;
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
                "resourceUseDescription='" + resourceUseDescription + '\'' +
                "resourceUseProperties='" + resourceUseProperties + '\'' +
                       ", watchResource=" + watchResource +
                       '}';
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
        return watchResource == that.watchResource &&
                Objects.equals(resourceUse, that.resourceUse) &&
                Objects.equals(resourceUseDescription, that.resourceUseDescription) &&
                Objects.equals(resourceUseProperties, that.resourceUseProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceUse, resourceUseDescription, resourceUseProperties, watchResource);
    }
}
