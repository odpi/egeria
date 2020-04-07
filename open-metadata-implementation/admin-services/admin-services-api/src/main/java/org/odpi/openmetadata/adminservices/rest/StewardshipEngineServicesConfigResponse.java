/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.StewardshipEngineServicesConfig;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * StewardshipEngineServicesConfigResponse is the response structure used on the OMAG REST API calls that returns a
 * StewardshipEngineServicesConfig object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StewardshipEngineServicesConfigResponse extends AdminServicesAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private StewardshipEngineServicesConfig config = null;


    /**
     * Default constructor
     */
    public StewardshipEngineServicesConfigResponse()
    {
    }


    /**
     * Return the config object.
     *
     * @return StewardshipEngineServicesConfig object
     */
    public StewardshipEngineServicesConfig getConfig()
    {
        if (config == null)
        {
            return null;
        }
        else
        {
            return new StewardshipEngineServicesConfig(config);
        }
    }


    /**
     * Set up the config object.
     *
     * @param config - StewardshipEngineServicesConfig object
     */
    public void setConfig(StewardshipEngineServicesConfig config)
    {
        this.config = config;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "StewardshipEngineServicesConfigResponse{" +
                "config=" + config +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        if (!(objectToCompare instanceof StewardshipEngineServicesConfigResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        StewardshipEngineServicesConfigResponse that = (StewardshipEngineServicesConfigResponse) objectToCompare;
        return Objects.equals(config, that.config);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), config);
    }
}
