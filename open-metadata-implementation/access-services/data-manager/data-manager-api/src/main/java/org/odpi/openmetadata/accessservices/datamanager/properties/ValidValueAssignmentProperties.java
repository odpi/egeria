/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueAssignmentProperties describes the properties between a referenceable and its valid values.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueAssignmentProperties extends RelationshipProperties
{
    private static final long    serialVersionUID = 1L;

    private boolean              strictRequirement = true;


    /**
     * Default constructor
     */
    public ValidValueAssignmentProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueAssignmentProperties(ValidValueAssignmentProperties template)
    {
        super(template);

        if (template != null)
        {
            strictRequirement = template.getStrictRequirement();
        }
    }


    /**
     * Return the strict requirement flag.
     *
     * @return boolean
     */
    public boolean getStrictRequirement()
    {
        return strictRequirement;
    }


    /**
     * Set up the strict requirement flag.
     *
     * @param strictRequirement string type name
     */
    public void setStrictRequirement(boolean strictRequirement)
    {
        this.strictRequirement = strictRequirement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueAssignmentProperties{" + "strictRequirement=" + strictRequirement + '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ValidValueAssignmentProperties that = (ValidValueAssignmentProperties) objectToCompare;
        return strictRequirement == that.strictRequirement;
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(strictRequirement);
    }
}
