/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RequestTypeType characterises one of the governance request types supported by a specific governance service.
 * This enables the capability of a governance service to be correctly matched to the resources and
 * elements that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SupportedRequestType.class, name = "SupportedRequestType"),
        })
public class RequestTypeType extends SpecificationProperty
{
    /**
     * Default constructor
     */
    public RequestTypeType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestTypeType(SpecificationProperty template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RequestTypeType{" +
                "} " + super.toString();
    }
}
