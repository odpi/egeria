/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opengovernance.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.RegisteredIntegrationConnectorProperties;


import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredIntegrationConnectorElement contains the properties and header for an integration connector entity
 * retrieved from the metadata
 * repository plus its supported request types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredIntegrationConnectorElement extends IntegrationConnectorElement
{
    private String                                   connectorId            = null;
    private RegisteredIntegrationConnectorProperties registrationProperties = null;


    /**
     * Default constructor
     */
    public RegisteredIntegrationConnectorElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredIntegrationConnectorElement(RegisteredIntegrationConnectorElement template)
    {
        super(template);

        if (template != null)
        {
            connectorId            = template.getConnectorId();
            registrationProperties = template.getRegistrationProperties();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredIntegrationConnectorElement(IntegrationConnectorElement template)
    {
        super(template);
    }


    /**
     * Return the properties of the registered integration connector.
     *
     * @return properties bean
     */
    public RegisteredIntegrationConnectorProperties getRegistrationProperties()
    {
        return registrationProperties;
    }


    /**
     * Return the unique identifier of this connector.
     *
     * @return string guid
     */
    public String getConnectorId()
    {
        return connectorId;
    }


    /**
     * Set up the unique identifier of this connector.
     *
     * @param connectorId string guid
     */
    public void setConnectorId(String connectorId)
    {
        this.connectorId = connectorId;
    }


    /**
     * Set up the properties of the registered integration connector.
     *
     * @param registrationProperties properties bean
     */
    public void setRegistrationProperties(RegisteredIntegrationConnectorProperties registrationProperties)
    {
        this.registrationProperties = registrationProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RegisteredIntegrationConnectorElement{" +
                "registrationProperties=" + registrationProperties +
                ", connectorId='" + connectorId + '\'' +
                ", elementHeader=" + getElementHeader() +
                ", properties=" + getProperties() +
                ", catalogTargets=" + getCatalogTargets() +
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
        RegisteredIntegrationConnectorElement that = (RegisteredIntegrationConnectorElement) objectToCompare;
        return Objects.equals(registrationProperties, that.registrationProperties) && Objects.equals(connectorId, that.connectorId);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), registrationProperties, connectorId);
    }
}
