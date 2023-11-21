/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasMetricsSystem describes the infrastructure that Apache Atlas is running on.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasMetricsSystem
{
    private Map<String, Object> memory = null;
    private Map<String, String> os = null;
    private Map<String, String> runtime = null;


    /**
     * Default constructor
     */
    public AtlasMetricsSystem()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AtlasMetricsSystem(AtlasMetricsSystem template)
    {
        if (template != null)
        {
            this.memory = template.getMemory();
            this.os = template.getOs();
            this.runtime = template.getRuntime();
        }
    }


    public Map<String, Object> getMemory()
    {
        return memory;
    }


    /**
     * Return a map of different memory usage stats.
     *
     * @param memory map of metrics
     */
    public void setMemory(Map<String, Object> memory)
    {
        this.memory = memory;
    }


    /**
     * Return a map of properties about the operating system.
     *
     * @return map of metrics
     */
    public Map<String, String> getOs()
    {
        return os;
    }


    /**
     * Set up a map of properties about the operating system.
     * @param os map of metrics
     */
    public void setOs(Map<String, String> os)
    {
        this.os = os;
    }


    /**
     * Return a map of properties about the Java runtime.
     *
     * @return map of metrics
     */
    public Map<String, String> getRuntime()
    {
        return runtime;
    }


    /**
     * Set up a map of properties about the Java runtime.
     *
     * @param runtime map of metrics
     */
    public void setRuntime(Map<String, String> runtime)
    {
        this.runtime = runtime;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AtlasMetricsSystem{" +
                       "memory=" + memory +
                       ", os=" + os +
                       ", runtime=" + runtime +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof AtlasMetricsSystem that))
        {
            return false;
        }
        return Objects.equals(memory, that.memory) && Objects.equals(os, that.os) && Objects.equals(runtime, that.runtime);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(memory, os, runtime);
    }
}
