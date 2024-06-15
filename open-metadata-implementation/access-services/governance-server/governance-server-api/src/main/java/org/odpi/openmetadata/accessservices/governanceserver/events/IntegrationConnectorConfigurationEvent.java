/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceserver.events;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationGroupConfigurationEvent is used to inform listening integration daemons that the configuration of
 * one of the integration groups has changed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationConnectorConfigurationEvent extends GovernanceServerEvent
{
    private String integrationConnectorGUID = null;
    private String integrationConnectorName = null;

    /**
     * Default constructor
     */
    public IntegrationConnectorConfigurationEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public IntegrationConnectorConfigurationEvent(IntegrationConnectorConfigurationEvent template)
    {
        super(template);

        if (template != null)
        {
            this.integrationConnectorGUID = template.getIntegrationConnectorGUID();
            this.integrationConnectorName = template.getIntegrationConnectorName();
        }
    }


    /**
     * Return the unique identifier of the Integration Group that has a configuration change.
     *
     * @return string guid
     */
    public String getIntegrationConnectorGUID()
    {
        return integrationConnectorGUID;
    }


    /**
     * Set up the unique identifier of the Integration Group that has a configuration change.
     *
     * @param integrationConnectorGUID string guid
     */
    public void setIntegrationConnectorGUID(String integrationConnectorGUID)
    {
        this.integrationConnectorGUID = integrationConnectorGUID;
    }


    /**
     * Return the unique name of the Integration Group that has a configuration change.
     *
     * @return string name
     */
    public String getIntegrationConnectorName()
    {
        return integrationConnectorName;
    }


    /**
     * Set up the unique name of the Integration Group that has a configuration change.
     *
     * @param integrationConnectorName string name
     */
    public void setIntegrationConnectorName(String integrationConnectorName)
    {
        this.integrationConnectorName = integrationConnectorName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupConfigurationEvent{" +
                "integrationConnectorGUID='" + integrationConnectorGUID + '\'' +
                ", integrationConnectorName='" + integrationConnectorName + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        IntegrationConnectorConfigurationEvent that = (IntegrationConnectorConfigurationEvent) objectToCompare;
        return Objects.equals(integrationConnectorGUID, that.integrationConnectorGUID) &&
                Objects.equals(integrationConnectorName, that.integrationConnectorName);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), integrationConnectorGUID, integrationConnectorName);
    }
}
