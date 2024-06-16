/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceserver.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceServiceConfigurationEvent is used to inform listening engine hosts that the configuration of one of the
 * governance services has changed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceServiceConfigurationEvent extends GovernanceEngineConfigurationEvent
{
    private String              registeredGovernanceServiceGUID = null;
    private String              requestType                     = null;
    private Map<String, String> requestParameters               = null;


    /**
     * Default constructor
     */
    public GovernanceServiceConfigurationEvent()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceServiceConfigurationEvent(GovernanceServiceConfigurationEvent template)
    {
        super(template);

        if (template != null)
        {
            registeredGovernanceServiceGUID = template.getRegisteredGovernanceServiceGUID();
            requestType = template.getRequestType();
            requestParameters = template.getRequestParameters();
        }
    }


    /**
     * Return the unique identifier of the registered governance service that has changed.
     *
     * @return string guid
     */
    public String getRegisteredGovernanceServiceGUID()
    {
        return registeredGovernanceServiceGUID;
    }


    /**
     * Set up the unique identifier of the registered governance service that has changed.
     *
     * @param registeredGovernanceServiceGUID string guid
     */
    public void setRegisteredGovernanceServiceGUID(String registeredGovernanceServiceGUID)
    {
        this.registeredGovernanceServiceGUID = registeredGovernanceServiceGUID;
    }


    /**
     * Return a governance request type for the governance service affected by the change.
     *
     * @return a governance request type
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the governance request type for the governance service affected by the change.
     *
     * @param requestType a governance request type
     */
    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }


    /**
     * Return the list of default analysis parameters for the governance service affected by the change.
     *
     * @return map of parameters
     */
    public Map<String, String> getRequestParameters()
    {
        if (requestParameters == null)
        {
            return null;
        }
        else if (requestParameters.isEmpty())
        {
            return  null;
        }

        return new HashMap<>(requestParameters);
    }


    /**
     * Set up the list of default analysis parameters for the governance service affected by the change.
     *
     * @param requestParameters map of parameters
     */
    public void setRequestParameters(Map<String, String> requestParameters)
    {
        this.requestParameters = requestParameters;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceServiceConfigurationEvent{" +
                       "registeredGovernanceServiceGUID='" + registeredGovernanceServiceGUID + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestParameters=" + requestParameters +
                       ", eventVersionId=" + getEventVersionId() +
                       ", eventType=" + getEventType() +
                       ", governanceEngineGUID='" + getGovernanceEngineGUID() + '\'' +
                       ", governanceEngineName='" + getGovernanceEngineName() + '\'' +
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
        GovernanceServiceConfigurationEvent that = (GovernanceServiceConfigurationEvent) objectToCompare;
        return Objects.equals(registeredGovernanceServiceGUID, that.registeredGovernanceServiceGUID) &&
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
        return Objects.hash(super.hashCode(), registeredGovernanceServiceGUID, requestType, requestParameters);
    }
}
