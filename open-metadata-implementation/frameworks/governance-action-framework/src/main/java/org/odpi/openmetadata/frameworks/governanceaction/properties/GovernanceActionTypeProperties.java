/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.GuardType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestParameterType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionTypeProperties provides a structure for carrying the properties for a governance action type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionTypeProperties extends ReferenceableProperties
{
    private int                        domainIdentifier           = 0;
    private String                     displayName                = null;
    private String                     description                = null;
    private List<RequestParameterType> supportedRequestParameters = null;
    private List<ActionTargetType>     supportedActionTargetTypes = null;
    private List<RequestParameterType> producedRequestParameters  = null;
    private List<ActionTargetType>     producedActionTargetTypes  = null;
    private List<GuardType>            producedGuards             = null;
    private String                     governanceEngineGUID       = null;
    private String                     requestType                = null;
    private Map<String, String>        fixedRequestParameters     = null;
    private int                        waitTime                   = 0;


    /**
     * Default constructor
     */
    public GovernanceActionTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionTypeProperties(GovernanceActionTypeProperties template)
    {
        super (template);

        if (template != null)
        {
            domainIdentifier = template.getDomainIdentifier();
            displayName = template.getDisplayName();
            description = template.getDescription();

            supportedRequestParameters = template.getSupportedRequestParameters();
            supportedActionTargetTypes = template.getSupportedActionTargetTypes();
            producedRequestParameters = template.getProducedRequestParameters();
            producedActionTargetTypes = template.getProducedActionTargetTypes();
            producedGuards            = template.getProducedGuards();

            governanceEngineGUID = template.getGovernanceEngineGUID();
            requestType            = template.getRequestType();
            fixedRequestParameters = template.getFixedRequestParameters();

            waitTime               = template.getWaitTime();
        }
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
     * Return the required request parameters to supply when using this governance acton type to initiate an action.
     *
     * @return list
     */
    public List<RequestParameterType> getSupportedRequestParameters()
    {
        return supportedRequestParameters;
    }


    /**
     * Set up the required request parameters to supply when using this governance acton type to initiate an action.
     *
     * @param supportedRequestParameters list
     */
    public void setSupportedRequestParameters(List<RequestParameterType> supportedRequestParameters)
    {
        this.supportedRequestParameters = supportedRequestParameters;
    }


    /**
     * Return the required action targets to supply when using this governance acton type to initiate an action.
     *
     * @return list
     */
    public List<ActionTargetType> getSupportedActionTargetTypes()
    {
        return supportedActionTargetTypes;
    }


    /**
     * Set up the required action targets to supply when using this governance acton type to initiate an action.
     *
     * @param supportedActionTargetTypes list
     */
    public void setSupportedActionTargetTypes(List<ActionTargetType> supportedActionTargetTypes)
    {
        this.supportedActionTargetTypes = supportedActionTargetTypes;
    }


    /**
     * Return the request parameters that are produced by the governance service.
     *
     * @return list
     */
    public List<RequestParameterType> getProducedRequestParameters()
    {
        return producedRequestParameters;
    }


    /**
     * Set up the request parameters that are produced by the governance service.
     *
     * @param producedRequestParameters list
     */
    public void setProducedRequestParameters(List<RequestParameterType> producedRequestParameters)
    {
        this.producedRequestParameters = producedRequestParameters;
    }


    /**
     * Return the action targets that are produced by the governance service.
     *
     * @return list
     */
    public List<ActionTargetType> getProducedActionTargetTypes()
    {
        return producedActionTargetTypes;
    }


    /**
     * Set up the action targets that are produced by the governance service.
     *
     * @param producedActionTargetTypes list
     */
    public void setProducedActionTargetTypes(List<ActionTargetType> producedActionTargetTypes)
    {
        this.producedActionTargetTypes = producedActionTargetTypes;
    }


    /**
     * Return the list of guards produced by the governance service.
     *
     * @return list of guards
     */
    public List<GuardType> getProducedGuards()
    {
        return producedGuards;
    }


    /**
     * Set up the list of guards produced by the governance service.
     *
     * @param producedGuards list of guards
     */
    public void setProducedGuards(List<GuardType> producedGuards)
    {
        this.producedGuards = producedGuards;
    }


    /**
     * Return the unique identifier of governance engine that is processing the governance action.
     *
     * @return string guid
     */
    public String getGovernanceEngineGUID()
    {
        return governanceEngineGUID;
    }


    /**
     * Set up the unique identifier of governance engine that is processing the governance action.
     *
     * @param governanceEngineGUID string guid
     */
    public void setGovernanceEngineGUID(String governanceEngineGUID)
    {
        this.governanceEngineGUID = governanceEngineGUID;
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
    public Map<String, String> getFixedRequestParameters()
    {
        return fixedRequestParameters;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param fixedRequestParameters list of properties
     */
    public void setFixedRequestParameters(Map<String, String> fixedRequestParameters)
    {
        this.fixedRequestParameters = fixedRequestParameters;
    }


    /**
     * Return the minimum number of minutes to wait before starting the next governance action.
     *
     * @return int (minutes)
     */
    public int getWaitTime()
    {
        return waitTime;
    }


    /**
     * Set up the minimum number of minutes to wait before starting the next governance action.
     *
     * @param waitTime int (minutes)
     */
    public void setWaitTime(int waitTime)
    {
        this.waitTime = waitTime;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceActionTypeProperties{" +
                       "domainIdentifier=" + domainIdentifier +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", requiredRequestParameters=" + supportedRequestParameters +
                       ", requiredActionTargets=" + supportedActionTargetTypes +
                       ", producedRequestParameters=" + producedRequestParameters +
                       ", producedActionTargets=" + producedActionTargetTypes +
                       ", producedGuards=" + producedGuards +
                       ", governanceEngineGUID='" + governanceEngineGUID + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestParameters=" + fixedRequestParameters +
                       ", waitTime=" + waitTime +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceActionTypeProperties that = (GovernanceActionTypeProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       waitTime == that.waitTime &&
                       Objects.equals(supportedRequestParameters, that.supportedRequestParameters) &&
                       Objects.equals(supportedActionTargetTypes, that.supportedActionTargetTypes) &&
                       Objects.equals(producedRequestParameters, that.producedRequestParameters) &&
                       Objects.equals(producedActionTargetTypes, that.producedActionTargetTypes) &&
                       Objects.equals(producedGuards, that.producedGuards) &&
                       Objects.equals(governanceEngineGUID, that.governanceEngineGUID) &&
                       Objects.equals(requestType, that.requestType) &&
                       Objects.equals(fixedRequestParameters, that.fixedRequestParameters);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), domainIdentifier, displayName, description, supportedRequestParameters,
                            supportedActionTargetTypes, producedRequestParameters, producedActionTargetTypes,
                            producedGuards, governanceEngineGUID, requestType, fixedRequestParameters, waitTime);
    }
}
