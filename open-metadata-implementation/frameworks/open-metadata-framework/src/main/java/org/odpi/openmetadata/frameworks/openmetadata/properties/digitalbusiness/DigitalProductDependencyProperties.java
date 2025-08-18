/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalProductDependencyProperties describes a dependency relationship between two digital products.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigitalProductDependencyProperties extends RelationshipBeanProperties
{
    private String  label   = null;
    private String description = null;


    /**
     * Default constructor
     */
    public DigitalProductDependencyProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public DigitalProductDependencyProperties(DigitalProductDependencyProperties template)
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
     * Set up the description for the dependency.
     *
     * @param description String name
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Returns the description for the dependency.
     *
     * @return String name
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DigitalProductDependencyProperties{" +
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
        if (! (objectToCompare instanceof DigitalProductDependencyProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
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
