/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssignmentScopeProperties provides a details of the scope of work/influence expected by the assigned actor(s).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssignmentScopeProperties extends RelationshipProperties
{
    private static final long    serialVersionUID = 1L;

    String assignmentType = null;
    String description    = null;

    /**
     * Default constructor
     */
    public AssignmentScopeProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssignmentScopeProperties(AssignmentScopeProperties template)
    {
        super(template);

        if (template != null)
        {
            this.assignmentType = template.getAssignmentType();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the expectations with respect to the scope.
     *
     * @return type of assignment
     */
    public String getAssignmentType()
    {
        return assignmentType;
    }


    /**
     * Set up the expectations with respect to the scope.
     *
     * @param assignmentType type of assignment
     */
    public void setAssignmentType(String assignmentType)
    {
        this.assignmentType = assignmentType;
    }


    /**
     * Return additional descriptive text.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up additional descriptive text.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssignmentScopeProperties{" +
                       "assignmentType='" + assignmentType + '\'' +
                       ", description='" + description + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
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
        AssignmentScopeProperties that = (AssignmentScopeProperties) objectToCompare;
        return Objects.equals(assignmentType, that.assignmentType) && Objects.equals(description, that.description);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assignmentType, description);
    }
}
