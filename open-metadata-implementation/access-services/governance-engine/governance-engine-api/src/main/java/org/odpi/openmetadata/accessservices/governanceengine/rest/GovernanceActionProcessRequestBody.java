/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

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
 * GovernanceActionProcessRequestBody provides a structure for passing the properties for initiating a new instance of a governance action process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                processQualifiedName  = null;
    private Map<String, String>   requestParameters     = null;
    private List<String>          requestSourceGUIDs    = null;
    private List<NewActionTarget> actionTargets         = null;
    private Date                  startTime             = null;
    private String                originatorServiceName = null;
    private String                originatorEngineName  = null;


    /**
     * Default constructor
     */
    public GovernanceActionProcessRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessRequestBody(GovernanceActionProcessRequestBody template)
    {
        if (template != null)
        {
            processQualifiedName = template.getProcessQualifiedName();
            requestParameters = template.getRequestParameters();
            requestSourceGUIDs = template.getRequestSourceGUIDs();
            actionTargets = template.getActionTargets();
            startTime = template.getStartTime();
            originatorServiceName = template.getOriginatorServiceName();
            originatorEngineName = template.getOriginatorEngineName();
        }
    }


    /**
     * Return the qualified name of the GovernanceActionProcess that will act as a template for the governance actions that will
     * be created and run.
     *
     * @return string name
     */
    public String getProcessQualifiedName()
    {
        return processQualifiedName;
    }


    /**
     * Set up the qualified name of the GovernanceActionProcess that will act as a template for the governance actions that will
     * be created and run.
     *
     * @param processQualifiedName string name
     */
    public void setProcessQualifiedName(String processQualifiedName)
    {
        this.processQualifiedName = processQualifiedName;
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
    public List<String> getRequestSourceGUIDs()
    {
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
     * Return the list of elements that the governance action process will work on.
     *
     * @return map of names to string guids
     */
    public List<NewActionTarget> getActionTargets()
    {
        return actionTargets;
    }


    /**
     * Set up the list of elements that the governance action process will work on.
     *
     * @param actionTargets map of names to string guids
     */
    public void setActionTargets(List<NewActionTarget> actionTargets)
    {
        this.actionTargets = actionTargets;
    }


    /**
     * Return the time that this process should start (null means as soon as possible).
     *
     * @return date object
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the time that this process should start (null means as soon as possible).
     *
     * @param startTime date object
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
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
        return "GovernanceActionProcessRequestBody{" +
                       "processQualifiedName='" + processQualifiedName + '\'' +
                       ", requestParameters=" + requestParameters +
                       ", requestSourceGUIDs=" + requestSourceGUIDs +
                       ", actionTargets=" + actionTargets +
                       ", startTime=" + startTime +
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
        GovernanceActionProcessRequestBody that = (GovernanceActionProcessRequestBody) objectToCompare;
        return Objects.equals(processQualifiedName, that.processQualifiedName) &&
                       Objects.equals(requestSourceGUIDs, that.requestSourceGUIDs) &&
                       Objects.equals(requestParameters, that.requestParameters) &&
                       Objects.equals(actionTargets, that.actionTargets) &&
                       Objects.equals(originatorServiceName, that.originatorServiceName) &&
                       Objects.equals(originatorEngineName, that.originatorEngineName) &&
                       Objects.equals(startTime, that.startTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(processQualifiedName, requestSourceGUIDs, requestParameters, actionTargets, startTime,
                            originatorServiceName, originatorEngineName);
    }
}
