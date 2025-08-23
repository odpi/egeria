/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionTypeRequestBody provides a structure for passing the properties for initiating a new instance
 * of an engine action from a governance action type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InitiateGovernanceActionTypeRequestBody
{
    private String                governanceActionTypeQualifiedName = null;
    private Map<String, String>   requestParameters                 = null;
    private List<String>          actionSourceGUIDs                 = null;
    private List<String>          actionCauseGUIDs                  = null;
    private List<NewActionTarget> actionTargets                     = null;
    private Date                  startDate                         = null;
    private String                originatorServiceName             = null;
    private String                originatorEngineName              = null;


    /**
     * Default constructor
     */
    public InitiateGovernanceActionTypeRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InitiateGovernanceActionTypeRequestBody(InitiateGovernanceActionTypeRequestBody template)
    {
        if (template != null)
        {
            governanceActionTypeQualifiedName = template.getGovernanceActionTypeQualifiedName();
            requestParameters                 = template.getRequestParameters();
            actionSourceGUIDs                 = template.getActionSourceGUIDs();
            actionCauseGUIDs                  = template.getActionCauseGUIDs();
            actionTargets                     = template.getActionTargets();
            startDate                         = template.getStartDate();
            originatorServiceName             = template.getOriginatorServiceName();
            originatorEngineName              = template.getOriginatorEngineName();
        }
    }


    /**
     * Return the qualified name of the GovernanceActionProcess that will act as a template for the engine actions that will
     * be created and run.
     *
     * @return string name
     */
    public String getGovernanceActionTypeQualifiedName()
    {
        return governanceActionTypeQualifiedName;
    }


    /**
     * Set up the qualified name of the GovernanceActionType that will act as a template for the engine actions that will
     * be created and run.
     *
     * @param governanceActionTypeQualifiedName string name
     */
    public void setGovernanceActionTypeQualifiedName(String governanceActionTypeQualifiedName)
    {
        this.governanceActionTypeQualifiedName = governanceActionTypeQualifiedName;
    }


    /**
     * Return the parameters to pass onto the governance service.
     *
     * @return map of properties
     */
    public Map<String, String> getRequestParameters()
    {
        if (requestParameters == null)
        {
            return null;
        }

        if (requestParameters.isEmpty())
        {
            return null;
        }

        return requestParameters;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param requestParameters map of properties
     */
    public void setRequestParameters(Map<String, String> requestParameters)
    {
        this.requestParameters = requestParameters;
    }


    /**
     * Return the list of elements that triggered this request.
     *
     * @return list of string guids
     */
    public List<String> getActionSourceGUIDs()
    {
        return actionSourceGUIDs;
    }


    /**
     * Set up the list of elements that triggered this request.
     *
     * @param actionSourceGUIDs list of string guids
     */
    public void setActionSourceGUIDs(List<String> actionSourceGUIDs)
    {
        this.actionSourceGUIDs = actionSourceGUIDs;
    }


    /**
     * Return the list of elements that caused this action.
     *
     * @return list of string guids
     */
    public List<String> getActionCauseGUIDs()
    {
        return actionCauseGUIDs;
    }


    /**
     * Set up the list of elements that caused this action.
     *
     * @param actionCauseGUIDs list of string guids
     */
    public void setActionCauseGUIDs(List<String> actionCauseGUIDs)
    {
        this.actionCauseGUIDs = actionCauseGUIDs;
    }


    /**
     * Return the list of elements that the governance service will work on.
     *
     * @return list of names to string guids
     */
    public List<NewActionTarget> getActionTargets()
    {
        return actionTargets;
    }


    /**
     * Set up the list of elements that the governance service will work on.
     *
     * @param actionTargets list of names to string guids
     */
    public void setActionTargets(List<NewActionTarget> actionTargets)
    {
        this.actionTargets = actionTargets;
    }


    /**
     * Return the time that this engine action should start (null means as soon as possible).
     *
     * @return date object
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the time that this engine action should start (null means as soon as possible).
     *
     * @param startDate date object
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the unique name of the service that created this request.
     *
     * @return string name
     */
    public String getOriginatorServiceName()
    {
        return originatorServiceName;
    }


    /**
     * Set up the unique name of the service that created this request.
     *
     * @param originatorServiceName string name
     */
    public void setOriginatorServiceName(String originatorServiceName)
    {
        this.originatorServiceName = originatorServiceName;
    }


    /**
     * Return the qualified name of the governance engine that originated this request (if any).
     *
     * @return string name
     */
    public String getOriginatorEngineName()
    {
        return originatorEngineName;
    }


    /**
     * Set up the qualified name of the governance engine that originated this request (if any).
     *
     * @param originatorEngineName string name
     */
    public void setOriginatorEngineName(String originatorEngineName)
    {
        this.originatorEngineName = originatorEngineName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "InitiateGovernanceActionTypeRequestBody{" +
                "governanceActionTypeQualifiedName='" + governanceActionTypeQualifiedName + '\'' +
                ", requestParameters=" + requestParameters +
                ", actionSourceGUIDs=" + actionSourceGUIDs +
                ", actionCauseGUIDs=" + actionCauseGUIDs +
                ", actionTargets=" + actionTargets +
                ", startDate=" + startDate +
                ", originatorServiceName='" + originatorServiceName + '\'' +
                ", originatorEngineName='" + originatorEngineName + '\'' +
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
        InitiateGovernanceActionTypeRequestBody that = (InitiateGovernanceActionTypeRequestBody) objectToCompare;
        return Objects.equals(governanceActionTypeQualifiedName, that.governanceActionTypeQualifiedName) &&
                Objects.equals(actionSourceGUIDs, that.actionSourceGUIDs) &&
                Objects.equals(actionCauseGUIDs, that.actionCauseGUIDs) &&
                Objects.equals(requestParameters, that.requestParameters) &&
                Objects.equals(actionTargets, that.actionTargets) &&
                Objects.equals(originatorServiceName, that.originatorServiceName) &&
                Objects.equals(originatorEngineName, that.originatorEngineName) &&
                Objects.equals(startDate, that.startDate);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceActionTypeQualifiedName, actionSourceGUIDs, actionCauseGUIDs, requestParameters, actionTargets, startDate,
                            originatorServiceName, originatorEngineName);
    }
}
