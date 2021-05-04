/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import java.util.*;

/**
 * To Do describes an action - it may be assigned to a person role (see PersonRoleAction).
 */
public class ToDo extends ReferenceableHeader
{
    private static final long    serialVersionUID = 1L;

    private Date                creationTime         = null;
    private int                 priority             = 0;
    private Date                dueTime              = null;
    private Date                completionTime       = null;
    private ToDoStatus          status               = null;
    private Map<String, Object> extendedProperties   = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public ToDo()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ToDo(ToDo template)
    {
        super(template);

        if (template != null)
        {
            creationTime = template.getCreationTime();
            priority = template.getPriority();
            dueTime = template.getDueTime();
            completionTime = template.getCompletionTime();
            status = template.getStatus();
            extendedProperties = template.getExtendedProperties();
            additionalProperties = template.getAdditionalProperties();
        }
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
     * Return any properties associated with the subclass of this element.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setExtendedProperties(Map<String, Object> additionalProperties)
    {
        this.extendedProperties = additionalProperties;
    }


    /**
     * Return any additional properties associated with the element.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ToDo{" +
                "creationTime=" + creationTime +
                ", priority=" + priority +
                ", dueTime=" + dueTime +
                ", completionTime=" + completionTime +
                ", status=" + status +
                ", extendedProperties=" + extendedProperties +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
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
        ToDo toDo = (ToDo) objectToCompare;
        return getPriority() == toDo.getPriority() &&
                Objects.equals(getCreationTime(), toDo.getCreationTime()) &&
                Objects.equals(getDueTime(), toDo.getDueTime()) &&
                Objects.equals(getCompletionTime(), toDo.getCompletionTime()) &&
                getStatus() == toDo.getStatus() &&
                Objects.equals(getExtendedProperties(), toDo.getExtendedProperties()) &&
                Objects.equals(getAdditionalProperties(), toDo.getAdditionalProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getCreationTime(), getPriority(), getDueTime(), getCompletionTime(),
                            getStatus(), getExtendedProperties(), getAdditionalProperties());
    }
}
