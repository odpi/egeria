/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGGAFAPIResponse provides a common header for Metadata Store Service managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ElementHeaderResponse.class, name = "ElementHeaderResponse"),
                @JsonSubTypes.Type(value = ElementHeadersResponse.class, name = "ElementHeadersResponse"),
                @JsonSubTypes.Type(value = OpenMetadataElementResponse.class, name = "OpenMetadataElementResponse"),
                @JsonSubTypes.Type(value = OpenMetadataElementsResponse.class, name = "OpenMetadataElementsResponse"),
                @JsonSubTypes.Type(value = RelatedMetadataElementListResponse.class, name = "RelatedMetadataElementListResponse"),
                @JsonSubTypes.Type(value = RelatedMetadataElementsListResponse.class, name = "RelatedMetadataElementsListResponse"),
        })
public abstract class OMAGGAFAPIResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    OMAGGAFAPIResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    OMAGGAFAPIResponse(OMAGGAFAPIResponse template)
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
        return "OMAGGAFAPIResponse{" +
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
