/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ViewServiceRequestBody passes the minimum information to set up a view server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ViewServiceRequestBody extends OMAGServerClientConfig
{
    private Map<String, Object>          viewServiceOptions = null;


    /**
     * Default constructor for use with Jackson libraries
     */
    public ViewServiceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ViewServiceRequestBody(ViewServiceRequestBody template)
    {
        super(template);

        if (template != null)
        {
            viewServiceOptions = template.getViewServiceOptions();
        }
    }


    /**
     * Return the options for this view service. These are properties that are specific to the view service.
     *
     * @return Map from String to String
     */
    public Map<String, Object> getViewServiceOptions()
    {
        if (viewServiceOptions == null)
        {
            return null;
        }
        else if (viewServiceOptions.isEmpty())
        {
            return null;
        }
        else
        {
            return viewServiceOptions;
        }
    }


    /**
     * Set up the options for this view service.  These are properties that are specific to the view service.
     *
     * @param viewServiceOptions Map from String to String
     */
    public void setViewServiceOptions(Map<String, Object> viewServiceOptions)
    {
        this.viewServiceOptions = viewServiceOptions;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ViewServiceRequestBody{" +
                "viewServiceOptions=" + viewServiceOptions +
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
        ViewServiceRequestBody that = (ViewServiceRequestBody) objectToCompare;
        return Objects.equals(viewServiceOptions, that.viewServiceOptions);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), viewServiceOptions);
    }
}
