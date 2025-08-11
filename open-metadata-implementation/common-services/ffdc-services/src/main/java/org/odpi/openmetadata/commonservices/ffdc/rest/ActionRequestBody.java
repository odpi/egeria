/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.AnchorOptions;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActionRequestBody provides a structure for passing action details over a REST API.
 * It is used for creating To Do, Meetings and engine action elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActionRequestBody extends AnchorOptions
{
    private List<NewActionTarget> newActionTargets = null;
    private ActionProperties      properties       = null;
    private String                originatorGUID   = null;
    private String                actionSponsorGUID = null;
    private String                assignToActorGUID = null;


    /**
     * Default constructor
     */
    public ActionRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActionRequestBody(ActionRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.newActionTargets  = template.getNewActionTargets();
            this.properties        = template.getProperties();
            this.originatorGUID    = template.getOriginatorGUID();
            this.actionSponsorGUID = template.getActionSponsorGUID();
            this.assignToActorGUID = template.getAssignToActorGUID();
        }
    }


    /**
     * Return any specific action targets for the action.
     *
     * @return list
     */
    public List<NewActionTarget> getNewActionTargets()
    {
        return newActionTargets;
    }


    /**
     * Set up any specific action targets for the action.
     *
     * @param newActionTargets list
     */
    public void setNewActionTargets(List<NewActionTarget> newActionTargets)
    {
        this.newActionTargets = newActionTargets;
    }


    /**
     * Return the properties of the action.
     *
     * @return properties
     */
    public ActionProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the action.
     *
     * @param properties properties
     */
    public void setProperties(ActionProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the unique identifier of the element that originated the action.
     *
     * @return guid
     */
    public String getOriginatorGUID()
    {
        return originatorGUID;
    }


    /**
     * Set up the unique identifier of the element that originated the action.
     *
     * @param originatorGUID guid
     */
    public void setOriginatorGUID(String originatorGUID)
    {
        this.originatorGUID = originatorGUID;
    }


    /**
     * Return the unique identifier of the sponsor of the action.
     *
     * @return guid
     */
    public String getActionSponsorGUID()
    {
        return actionSponsorGUID;
    }


    /**
     * Set up the unique identifier of the sponsor of the action.
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
        return "ActionRequestBody{" +
                "newActionTargetProperties=" + newActionTargets +
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
        ActionRequestBody that = (ActionRequestBody) objectToCompare;
        return Objects.equals(newActionTargets, that.newActionTargets) &&
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
        return Objects.hash(newActionTargets, properties, originatorGUID, actionSponsorGUID, assignToActorGUID);
    }
}
