/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineServiceRequestBody passes the minimum information to set up an engine service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineServiceRequestBody extends OMAGServerClientConfig
{
    private Map<String, Object> engineServiceOptions = null;
    private List<EngineConfig>  engines              = null;


    /**
     * Default constructor for use with Jackson libraries
     */
    public EngineServiceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineServiceRequestBody(EngineServiceRequestBody template)
    {
        super(template);

        if (template != null)
        {
            engines = template.getEngines();
            engineServiceOptions   = template.getEngineServiceOptions();
        }
    }


    /**
     * Return the list of engines to load into this engine service.
     *
     * @return  list of qualified names for the engines to load plus user information
     */
    public List<EngineConfig> getEngines()
    {
        return engines;
    }


    /**
     * Set up the list of engines to load into this engine service.
     *
     * @param engines list of qualified names for the engines to load plus user information
     */
    public void setEngines(List<EngineConfig> engines)
    {
        this.engines = engines;
    }


    /**
     * Return the options for this engine service. These are properties that are specific to the engine service.
     *
     * @return Map from String to String
     */
    public Map<String, Object> getEngineServiceOptions()
    {
        if (engineServiceOptions == null)
        {
            return null;
        }
        else if (engineServiceOptions.isEmpty())
        {
            return null;
        }
        else
        {
            return engineServiceOptions;
        }
    }


    /**
     * Set up the options for this engine service.  These are properties that are specific to the engine service.
     *
     * @param engineServiceOptions Map from String to String
     */
    public void setEngineServiceOptions(Map<String, Object> engineServiceOptions)
    {
        this.engineServiceOptions = engineServiceOptions;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EngineServiceRequestBody{" +
                "engineServiceOptions=" + engineServiceOptions +
                ", engines=" + engines +
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
        EngineServiceRequestBody that = (EngineServiceRequestBody) objectToCompare;
        return Objects.equals(engineServiceOptions, that.engineServiceOptions) &&
                Objects.equals(engines, that.engines);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), engineServiceOptions, engines);
    }
}
