/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupportedRequestParameter characterises one of the request parameters supported by a specific
 * governance service or governance action.
 * This enables the capability of a governance service/action to be correctly supplied with the
 * parameters that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedRequestParameter extends ActionTargetType
{
    /**
     * Default constructor
     */
    public SupportedRequestParameter()
    {
        super();
        super.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedRequestParameter(ActionTargetType template)
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
        return "SupportedRequestParameter{" +
                "} " + super.toString();
    }
}
