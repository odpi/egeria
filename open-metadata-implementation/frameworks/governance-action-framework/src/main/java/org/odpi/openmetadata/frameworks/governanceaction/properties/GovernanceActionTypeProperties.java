/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 1L;

    private int                 domainIdentifier     = 0;
    private String              displayName          = null;
    private String              description          = null;

    private List<String>        supportedGuards      = null;

    private String              governanceEngineGUID = null;
    private String              requestType          = null;
    private Map<String, String> requestParameters    = null;

    private int                 waitTime               = 0;


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

            supportedGuards = template.getSupportedGuards();

            governanceEngineGUID = template.getGovernanceEngineGUID();
            requestType = template.getRequestType();
            requestParameters = template.getRequestParameters();

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
     * Return the list of guards provided by the previous governance service.
     *
     * @return list of guards
     */
    public List<String> getSupportedGuards()
    {
        return supportedGuards;
    }


    /**
     * Set up the list of guards provided by the previous governance service.
     *
     * @param supportedGuards list of guards
     */
    public void setSupportedGuards(List<String> supportedGuards)
    {
        this.supportedGuards = supportedGuards;
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
                       ", supportedGuards=" + supportedGuards +
                       ", governanceEngineGUID='" + governanceEngineGUID + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestParameters=" + requestParameters +
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
                       Objects.equals(supportedGuards, that.supportedGuards) &&
                       Objects.equals(governanceEngineGUID, that.governanceEngineGUID) &&
                       Objects.equals(requestType, that.requestType) &&
                       Objects.equals(requestParameters, that.requestParameters);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), domainIdentifier, displayName, description,
                            supportedGuards, governanceEngineGUID, requestType, requestParameters, waitTime);
    }
}
