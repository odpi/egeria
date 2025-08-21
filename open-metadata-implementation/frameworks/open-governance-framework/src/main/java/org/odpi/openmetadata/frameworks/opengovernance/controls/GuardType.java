/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.controls;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GuardType describes a guard.  It is used in the connector provider of a governance service to help
 * tools understand the operations of a service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GuardType
{
    /**
     * Value to use for the guard.
     */
    private String guard = null;

    /**
     * Description of the guard.
     */
    private String description = null;

    /**
     * The typical completion status used with this guard.
     */
    private CompletionStatus completionStatus = null;

    /**
     * A map of additional property name to property value for this governance service.
     */
    private Map<String, String> otherPropertyValues = null;


    /**
     * Default constructor
     */
    public GuardType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GuardType(GuardType template)
    {
        if (template != null)
        {
            this.guard               = template.getGuard();
            this.description         = template.getDescription();
            this.completionStatus    = template.getCompletionStatus();
            this.otherPropertyValues = template.getOtherPropertyValues();
        }
    }


    /**
     * Return the string to use as the guard.
     *
     * @return string name
     */
    public String getGuard()
    {
        return guard;
    }


    /**
     * Set up the string to use as the guard.
     *
     * @param guard string name
     */
    public void setGuard(String guard)
    {
        this.guard = guard;
    }


    /**
     * Return the description of the action target.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the action target.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the completion status typically used with this guard.
     *
     * @return name of an open metadata type
     */
    public CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }


    /**
     * Set up the completion status typically used with this guard.
     *
     * @param completionStatus name of an open metadata type
     */
    public void setCompletionStatus(CompletionStatus completionStatus)
    {
        this.completionStatus = completionStatus;
    }


    /**
     * Return a map of property name to property value to provide additional information for this governance service.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Set up a map of property name to property value to provide additional information for this governance service..
     *
     * @param otherPropertyValues map of string to string
     */
    public void setOtherPropertyValues(Map<String, String> otherPropertyValues)
    {
        this.otherPropertyValues = otherPropertyValues;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GuardType{" +
                "guard='" + guard + '\'' +
                ", description='" + description + '\'' +
                ", completionStatus=" + completionStatus +
                ", otherPropertyValues=" + otherPropertyValues +
                '}';
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
        if (! (objectToCompare instanceof GuardType that))
        {
            return false;
        }
        return Objects.equals(completionStatus, that.completionStatus) &&
                Objects.equals(description, that.description) &&
                Objects.equals(guard, that.guard) &&
                Objects.equals(otherPropertyValues, that.otherPropertyValues);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(description, completionStatus, guard, otherPropertyValues);
    }
}
