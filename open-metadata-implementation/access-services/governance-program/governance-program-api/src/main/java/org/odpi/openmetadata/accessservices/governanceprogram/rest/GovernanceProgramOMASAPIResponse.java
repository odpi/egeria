/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceProgramOMASAPIResponse provides a common header for Governance Program OMAS managed rest to its REST API.
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
                @JsonSubTypes.Type(value = GovernanceRoleResponse.class, name = "GovernanceRoleResponse"),
                @JsonSubTypes.Type(value = GovernanceRoleListResponse.class, name = "GovernanceRoleListResponse"),
                @JsonSubTypes.Type(value = SubjectAreaDefinitionResponse.class, name = "SubjectAreaDefinitionResponse"),
                @JsonSubTypes.Type(value = SubjectAreaListResponse.class, name = "SubjectAreaListResponse"),
                @JsonSubTypes.Type(value = SubjectAreaResponse.class, name = "SubjectAreaResponse"),
                @JsonSubTypes.Type(value = GovernanceZoneDefinitionResponse.class, name = "GovernanceZoneDefinitionResponse"),
                @JsonSubTypes.Type(value = GovernanceZoneListResponse.class, name = "GovernanceZoneListResponse"),
                @JsonSubTypes.Type(value = GovernanceZoneResponse.class, name = "GovernanceZoneResponse"),
        })
public abstract class GovernanceProgramOMASAPIResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public GovernanceProgramOMASAPIResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceProgramOMASAPIResponse(GovernanceProgramOMASAPIResponse  template)
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
        return "GovernanceProgramOMASAPIResponse{" +
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
