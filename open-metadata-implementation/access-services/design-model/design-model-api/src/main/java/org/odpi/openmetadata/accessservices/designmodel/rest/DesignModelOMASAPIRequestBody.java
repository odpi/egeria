/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.designmodel.rest;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DesignModelOMASAPIRequestBody provides a common header for Design Model OMAS request bodies for its REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
        })
public abstract class DesignModelOMASAPIRequestBody
{
    /**
     * Default constructor
     */
    public DesignModelOMASAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DesignModelOMASAPIRequestBody(DesignModelOMASAPIRequestBody template)
    {
    }


    /**
     * JSON-like toString
     *
     * @return string containing the class name
     */
    @Override
    public String toString()
    {
        return "DesignModelOMASAPIRequestBody{}";
    }
}
