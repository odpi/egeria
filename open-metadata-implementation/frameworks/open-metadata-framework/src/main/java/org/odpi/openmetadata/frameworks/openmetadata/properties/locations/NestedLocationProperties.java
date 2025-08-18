/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.locations;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NestedLocationProperties describes the properties for the NestedLocation relationship between locations.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NestedLocationProperties extends RelationshipBeanProperties
{
    private String label       = null;
    private String description = null;


    /**
     * Default constructor
     */
    public NestedLocationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.NESTED_LOCATION_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NestedLocationProperties(NestedLocationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.label = template.getLabel();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the label.
     *
     * @return string
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set up the label.
     *
     * @param label string
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Return the reasons why the elements are related.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the reasons why the elements are related.
     *
     * @param description description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NestedLocationProperties{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
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
        NestedLocationProperties that = (NestedLocationProperties) objectToCompare;
        return Objects.equals(label, that.label) &&
                Objects.equals(description, that.description);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), label, description);
    }
}
