/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    private static final long    serialVersionUID = 1L;

    String  resourceUse   = null;
    boolean watchResource = false;

    /**
     * Default constructor
     */
    public ResourceListProperties()
    {
        super();
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
            this.watchResource = template.getWatchResource();
        }
    }


    /**
     * Return the reason why the resource has been attached to the initiative.
     *
     * @return description
     */
    public String getResourceUse()
    {
        return resourceUse;
    }


    /**
     * Set up the reason why the resource has been attached to the initiative.
     *
     * @param resourceUse description
     */
    public void setResourceUse(String resourceUse)
    {
        this.resourceUse = resourceUse;
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
        return watchResource == that.watchResource && Objects.equals(resourceUse, that.resourceUse);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceUse, watchResource);
    }
}
