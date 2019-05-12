/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetConsumerOMASAPIResponse provides a common header for Asset Consumer OMAS managed rest to its REST API.
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
                @JsonSubTypes.Type(value = ConnectionResponse.class,           name = "ConnectionResponse"),
                @JsonSubTypes.Type(value = NoteLogResponse.class,              name = "NoteLogResponse"),
                @JsonSubTypes.Type(value = MeaningListResponse.class,          name = "MeaningListResponse"),
                @JsonSubTypes.Type(value = MeaningResponse.class,              name = "MeaningResponse"),
                @JsonSubTypes.Type(value = TagListResponse.class,              name = "TagListResponse"),
                @JsonSubTypes.Type(value = TagResponse.class,                  name = "TagResponse")
        })
public abstract class AssetConsumerOMASAPIResponse extends FFDCResponseBase
{
    /**
     * Default constructor
     */
    public AssetConsumerOMASAPIResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetConsumerOMASAPIResponse(AssetConsumerOMASAPIResponse  template)
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
        return "AssetConsumerOMASAPIResponse{" +
                "relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
