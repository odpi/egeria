/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ODFOMASAPIRequestBody provides a common header for Open Discovery Framework (ODF) bean-based request bodies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AnnotationRequestBody.class,                   name = "AnnotationRequestBody"),
                @JsonSubTypes.Type(value = DiscoveryAnalysisReportRequestBody.class,      name = "DiscoveryAnalysisReportRequestBody"),
                @JsonSubTypes.Type(value = NewDiscoveryEngineRequestBody.class,           name = "NewDiscoveryEngineRequestBody"),
                @JsonSubTypes.Type(value = NewDiscoveryServiceRequestBody.class,          name = "NewDiscoveryServiceRequestBody"),
                @JsonSubTypes.Type(value = DiscoveryServiceRegistrationRequestBody.class, name = "DiscoveryServiceRegistrationRequestBody"),
        })
public abstract class ODFOMASAPIRequestBody implements java.io.Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public ODFOMASAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ODFOMASAPIRequestBody(ODFOMASAPIRequestBody template)
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
        return "ODFOMASAPIRequestBody{}";
    }
}
