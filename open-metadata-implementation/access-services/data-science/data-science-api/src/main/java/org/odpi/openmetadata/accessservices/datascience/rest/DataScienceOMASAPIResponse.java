/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datascience.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataScienceOMASAPIResponse provides a common header for Data Science OMAS managed rest to its REST API.
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
        })
public abstract class DataScienceOMASAPIResponse extends FFDCResponseBase
{
    /**
     * Default constructor
     */
    public DataScienceOMASAPIResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataScienceOMASAPIResponse(DataScienceOMASAPIResponse  template)
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
        return "DataScienceOMASAPIResponse{" +
                "relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
