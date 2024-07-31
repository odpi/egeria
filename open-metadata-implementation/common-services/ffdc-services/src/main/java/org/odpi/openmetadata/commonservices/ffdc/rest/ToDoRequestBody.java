/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.NewToDoActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ToDoRequestBody provides a structure for passing to do details over a REST API.
 * It is used for creating "to Do" elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ToDoRequestBody
{
    private List<NewToDoActionTargetProperties> newActionTargetProperties = null;
    private ToDoProperties                      properties                = null;
    private String                              originatorGUID            = null;
    private String                              actionSponsorGUID         = null;
    private String                              assignToActorGUID         = null;
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
            this.newActionTargetProperties = template.getNewActionTargetProperties();
            this.properties                = template.getProperties();
            this.originatorGUID            = template.getOriginatorGUID();
            this.actionSponsorGUID         = template.getActionSponsorGUID();
            this.assignToActorGUID         = template.getAssignToActorGUID();
        }
    }


    /**
     * Return any specific action targets for the to do.
     *
     * @return list
     */
    public List<NewToDoActionTargetProperties> getNewActionTargetProperties()
    {
        return newActionTargetProperties;
    }


    /**
     * Set up any specific action targets for the to do.
     *
     * @param newActionTargetProperties list
     */
    public void setNewActionTargetProperties(List<NewToDoActionTargetProperties> newActionTargetProperties)
    {
        this.newActionTargetProperties = newActionTargetProperties;
    }


    /**
     * Return the properties of the to do.
     *
     * @return properties
     */
    public ToDoProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the to do.
     *
     * @param properties properties
     */
    public void setProperties(ToDoProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the unique identifier of the element that originated the to do.
     *
     * @return guid
     */
    public String getOriginatorGUID()
    {
        return originatorGUID;
    }


    /**
     * Set up the unique identifier of the element that originated the to do.
     *
     * @param originatorGUID guid
     */
    public void setOriginatorGUID(String originatorGUID)
    {
        this.originatorGUID = originatorGUID;
    }


    /**
     * Return the unique identifier of the sponsor of the To Do.
     *
     * @return guid
     */
    public String getActionSponsorGUID()
    {
        return actionSponsorGUID;
    }


    /**
     * Set up the unique identifier of the sponsor of the To Do.
     *
     * @param actionSponsorGUID guid
     */
    public void setActionSponsorGUID(String actionSponsorGUID)
    {
        this.actionSponsorGUID = actionSponsorGUID;
    }


    /**
     * Return the unique identifier of the action assigned to perform the work.
     *
     * @return guid
     */
    public String getAssignToActorGUID()
    {
        return assignToActorGUID;
    }


    /**
     * Set up the unique identifier of the action assigned to perform the work.
     *
     * @param assignToActorGUID guid
     */
    public void setAssignToActorGUID(String assignToActorGUID)
    {
        this.assignToActorGUID = assignToActorGUID;
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
                "newActionTargetProperties=" + newActionTargetProperties +
                ", properties=" + properties +
                ", originatorGUID='" + originatorGUID + '\'' +
                ", actionSponsorGUID='" + actionSponsorGUID + '\'' +
                ", assignToActorGUID='" + assignToActorGUID + '\'' +
                "} " + super.toString();
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ToDoRequestBody that = (ToDoRequestBody) objectToCompare;
        return Objects.equals(newActionTargetProperties, that.newActionTargetProperties) &&
                       Objects.equals(properties, that.properties) &&
                Objects.equals(originatorGUID, that.originatorGUID) &&
                Objects.equals(actionSponsorGUID, that.actionSponsorGUID) &&
                Objects.equals(assignToActorGUID, that.assignToActorGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(newActionTargetProperties, properties, originatorGUID, actionSponsorGUID, assignToActorGUID);
    }
}
