/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ToDoRequestBody provides a structure for passing the properties for a new to do.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ToDoRequestBody implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String                qualifiedName         = null;
    private String                title                 = null;
    private String                instructions          = null;
    private String                category              = null;
    private int                   priority              = 0;
    private Date                  dueDate               = null;
    private Map<String, String>   additionalProperties  = null;
    private String                assignToQualifiedName = null;
    private String                causeGUID             = null;
    private List<NewActionTarget> actionTargets         = null;


    /**
     * Default constructor
     */
    public ToDoRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ToDoRequestBody(ToDoRequestBody template)
    {
        if (template != null)
        {
            qualifiedName = template.getQualifiedName();
            title = template.getTitle();
            instructions = template.getInstructions();
            category = template.getCategory();
            priority = template.getPriority();
            dueDate = template.getDueDate();
            additionalProperties = template.getAdditionalProperties();
            assignToQualifiedName = template.getAssignToQualifiedName();
            causeGUID = template.getCauseGUID();
            actionTargets = template.getActionTargets();
        }
    }


    /**
     * Return the unique name of the new to do.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name of the new to do.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the short description of the action to perform.  It could be used as the subject of an email or message.
     *
     * @return string text
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the short description of the action to perform.  It could be used as the subject of an email or message.
     *
     * @param title string text
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Return the instructions that describe the action to perform.
     *
     * @return text
     */
    public String getInstructions()
    {
        return instructions;
    }


    /**
     * Set up the instructions that describe the action to perform.
     *
     * @param instructions text
     */
    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }


    /**
     * Return a category of to dos (for example, "data error", "access request").
     *
     * @return name string
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * Set up a category of to dos (for example, "data error", "access request").
     *
     * @param category name string
     */
    public void setCategory(String category)
    {
        this.category = category;
    }


    /**
     * Return the identifier of the governance domain that this incident report belongs to (0=ALL/ANY).
     *
     * @return int
     */
    public int getPriority()
    {
        return priority;
    }


    /**
     * Set up the identifier of the governance domain that this incident report belongs to (0=ALL/ANY).
     *
     * @param priority int
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    /**
     * Return the date/time that to do should be completed.
     *
     * @return date/time
     */
    public Date getDueDate()
    {
        return dueDate;
    }


    /**
     * Set up the date/time that to do should be completed.
     *
     * @param dueDate date/time
     */
    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }


    /**
     * Return any additional properties.
     *
     * @return property map
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }

        if (additionalProperties.isEmpty())
        {
            return null;
        }

        return additionalProperties;
    }


    /**
     * Set up any additional properties.
     *
     * @param additionalProperties property map
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the unique name of the person that should perform the to do.
     *
     * @return string name
     */
    public String getAssignToQualifiedName()
    {
        return assignToQualifiedName;
    }


    /**
     * Set up the unique name of the person that should perform the to do.
     *
     * @param assignToQualifiedName string name
     */
    public void setAssignToQualifiedName(String assignToQualifiedName)
    {
        this.assignToQualifiedName = assignToQualifiedName;
    }


    /**
     * Return the unique identifier of the element that describes the rule, project that this is on behalf of.
     *
     * @return string guid
     */
    public String getCauseGUID()
    {
        return causeGUID;
    }


    /**
     * Set up the unique identifier of the element that describes the rule, project that this is on behalf of.
     *
     * @param causeGUID string guid
     */
    public void setCauseGUID(String causeGUID)
    {
        this.causeGUID = causeGUID;
    }


    /**
     * Return the list of elements that should be acted upon.
     *
     * @return list of action targets
     */
    public List<NewActionTarget> getActionTargets()
    {
        return actionTargets;
    }


    /**
     * Set up the list of elements that should be acted upon.
     *
     * @param actionTargets list of action targets
     */
    public void setActionTargets(List<NewActionTarget> actionTargets)
    {
        this.actionTargets = actionTargets;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ToDoRequestBody{" +
                       "qualifiedName='" + qualifiedName + '\'' +
                       ", title='" + title + '\'' +
                       ", instructions='" + instructions + '\'' +
                       ", category='" + category + '\'' +
                       ", priority=" + priority +
                       ", dueDate=" + dueDate +
                       ", additionalProperties=" + additionalProperties +
                       ", assignToQualifiedName='" + assignToQualifiedName + '\'' +
                       ", causeGUID='" + causeGUID + '\'' +
                       ", actionTargets=" + actionTargets +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof ToDoRequestBody))
        {
            return false;
        }
        ToDoRequestBody that = (ToDoRequestBody) objectToCompare;
        return priority == that.priority &&
                       Objects.equals(qualifiedName, that.qualifiedName) &&
                       Objects.equals(title, that.title) &&
                       Objects.equals(instructions, that.instructions) &&
                       Objects.equals(category, that.category) &&
                       Objects.equals(dueDate, that.dueDate) &&
                       Objects.equals(additionalProperties, that.additionalProperties) &&
                       Objects.equals(causeGUID, that.causeGUID) &&
                       Objects.equals(actionTargets, that.actionTargets) &&
                       Objects.equals(assignToQualifiedName, that.assignToQualifiedName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedName, title, instructions, category, priority, dueDate, additionalProperties, causeGUID, actionTargets, assignToQualifiedName);
    }
}
