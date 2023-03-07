/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The "To Do" describes an action - it may be assigned to a person role (see PersonRoleAction).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ToDoProperties extends ReferenceableProperties
{
    private static final long    serialVersionUID = 1L;

    private String     name           = null;
    private String     description    = null;
    private Date       creationTime   = null;
    private int        priority       = 0;
    private Date       dueTime        = null;
    private Date       completionTime = null;
    private ToDoStatus status         = null;



    /**
     * Default constructor
     */
    public ToDoProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ToDoProperties(ToDoProperties template)
    {
        super(template);

        if (template != null)
        {
            this.name = template.getName();
            this.description = template.getDescription();
            creationTime = template.getCreationTime();
            priority = template.getPriority();
            dueTime = template.getDueTime();
            completionTime = template.getCompletionTime();
            status = template.getStatus();
        }
    }


    /**
     * Return the name of the asset.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the asset.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the asset.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the asset.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the time that this action was created.
     *
     * @return date
     */
    public Date getCreationTime()
    {
        return creationTime;
    }


    /**
     * Set up the time that the action was created.
     *
     * @param creationTime data
     */
    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }


    /**
     * Return how important this is.
     *
     * @return int
     */
    public int getPriority()
    {
        return priority;
    }


    /**
     * Set up how important this is.
     *
     * @param priority integer
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    /**
     * Return when this needs to be completed by.
     *
     * @return date
     */
    public Date getDueTime()
    {
        return dueTime;
    }


    /**
     * Set up when this needs to be completed by.
     *
     * @param dueTime date
     */
    public void setDueTime(Date dueTime)
    {
        this.dueTime = dueTime;
    }


    /**
     * Return when this was completed - or null if still outstanding.
     *
     * @return date
     */
    public Date getCompletionTime()
    {
        return completionTime;
    }


    /**
     * Set up when this was completed - or null if still outstanding.
     *
     * @param completionTime date
     */
    public void setCompletionTime(Date completionTime)
    {
        this.completionTime = completionTime;
    }


    /**
     * Return the status of the action/to do.
     *
     * @return status enum
     */
    public ToDoStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the status of the action/to do.
     *
     * @param status status enum
     */
    public void setStatus(ToDoStatus status)
    {
        this.status = status;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ToDoProperties{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", creationTime=" + creationTime +
                       ", priority=" + priority +
                       ", dueTime=" + dueTime +
                       ", completionTime=" + completionTime +
                       ", status=" + status +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ToDoProperties that = (ToDoProperties) objectToCompare;
        return priority == that.priority &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(creationTime, that.creationTime) &&
                       Objects.equals(dueTime, that.dueTime) &&
                       Objects.equals(completionTime, that.completionTime) &&
                       status == that.status;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getName(), getDescription(), getCreationTime(), getPriority(), getDueTime(), getCompletionTime(),
                            getStatus(), getExtendedProperties(), getAdditionalProperties());
    }
}
