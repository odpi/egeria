/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SpecificationPropertyAssignmentProperties is a java bean used to associate an element with a valid value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SpecificationPropertyAssignmentProperties extends RelationshipBeanProperties
{
    private String propertyName = null;


    /**
     * Default constructor
     */
    public SpecificationPropertyAssignmentProperties()
    {
        super();
        super.typeName = OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public SpecificationPropertyAssignmentProperties(SpecificationPropertyAssignmentProperties template)
    {
        super(template);

        if (template != null)
        {
            propertyName = template.getPropertyName();
        }
    }


    /**
     * Returns the property name for the valid value that is used to look up the placeholder value.
     *
     * @return String name
     */
    public String getPropertyName()
    {
        return propertyName;
    }


    /**
     * Set up the property name for the valid value that is used to look up the placeholder value.
     *
     * @param propertyName String name
     */
    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SpecificationPropertyAssignmentProperties{" +
                "propertyName='" + propertyName + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SpecificationPropertyAssignmentProperties that = (SpecificationPropertyAssignmentProperties) objectToCompare;
        return Objects.equals(propertyName, that.propertyName);
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), propertyName);
    }
}
