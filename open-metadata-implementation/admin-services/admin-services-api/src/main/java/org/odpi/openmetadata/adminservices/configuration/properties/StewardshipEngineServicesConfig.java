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
 * StewardshipServicesConfig provides the configuration properties for
 * the stewardship services
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StewardshipServicesConfig extends AccessServiceClientConfig
{
    private static final long    serialVersionUID = 1L;

    private List<String> stewardshipEngineNames = null;


    /**
     * Default constructor
     */
    public StewardshipServicesConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StewardshipServicesConfig(StewardshipServicesConfig template)
    {
        super(template);

        if (template != null)
        {
            stewardshipEngineNames = template.getStewardshipEngineNames();
        }
    }


    /**
     * Return the list of unique names (qualifiedName) for the discovery engines that will run in this discover server.
     *
     * @return list of qualified names
     */
    public List<String> getStewardshipEngineNames()
    {
        return stewardshipEngineNames;
    }


    /**
     * Set up the list of unique names (qualifiedName) for the discovery engines that will run in this discovery
     * server.
     *
     * @param stewardshipEngineNames list of qualified names
     */
    public void setStewardshipEngineNames(List<String> stewardshipEngineNames)
    {
        this.stewardshipEngineNames = stewardshipEngineNames;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "StewardshipServicesConfig{" +
                "stewardshipEngineNames=" + stewardshipEngineNames +
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
        StewardshipServicesConfig that = (StewardshipServicesConfig) objectToCompare;
        return Objects.equals(stewardshipEngineNames, that.getStewardshipEngineNames());
    }

    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), stewardshipEngineNames);
    }
}
