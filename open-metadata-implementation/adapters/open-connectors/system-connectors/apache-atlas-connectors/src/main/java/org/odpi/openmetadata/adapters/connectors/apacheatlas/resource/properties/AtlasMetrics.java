/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasMetrics returns information about a running Apache Atlas.  The values are returned in a series of nested maps.
 * Note this class does not describe all the information returned.  Also the values returned differ depending on the
 * release of Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasMetrics
{
    private AtlasMetricsData data = null;


    /**
     * Default constructor
     */
    public AtlasMetrics()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AtlasMetrics(AtlasMetrics template)
    {
        if (template != null)
        {
            this.data = template.getData();
        }
    }


    /**
     * Retrieve the stats.
     *
     * @return data map
     */
    public AtlasMetricsData getData()
    {
        return data;
    }


    /**
     * Set up the stats.
     *
     * @param data data map
     */
    public void setData(AtlasMetricsData data)
    {
        this.data = data;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AtlasMetrics{" +
                       "data=" + data +
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
        if (! (objectToCompare instanceof AtlasMetrics that))
        {
            return false;
        }
        return Objects.equals(data, that.data);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(data);
    }
}
