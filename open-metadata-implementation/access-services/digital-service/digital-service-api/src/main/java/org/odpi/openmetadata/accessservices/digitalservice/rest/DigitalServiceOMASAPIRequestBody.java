/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalServiceProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalServiceOMASAPIRequestBody provides a common header for DigitalServiceProperties OMAS request bodies for its REST API.
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSubTypes(
        {
        @JsonSubTypes.Type(value = DigitalServiceProperties.class, name = "DigitalServiceProperties")
        })
public abstract class DigitalServiceOMASAPIRequestBody implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public DigitalServiceOMASAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalServiceOMASAPIRequestBody(DigitalServiceOMASAPIRequestBody template)
    {
    }


    /**
     * {@inheritDoc}
     *
     * JSON-like toString
     */
    @Override
    public String toString()
    {
        return "DigitalServiceOMASAPIRequestBody{}";
    }
}
