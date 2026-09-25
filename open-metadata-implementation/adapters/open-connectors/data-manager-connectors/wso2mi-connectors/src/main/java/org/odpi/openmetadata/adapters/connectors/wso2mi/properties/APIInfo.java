/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wso2mi.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APIInfo describes a single REST API deployed on a WSO2 Micro Integrator, as returned by the
 * Management API's {@code GET /management/apis} resource.  Only the fields needed to catalog the
 * API as an open metadata asset are modelled here; unknown fields returned by newer WSO2 versions
 * are ignored.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIInfo
{
    private String name    = null;
    private String url      = null;
    private String version  = null;
    private String tracing  = null;
    private String stats    = null;


    /**
     * Default constructor.
     */
    public APIInfo()
    {
    }


    /**
     * Return the name of the API.  This is the unique identifier of the API within the Micro Integrator.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the API.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the invocation URL for the API (the context path exposed by the Micro Integrator).
     *
     * @return string URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the invocation URL for the API.
     *
     * @param url string URL
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Return the API version, if the Micro Integrator reports one.
     *
     * @return string version
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the API version.
     *
     * @param version string version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return whether message tracing is enabled for the API ("enabled" / "disabled").
     *
     * @return string flag
     */
    public String getTracing()
    {
        return tracing;
    }


    /**
     * Set up whether message tracing is enabled for the API.
     *
     * @param tracing string flag
     */
    public void setTracing(String tracing)
    {
        this.tracing = tracing;
    }


    /**
     * Return whether statistics collection is enabled for the API ("enabled" / "disabled").
     *
     * @return string flag
     */
    public String getStats()
    {
        return stats;
    }


    /**
     * Set up whether statistics collection is enabled for the API.
     *
     * @param stats string flag
     */
    public void setStats(String stats)
    {
        this.stats = stats;
    }


    /**
     * JSON-style toString.
     *
     * @return string with the property names and values
     */
    @Override
    public String toString()
    {
        return "APIInfo{" +
                       "name='" + name + '\'' +
                       ", url='" + url + '\'' +
                       ", version='" + version + '\'' +
                       ", tracing='" + tracing + '\'' +
                       ", stats='" + stats + '\'' +
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
        if (! (objectToCompare instanceof APIInfo that))
        {
            return false;
        }
        return Objects.equals(name, that.name) &&
                       Objects.equals(url, that.url) &&
                       Objects.equals(version, that.version) &&
                       Objects.equals(tracing, that.tracing) &&
                       Objects.equals(stats, that.stats);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, url, version, tracing, stats);
    }
}
