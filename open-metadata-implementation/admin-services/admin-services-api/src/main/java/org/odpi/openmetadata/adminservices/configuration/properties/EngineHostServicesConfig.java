/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineHostServicesConfig provides the properties to configure an engine host OMAG server.  The engine host
 * runs one or more Open Metadata Engine Services (OMES) that in turn run one or more governance engines.
 * The configuration of the engine host services is in two parts:
 *
 * <ul>
 *     <li>The inherited properties from OMAGServerClientConfig defines the platform root URL and server name of
 *         server running the Governance Engine OMAS that provides configuration for the governance engines
 *         as well as the governance actions that drive the automated execution of governance requests.</li>
 *
 *     <li>An array of EngineServiceConfig properties, one for each engine service to run.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineHostServicesConfig extends OMAGServerClientConfig
{
    private static final long    serialVersionUID = 1L;

    
    private List<EngineServiceConfig> engineServiceConfigs = null;


    /**
     * Default constructor
     */
    public EngineHostServicesConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineHostServicesConfig(EngineHostServicesConfig template)
    {
        super(template);

        if (template != null)
        {
            engineServiceConfigs = template.getEngineServiceConfigs();
        }
    }


    /**
     * Return the list of configuration for the engine services that will run in this server.
     *
     * @return list of qualified names
     */
    public List<EngineServiceConfig> getEngineServiceConfigs()
    {
        return engineServiceConfigs;
    }


    /**
     * Set up the list of configuration for the engine services that will run in this server.
     *
     * @param engineServiceConfigs list of qualified names
     */
    public void setEngineServiceConfigs(List<EngineServiceConfig> engineServiceConfigs)
    {
        this.engineServiceConfigs = engineServiceConfigs;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EngineHostServicesConfig{" +
                       "engineServiceConfigs=" + engineServiceConfigs +
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
        EngineHostServicesConfig that = (EngineHostServicesConfig) objectToCompare;
        return Objects.equals(engineServiceConfigs, that.engineServiceConfigs);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), engineServiceConfigs);
    }
}
