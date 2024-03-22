/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationDaemonConfig provides the specialist properties used to initialize an integration daemon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationDaemonServicesConfig extends AdminServicesConfigHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    private List<IntegrationServiceConfig>  integrationServicesConfig       = null;
    private List<IntegrationGroupConfig>    dynamicIntegrationGroupsConfig  = null;



    /**
     * Default constructor.
     */
    public IntegrationDaemonServicesConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public IntegrationDaemonServicesConfig(IntegrationDaemonServicesConfig template)
    {
        super(template);

        if (template != null)
        {
            integrationServicesConfig       = template.getIntegrationServicesConfig();
            dynamicIntegrationGroupsConfig  = template.getDynamicIntegrationGroupsConfig();
        }
    }


    /**
     * Return the configuration for the registered Open Metadata Integration Services (OMISs).  Used in an integration daemon.
     *
     * @return list of configuration properties, one for each OMIS
     */
    public List<IntegrationServiceConfig> getIntegrationServicesConfig()
    {
        return integrationServicesConfig;
    }


    /**
     * Set up the configuration for the registered Open Metadata Integration Services (OMISs).  Used in an integration daemon.
     *
     * @param integrationServicesConfig list of configuration properties, one for each OMIS
     */
    public void setIntegrationServicesConfig(List<IntegrationServiceConfig> integrationServicesConfig)
    {
        this.integrationServicesConfig = integrationServicesConfig;
    }


    /**
     * Return the optional list of dynamic integration groups.  Used in an integration daemon.
     *
     * @return list of configuration properties, one for each integration group
     */
    public List<IntegrationGroupConfig> getDynamicIntegrationGroupsConfig()
    {
        return dynamicIntegrationGroupsConfig;
    }


    /**
     * Set up the optional list of dynamic integration groups.  Used in an integration daemon.
     *
     * @param dynamicIntegrationGroupsConfig list of configuration properties, one for each integration group
     */
    public void setDynamicIntegrationGroupsConfig(List<IntegrationGroupConfig> dynamicIntegrationGroupsConfig)
    {
        this.dynamicIntegrationGroupsConfig = dynamicIntegrationGroupsConfig;
    }

    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationDaemonServicesConfig{" +
                "integrationServicesConfig=" + integrationServicesConfig +
                ", dynamicIntegrationGroupsConfig=" + dynamicIntegrationGroupsConfig +
                "} " + super.toString();
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
        IntegrationDaemonServicesConfig that = (IntegrationDaemonServicesConfig) objectToCompare;
        return Objects.equals(getIntegrationServicesConfig(), that.getIntegrationServicesConfig()) &&
                       Objects.equals(getDynamicIntegrationGroupsConfig(), that.getDynamicIntegrationGroupsConfig());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getIntegrationServicesConfig(), getDynamicIntegrationGroupsConfig());
    }
}
