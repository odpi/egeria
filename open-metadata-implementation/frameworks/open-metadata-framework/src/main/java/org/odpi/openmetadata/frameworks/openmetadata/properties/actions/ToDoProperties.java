/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ToDoStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

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
    private String     name           = null;
    private String     description    = null;
    private String     toDoType       = null;
    private Date       creationTime   = null;
    private int        priority       = 0;
    private Date       dueTime        = null;
    private Date       lastReviewTime = null;
    private Date       completionTime = null;
    private ToDoStatus toDoStatus     = ToDoStatus.OPEN;



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
            name = template.getName();
            description = template.getDescription();
            toDoType = template.getToDoType();
            creationTime = template.getCreationTime();
            priority = template.getPriority();
            lastReviewTime = template.getLastReviewTime();
            dueTime = template.getDueTime();
            completionTime = template.getCompletionTime();
            toDoStatus     = template.getToDoStatus();
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
     * Return the
     * @return
     */
    public String getToDoType()
    {
        return toDoType;
    }

    public void setToDoType(String toDoType)
    {
        this.toDoType = toDoType;
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
     * Return the last time the action was reviewed for priority, ownership etc.
     *
     * @return date
     */
    public Date getLastReviewTime()
    {
        return lastReviewTime;
    }


    /**
     * Set up the last time the action was reviewed for priority, ownership etc.
     *
     * @param lastReviewTime data
     */
    public void setLastReviewTime(Date lastReviewTime)
    {
        this.lastReviewTime = lastReviewTime;
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
    public ToDoStatus getToDoStatus()
    {
        return toDoStatus;
    }


    /**
     * Set up the status of the action/to do.
     *
     * @param toDoStatus status enum
     */
    public void setToDoStatus(ToDoStatus toDoStatus)
    {
        this.toDoStatus = toDoStatus;
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
                       ", lastReviewTime=" + lastReviewTime +
                       ", dueTime=" + dueTime +
                       ", completionTime=" + completionTime +
                       ", status=" + toDoStatus +
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
                       Objects.equals(lastReviewTime, that.lastReviewTime) &&
                       Objects.equals(dueTime, that.dueTime) &&
                       Objects.equals(completionTime, that.completionTime) &&
                       toDoStatus == that.toDoStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getName(), getDescription(), getCreationTime(), getPriority(),
                            getLastReviewTime(), getDueTime(), getCompletionTime(),
                            getToDoStatus(), getExtendedProperties(), getAdditionalProperties());
    }
}
