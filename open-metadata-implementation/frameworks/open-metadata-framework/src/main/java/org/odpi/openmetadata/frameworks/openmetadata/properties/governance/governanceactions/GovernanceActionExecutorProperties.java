/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionExecutorProperties identifies the properties of a GovernanceActionExecutor relationship.
 * This links a governance action type to the governance engine that it will call.
 * The properties describe the parameters used by the governance engine to select a governance service to call and to
 * call it with the appropriate request type, request parameters and action targets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionExecutorProperties extends RelationshipBeanProperties
{
    private String              requestType            = null;
    private Map<String, String> requestParameters      = null;
    private List<String>        requestParameterFilter = null;
    private Map<String, String> requestParameterMap    = null;
    private List<String>        actionTargetFilter     = null;
    private Map<String, String> actionTargetMap        = null;

    /**
     * Typical Constructor
     */
    public GovernanceActionExecutorProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public GovernanceActionExecutorProperties(GovernanceActionExecutorProperties template)
    {
        if (template != null)
        {
            requestType            = template.getRequestType();
            requestParameters      = template.getRequestParameters();
            requestParameterFilter = template.getRequestParameterFilter();
            requestParameterMap    = template.getRequestParameterMap();
            actionTargetFilter     = template.getActionTargetFilter();
            actionTargetMap        = template.getActionTargetMap();
        }
    }

    /**
     * Return the request type used to call the engine.
     *
     * @return string
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up   the request type used to call the engine.
     *
     * @param requestType string
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
    public Map<String, String> getRequestParameters()
    {
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

    public List<String> getRequestParameterFilter()
    {
        return requestParameterFilter;
    }

    public void setRequestParameterFilter(List<String> requestParameterFilter)
    {
        this.requestParameterFilter = requestParameterFilter;
    }

    public Map<String, String> getRequestParameterMap()
    {
        return requestParameterMap;
    }

    public void setRequestParameterMap(Map<String, String> requestParameterMap)
    {
        this.requestParameterMap = requestParameterMap;
    }

    public List<String> getActionTargetFilter()
    {
        return actionTargetFilter;
    }

    public void setActionTargetFilter(List<String> actionTargetFilter)
    {
        this.actionTargetFilter = actionTargetFilter;
    }

    public Map<String, String> getActionTargetMap()
    {
        return actionTargetMap;
    }

    public void setActionTargetMap(Map<String, String> actionTargetMap)
    {
        this.actionTargetMap = actionTargetMap;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceActionExecutorProperties{" +
                "requestType='" + requestType + '\'' +
                ", requestParameters=" + requestParameters +
                ", requestParameterFilter=" + requestParameterFilter +
                ", requestParameterMap=" + requestParameterMap +
                ", actionTargetFilter=" + actionTargetFilter +
                ", actionTargetMap=" + actionTargetMap +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GovernanceActionExecutorProperties that = (GovernanceActionExecutorProperties) objectToCompare;
        return Objects.equals(requestType, that.requestType) &&
                Objects.equals(requestParameters, that.requestParameters) &&
                Objects.equals(requestParameterFilter, that.requestParameterFilter) &&
                Objects.equals(requestParameterMap, that.requestParameterMap) &&
                Objects.equals(actionTargetFilter, that.actionTargetFilter) &&
                Objects.equals(actionTargetMap, that.actionTargetMap);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), requestType, requestParameters, requestParameterFilter, requestParameterMap, actionTargetFilter, actionTargetMap);
    }
}
