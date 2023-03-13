/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationConnectorConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationServiceRequestBody passes the minimum information to set up an integration service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationServiceRequestBody extends OMAGServerClientConfig
{
    private static final long    serialVersionUID = 1L;

    private Map<String, Object>              integrationServiceOptions   = null;
    private List<IntegrationConnectorConfig> integrationConnectorConfigs = null;


    /**
     * Default constructor for use with Jackson libraries
     */
    public IntegrationServiceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationServiceRequestBody(IntegrationServiceRequestBody template)
    {
        super(template);

        if (template != null)
        {
            integrationConnectorConfigs = template.getIntegrationConnectorConfigs();
            integrationServiceOptions   = template.getIntegrationServiceOptions();
        }
    }


    /**
     * Return the OCF Connection for the topic used to pass requests to this integration service.
     * The default values are constructed from the integration service name.
     * If this value is set to null then the integration service ignores incoming events.
     *
     * @return list of connector configurations
     */
    public List<IntegrationConnectorConfig> getIntegrationConnectorConfigs()
    {
        return integrationConnectorConfigs;
    }


    /**
     * Set up the OCF Connection for the topic used to pass requests to this integration service.
     * The default values are constructed from the integration service name.
     * If this value is set to null then the integration service ignores incoming events.
     *
     * @param integrationConnectorConfigs Connection properties
     */
    public void setIntegrationConnectorConfigs(List<IntegrationConnectorConfig> integrationConnectorConfigs)
    {
        this.integrationConnectorConfigs = integrationConnectorConfigs;
    }


    /**
     * Return the options for this integration service. These are properties that are specific to the integration service.
     *
     * @return Map from String to String
     */
    public Map<String, Object> getIntegrationServiceOptions()
    {
        if (integrationServiceOptions == null)
        {
            return null;
        }
        else if (integrationServiceOptions.isEmpty())
        {
            return null;
        }
        else
        {
            return integrationServiceOptions;
        }
    }


    /**
     * Set up the options for this integration service.  These are properties that are specific to the integration service.
     *
     * @param integrationServiceOptions Map from String to String
     */
    public void setIntegrationServiceOptions(Map<String, Object> integrationServiceOptions)
    {
        this.integrationServiceOptions = integrationServiceOptions;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationServiceRequestBody{" +
                "integrationServiceOptions=" + integrationServiceOptions +
                ", integrationConnectorConfigs=" + integrationConnectorConfigs +
                ", OMAGServerPlatformRootURL='" + getOMAGServerPlatformRootURL() + '\'' +
                ", OMAGServerName='" + getOMAGServerName() + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        IntegrationServiceRequestBody that = (IntegrationServiceRequestBody) objectToCompare;
        return Objects.equals(integrationServiceOptions, that.integrationServiceOptions) &&
                Objects.equals(integrationConnectorConfigs, that.integrationConnectorConfigs);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), integrationServiceOptions, integrationConnectorConfigs);
    }
}
