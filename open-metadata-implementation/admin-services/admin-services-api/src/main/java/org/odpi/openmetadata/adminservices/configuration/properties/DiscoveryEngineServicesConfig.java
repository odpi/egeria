/* SPDX-License-Identifier: Apache 2.0 */
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
 * DiscoveryEngineServicesConfig provides the properties to configure a discovery server.  The discovery service
 * runs one of more discovery engines.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryEngineServicesConfig extends OMAGServerClientConfig
{
    private static final long    serialVersionUID = 1L;

    private List<String>  discoveryEngineNames = null;


    /**
     * Default constructor
     */
    public DiscoveryEngineServicesConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryEngineServicesConfig(DiscoveryEngineServicesConfig template)
    {
        super(template);

        if (template != null)
        {
            discoveryEngineNames = template.getDiscoveryEngineNames();
        }
    }


    /**
     * Return the list of unique names (qualifiedName) for the discovery engines that will run in this server.
     *
     * @return list of qualified names
     */
    public List<String> getDiscoveryEngineNames()
    {
        return discoveryEngineNames;
    }


    /**
     * Set up the list of unique names (qualifiedName) for the discovery engines that will run in this server.
     *
     * @param discoveryEngineNames list of qualified names
     */
    public void setDiscoveryEngineNames(List<String> discoveryEngineNames)
    {
        this.discoveryEngineNames = discoveryEngineNames;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "DiscoveryEngineServicesConfig{" +
                "discoveryEngineNames=" + discoveryEngineNames +
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
        DiscoveryEngineServicesConfig that = (DiscoveryEngineServicesConfig) objectToCompare;
        return Objects.equals(discoveryEngineNames, that.discoveryEngineNames);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), discoveryEngineNames);
    }
}
