/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryEngineOMASAPIResponse provides a common header for Discovery Engine OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes
        ({
                @JsonSubTypes.Type(value = DiscoveryEngineListResponse.class,        name = "DiscoveryEngineListResponse"),
                @JsonSubTypes.Type(value = DiscoveryEnginePropertiesResponse.class,  name = "DiscoveryEnginePropertiesResponse"),
                @JsonSubTypes.Type(value = DiscoveryServiceListResponse.class,       name = "DiscoveryServiceListResponse"),
                @JsonSubTypes.Type(value = DiscoveryServicePropertiesResponse.class, name = "DiscoveryServicePropertiesResponse"),
                @JsonSubTypes.Type(value = RegisteredDiscoveryServiceResponse.class, name = "RegisteredDiscoveryServiceResponse")
        })
public abstract class DiscoveryEngineOMASAPIResponse extends FFDCResponseBase
{


    /**
     * Default constructor
     */
    public DiscoveryEngineOMASAPIResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryEngineOMASAPIResponse(DiscoveryEngineOMASAPIResponse  template)
    {
        super(template);
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryEngineOMASAPIResponse{" +
                "relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
