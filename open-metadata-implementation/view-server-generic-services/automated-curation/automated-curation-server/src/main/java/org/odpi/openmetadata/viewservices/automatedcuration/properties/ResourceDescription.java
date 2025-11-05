/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResourceDescription extends RefDataElementBase
{
    String resourceUse = null;

    /**
     * Default constructor
     */
    public ResourceDescription()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ResourceDescription(ResourceDescription template)
    {
        super(template);

        if (template != null)
        {
            this.resourceUse = template.getResourceUse();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ResourceDescription(ResourceListProperties template)
    {
        if (template != null)
        {
            this.resourceUse          = template.getResourceUse();
            super.displayName         = template.getDisplayName();
            super.description         = template.getDescription();
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ResourceDescription{" +
                "resourceUse='" + resourceUse + '\'' +
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
        ResourceDescription that = (ResourceDescription) objectToCompare;
        return Objects.equals(resourceUse, that.resourceUse);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceUse);
    }
}
