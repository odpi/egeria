/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceProgramOMASAPIRequestBody provides a common header for Governance Program OMAS request bodies for its REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PersonalDetailsRequestBody.class, name = "PersonalDetailsRequestBody"),
                @JsonSubTypes.Type(value = PersonalProfileValidatorRequestBody.class, name = "PersonalProfileValidatorRequestBody"),
                @JsonSubTypes.Type(value = GovernanceOfficerDetailsRequestBody.class, name = "GovernanceOfficerDetailsRequestBody"),
                @JsonSubTypes.Type(value = GovernanceOfficerValidatorRequestBody.class, name = "GovernanceOfficerValidatorRequestBody"),
                @JsonSubTypes.Type(value = GovernanceDomainRequestBody.class, name = "GovernanceDomainRequestBody"),
                @JsonSubTypes.Type(value = GUIDRequestBody.class, name = "GUIDRequestBody")
        })
public abstract class GovernanceProgramOMASAPIRequestBody implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public GovernanceProgramOMASAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceProgramOMASAPIRequestBody(GovernanceProgramOMASAPIRequestBody template)
    {
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceProgramOMASAPIRequestBody{}";
    }
}
