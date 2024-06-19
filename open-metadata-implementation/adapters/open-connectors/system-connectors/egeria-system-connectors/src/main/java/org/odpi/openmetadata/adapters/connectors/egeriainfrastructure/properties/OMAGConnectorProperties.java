/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGConnectorProperties holds details about a configured and possibly running connector.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGConnectorProperties
{
    private String                        connectorName           = null;
    private ConnectorType                 connectorType           = null;
    private String                        networkAddress          = null;
    private String                        connectorUserId         = null;
    private Map<String, Object>           configurationProperties = null;
    private String                        connectorStatus         = null;
    private List<OMAGConnectorProperties> nestedConnectors        = null;

    public OMAGConnectorProperties()
    {
    }

    public String getConnectorName()
    {
        return connectorName;
    }

    public void setConnectorName(String connectorName)
    {
        this.connectorName = connectorName;
    }

    public ConnectorType getConnectorType()
    {
        return connectorType;
    }

    public void setConnectorType(ConnectorType connectorType)
    {
        this.connectorType = connectorType;
    }

    public String getNetworkAddress()
    {
        return networkAddress;
    }

    public void setNetworkAddress(String networkAddress)
    {
        this.networkAddress = networkAddress;
    }

    public String getConnectorUserId()
    {
        return connectorUserId;
    }

    public void setConnectorUserId(String connectorUserId)
    {
        this.connectorUserId = connectorUserId;
    }

    public Map<String, Object> getConfigurationProperties()
    {
        return configurationProperties;
    }

    public void setConfigurationProperties(Map<String, Object> configurationProperties)
    {
        this.configurationProperties = configurationProperties;
    }

    public String getConnectorStatus()
    {
        return connectorStatus;
    }

    public void setConnectorStatus(String connectorStatus)
    {
        this.connectorStatus = connectorStatus;
    }

    public List<OMAGConnectorProperties> getNestedConnectors()
    {
        return nestedConnectors;
    }

    public void setNestedConnectors(List<OMAGConnectorProperties> nestedConnectors)
    {
        this.nestedConnectors = nestedConnectors;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMAGConnectorProperties{" +
                "connectorName='" + connectorName + '\'' +
                ", connectorType='" + connectorType + '\'' +
                ", networkAddress='" + networkAddress + '\'' +
                ", connectorUserId='" + connectorUserId + '\'' +
                ", configurationProperties=" + configurationProperties +
                ", connectorStatus='" + connectorStatus + '\'' +
                ", nestedConnectors=" + nestedConnectors +
                '}';
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
        OMAGConnectorProperties that = (OMAGConnectorProperties) objectToCompare;
        return Objects.equals(connectorName, that.connectorName) && Objects.equals(connectorType, that.connectorType) && Objects.equals(networkAddress, that.networkAddress) && Objects.equals(connectorUserId, that.connectorUserId) && Objects.equals(configurationProperties, that.configurationProperties) && Objects.equals(connectorStatus, that.connectorStatus) && Objects.equals(nestedConnectors, that.nestedConnectors);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(connectorName, connectorType, networkAddress, connectorUserId, configurationProperties, connectorStatus, nestedConnectors);
    }
}
