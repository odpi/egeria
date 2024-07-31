/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OCFOMASAPIResponse provides a common header for or Open Connector Framework (OCF) bean-based REST responses.
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
                @JsonSubTypes.Type(value = PagedResponse.class, name = "PagedResponse"),
                @JsonSubTypes.Type(value = AssetResponse.class, name = "AssetResponse"),
                @JsonSubTypes.Type(value = CommentResponse.class, name = "CommentResponse"),
                @JsonSubTypes.Type(value = MeaningResponse.class, name = "MeaningResponse"),
                @JsonSubTypes.Type(value = NoteLogResponse.class, name = "NoteLogResponse"),
                @JsonSubTypes.Type(value = ConnectionResponse.class, name = "OCFConnectionResponse"),
                @JsonSubTypes.Type(value = SchemaTypeResponse.class, name = "SchemaTypeResponse"),
                @JsonSubTypes.Type(value = TagResponse.class, name = "TagResponse"),
                @JsonSubTypes.Type(value = ValidValueResponse.class, name = "ValidValueResponse"),
        })
public abstract class OCFOMASAPIResponse extends FFDCResponseBase
{
    /**
     * Default constructor
     */
    public OCFOMASAPIResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OCFOMASAPIResponse(OCFOMASAPIResponse template)
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
        return "OCFOMASAPIResponse{" +
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
