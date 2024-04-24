/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalArchitectureOMASAPIResponse provides a common header for Digital Architecture OMAS managed rest to its REST API.
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
                @JsonSubTypes.Type(value = ReferenceValueAssignmentDefinitionsResponse.class, name = "ReferenceValueAssignmentDefinitionsResponse"),
                @JsonSubTypes.Type(value = ReferenceValueAssignmentItemsResponse.class, name = "ReferenceValueAssignmentItemsResponse"),
                @JsonSubTypes.Type(value = ValidValueAssignmentConsumersResponse.class, name = "ValidValueAssignmentConsumersResponse"),
                @JsonSubTypes.Type(value = ValidValueAssignmentDefinitionsResponse.class, name = "ValidValueAssignmentDefinitionsResponse"),
                @JsonSubTypes.Type(value = ValidValueMappingsResponse.class, name = "ValidValueMappingsResponse"),
                @JsonSubTypes.Type(value = ValidValueResponse.class, name = "ValidValueResponse"),
                @JsonSubTypes.Type(value = ValidValuesImplAssetsResponse.class, name = "ValidValuesImplAssetsResponse"),
                @JsonSubTypes.Type(value = ValidValuesImplDefinitionsResponse.class, name = "ValidValuesImplDefinitionsResponse"),
                @JsonSubTypes.Type(value = ValidValuesMappingsResponse.class, name = "ValidValuesMappingsResponse"),
                @JsonSubTypes.Type(value = ValidValuesResponse.class, name = "ValidValuesResponse"),

        })
public abstract class DigitalArchitectureOMASAPIResponse extends FFDCResponseBase
{
    /**
     * Default constructor
     */
    public DigitalArchitectureOMASAPIResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalArchitectureOMASAPIResponse(DigitalArchitectureOMASAPIResponse template)
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
        return "DigitalArchitectureOMASAPIResponse{" +
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
