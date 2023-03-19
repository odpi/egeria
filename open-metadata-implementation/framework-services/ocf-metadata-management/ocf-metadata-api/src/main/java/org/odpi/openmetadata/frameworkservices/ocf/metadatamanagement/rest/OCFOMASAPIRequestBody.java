/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OCFOMASAPIRequestBody provides a common header for Open Connector Framework (OCF) bean-based request bodies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ConnectionRequestBody.class, name = "ConnectionRequestBody"),
                @JsonSubTypes.Type(value = OriginRequestBody.class, name = "OriginRequestBody"),
                @JsonSubTypes.Type(value = OwnerRequestBody.class, name = "OwnerRequestBody"),
                @JsonSubTypes.Type(value = SchemaRequestBody.class, name = "SchemaRequestBody"),
                @JsonSubTypes.Type(value = FeedbackRequestBody.class, name = "FeedbackRequestBody")
        })
public abstract class OCFOMASAPIRequestBody implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public OCFOMASAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OCFOMASAPIRequestBody(OCFOMASAPIRequestBody template)
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
        return "OCFOMASAPIRequestBody{}";
    }
}
