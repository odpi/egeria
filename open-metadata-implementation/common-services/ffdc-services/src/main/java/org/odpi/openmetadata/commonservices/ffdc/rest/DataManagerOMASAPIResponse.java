/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;


import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FFDCResponseBase provides a common header for Data Manager OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DatabaseColumnResponse.class, name = "DatabaseColumnResponse"),
                @JsonSubTypes.Type(value = DatabaseColumnsResponse.class, name = "DatabaseColumnsResponse"),
                @JsonSubTypes.Type(value = DatabaseResponse.class, name = "DatabaseResponse"),
                @JsonSubTypes.Type(value = DatabasesResponse.class, name = "DatabasesResponse"),
                @JsonSubTypes.Type(value = DatabaseSchemaResponse.class, name = "DatabaseSchemaResponse"),
                @JsonSubTypes.Type(value = DatabaseSchemasResponse.class, name = "DatabaseSchemasResponse"),
                @JsonSubTypes.Type(value = DatabaseTableResponse.class, name = "DatabaseTableResponse"),
                @JsonSubTypes.Type(value = DatabaseTablesResponse.class, name = "DatabaseTablesResponse"),
                @JsonSubTypes.Type(value = DatabaseViewResponse.class, name = "DatabaseViewResponse"),
                @JsonSubTypes.Type(value = DatabaseViewsResponse.class, name = "DatabaseViewsResponse"),
        })
public abstract class DataManagerOMASAPIResponse extends FFDCResponseBase
{
    /**
     * Default constructor
     */
    public DataManagerOMASAPIResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataManagerOMASAPIResponse(DataManagerOMASAPIResponse template)
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
        return "DataManagerOMASAPIResponse{" +
                "exceptionClassName='" + getExceptionClassName() + '\'' +
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
}
