/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionRequestBody provides a structure for passing the properties for a new governance action.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String              qualifiedName = null;
    private int                 domainIdentifier = 0;
    private String              displayName = null;
    private String              description = null;
    private List<String>        requestSourceGUIDs = null;
    private List<String>        actionTargetGUIDs = null;
    private Date                startTime = null;
    private String              requestType = null;
    private Map<String, String> requestProperties = null;


    /**
     * Default constructor
     */
    public GovernanceActionRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionRequestBody(GovernanceActionRequestBody template)
    {
        if (template != null)
        {
            qualifiedName = template.getQualifiedName();
            domainIdentifier = template.getDomainIdentifier();
            displayName = template.getDisplayName();
            description = template.getDescription();
            requestSourceGUIDs = template.getRequestSourceGUIDs();
            actionTargetGUIDs = template.getActionTargetGUIDs();
            startTime = template.getStartTime();
            requestType = template.getRequestType();
            requestProperties = template.getRequestProperties();
        }
    }


    /**
     * Return the qualified name for this new governance action.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the qualified name of this new governance action.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the identifier of the governance domain that this action belongs to (0=ALL/ANY).
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this action belongs to (0=ALL/ANY).
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Return the display name for the governance action.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for the governance action.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the governance action.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the governance action.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the list of elements that triggered this request.
     *
     * @return list of string guids
     */
    public List<String> getRequestSourceGUIDs()
    {
        if (requestSourceGUIDs == null)
        {
            return null;
        }

        if (requestSourceGUIDs.isEmpty())
        {
            return null;
        }

        return requestSourceGUIDs;
    }


    /**
     * Set up the list of elements that triggered this request.
     *
     * @param requestSourceGUIDs list of string guids
     */
    public void setRequestSourceGUIDs(List<String> requestSourceGUIDs)
    {
        this.requestSourceGUIDs = requestSourceGUIDs;
    }


    /**
     * Return the list of elements that the governance action will work on.
     *
     * @return list of string guids
     */
    public List<String> getActionTargetGUIDs()
    {
        if (actionTargetGUIDs == null)
        {
            return null;
        }

        if (actionTargetGUIDs.isEmpty())
        {
            return null;
        }

        return actionTargetGUIDs;
    }


    /**
     * Set up the list of elements that the governance action will work on.
     *
     * @param actionTargetGUIDs list of string guids
     */
    public void setActionTargetGUIDs(List<String> actionTargetGUIDs)
    {
        this.actionTargetGUIDs = actionTargetGUIDs;
    }


    /**
     * Return the time that this governance action should start (null means as soon as possible).
     *
     * @return date object
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the time that this governance action should start (null means as soon as possible).
     *
     * @param startTime date object
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    /**
     * Return the request type associated with this governance action.
     *
     * @return string name
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the request type associated with this governance action, used to identify ths governance service to run.
     *
     * @param requestType string name
     */
    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }


    /**
     * Return the parameters to pass onto the governance service.
     *
     * @return map of properties
     */
    public Map<String, String> getRequestProperties()
    {
        if (requestProperties == null)
        {
            return null;
        }

        if (requestProperties.isEmpty())
        {
            return null;
        }

        return requestProperties;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param requestProperties map of properties
     */
    public void setRequestProperties(Map<String, String> requestProperties)
    {
        this.requestProperties = requestProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceActionRequestBody{" +
                       "qualifiedName='" + qualifiedName + '\'' +
                       ", domainIdentifier=" + domainIdentifier +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", requestSourceGUIDs=" + requestSourceGUIDs +
                       ", actionTargetGUIDs=" + actionTargetGUIDs +
                       ", startTime=" + startTime +
                       ", requestType='" + requestType + '\'' +
                       ", requestProperties=" + requestProperties +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        GovernanceActionRequestBody that = (GovernanceActionRequestBody) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(qualifiedName, that.qualifiedName) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(requestSourceGUIDs, that.requestSourceGUIDs) &&
                       Objects.equals(actionTargetGUIDs, that.actionTargetGUIDs) &&
                       Objects.equals(startTime, that.startTime) &&
                       Objects.equals(requestType, that.requestType) &&
                       Objects.equals(requestProperties, that.requestProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedName, domainIdentifier, displayName, description, requestSourceGUIDs, actionTargetGUIDs, startTime, requestType,
                            requestProperties);
    }
}
