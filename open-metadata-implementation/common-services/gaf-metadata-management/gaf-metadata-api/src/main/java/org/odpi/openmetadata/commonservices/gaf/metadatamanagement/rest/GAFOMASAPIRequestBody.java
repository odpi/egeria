/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.gaf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GAFOMASAPIRequestBody provides a common header for Governance Action Framework (GAF) bean-based request bodies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SecurityTagsRequestBody.class, name = "SecurityTagsRequestBody"),
                @JsonSubTypes.Type(value = ZoneRequestBody.class, name = "ZoneRequestBody")
        })
public abstract class GAFOMASAPIRequestBody implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public GAFOMASAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GAFOMASAPIRequestBody(GAFOMASAPIRequestBody template)
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
        return "GAFOMASAPIRequestBody{}";
    }
}
