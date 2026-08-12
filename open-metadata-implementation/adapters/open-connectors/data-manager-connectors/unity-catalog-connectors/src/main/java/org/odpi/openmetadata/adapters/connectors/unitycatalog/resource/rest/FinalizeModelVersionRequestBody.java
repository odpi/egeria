/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Payload for finalizing a model version - maps to FinalizeModelVersion.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FinalizeModelVersionRequestBody
{
    private String full_name = null;
    private long    version   = 0L;


    /**
     * Constructor
     */
    public FinalizeModelVersionRequestBody()
    {
    }


    /**
     * Return the full name of the registered model to finalize.
     *
     * @return string
     */
    public String getFull_name()
    {
        return full_name;
    }


    /**
     * Set up the full name of the registered model to finalize.
     *
     * @param full_name string
     */
    public void setFull_name(String full_name)
    {
        this.full_name = full_name;
    }


    /**
     * Return the version number of the version to finalize.
     *
     * @return long
     */
    public long getVersion()
    {
        return version;
    }


    /**
     * Set up the version number of the version to finalize.
     *
     * @param version long
     */
    public void setVersion(long version)
    {
        this.version = version;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FinalizeModelVersionRequestBody{" +
                "full_name='" + full_name + '\'' +
                ", version=" + version +
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
        FinalizeModelVersionRequestBody that = (FinalizeModelVersionRequestBody) objectToCompare;
        return version == that.version && Objects.equals(full_name, that.full_name);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(full_name, version);
    }
}
