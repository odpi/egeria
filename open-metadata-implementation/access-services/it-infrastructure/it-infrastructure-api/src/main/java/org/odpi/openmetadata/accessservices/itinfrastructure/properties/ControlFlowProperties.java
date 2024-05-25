/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ControlFlowProperties describe the properties for a control flow relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ControlFlowProperties extends ConfigurationItemRelationshipProperties
{
    private String qualifiedName = null;
    private String description   = null;
    private String guard         = null;


    /**
     * Default constructor
     */
    public ControlFlowProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public ControlFlowProperties(ControlFlowProperties template)
    {
        if (template != null)
        {
            qualifiedName = template.getQualifiedName();
            description   = template.getDescription();
            guard         = template.getGuard();
        }
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored qualified name property for the relationship.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the description of the relationship.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the relationship.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the guard of the relationship.
     *
     * @return string guard
     */
    public String getGuard()
    {
        return guard;
    }


    /**
     * Set up the guard of the relationship.
     *
     * @param guard string name
     */
    public void setGuard(String guard)
    {
        this.guard = guard;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ControlFlowProperties{" +
                       "qualifiedName='" + qualifiedName + '\'' +
                       ", description='" + description + '\'' +
                       ", guard='" + guard + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
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
        ControlFlowProperties that = (ControlFlowProperties) objectToCompare;
        return Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getGuard(), that.getGuard());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedName, description, guard);
    }
}