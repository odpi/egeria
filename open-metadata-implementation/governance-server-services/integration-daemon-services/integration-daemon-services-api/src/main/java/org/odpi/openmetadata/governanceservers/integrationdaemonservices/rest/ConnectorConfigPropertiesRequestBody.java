/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ConnectorConfigPropertiesRequestBody describes the request body used to update the configuration properties of an integration connector.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorConfigPropertiesRequestBody implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String              connectorName           = null;
    private boolean             mergeUpdate             = false;
    private Map<String, Object> configurationProperties = null;


    /**
     * Default constructor
     */
    public ConnectorConfigPropertiesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ConnectorConfigPropertiesRequestBody(ConnectorConfigPropertiesRequestBody template)
    {
        if (template != null)
        {
            connectorName = template.getConnectorName();
            mergeUpdate = template.getMergeUpdate();
            configurationProperties = template.getConfigurationProperties();
        }
    }


    /**
     * Return the name of the connector (from the integration service configuration).
     *
     * @return string name
     */
    public String getConnectorName()
    {
        return connectorName;
    }


    /**
     * Set up the name of the connector (from the integration service configuration).
     *
     * @param connectorName string name
     */
    public void setConnectorName(String connectorName)
    {
        this.connectorName = connectorName;
    }


    /**
     * Return indication of whether properties should be over-write or replace existing properties.
     *
     * @return boolean flag
     */
    public boolean getMergeUpdate()
    {
        return mergeUpdate;
    }


    /**
     * Set up the indication of whether properties should be over-write or replace existing properties.
     *
     * @param mergeUpdate boolean flag
     */
    public void setMergeUpdate(boolean mergeUpdate)
    {
        this.mergeUpdate = mergeUpdate;
    }


    /**
     * Return the configuration properties for the connection.
     *
     * @return properties object
     */
    public Map<String, Object> getConfigurationProperties()
    {
        return configurationProperties;
    }


    /**
     * Set up the configuration properties for the connection.
     *
     * @param configurationProperties properties object
     */
    public void setConfigurationProperties(Map<String, Object> configurationProperties)
    {
        this.configurationProperties = configurationProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectorConfigPropertiesRequestBody{" +
                       "connectorName='" + connectorName + '\'' +
                       ", mergeUpdate=" + mergeUpdate +
                       ", configurationProperties=" + configurationProperties +
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
        ConnectorConfigPropertiesRequestBody that = (ConnectorConfigPropertiesRequestBody) objectToCompare;
        return mergeUpdate == that.mergeUpdate &&
                       Objects.equals(connectorName, that.connectorName) &&
                       Objects.equals(configurationProperties, that.configurationProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(connectorName, mergeUpdate, configurationProperties);
    }
}
