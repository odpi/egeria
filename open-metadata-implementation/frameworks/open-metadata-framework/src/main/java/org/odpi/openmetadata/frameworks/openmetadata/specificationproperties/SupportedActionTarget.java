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
 * SupportedActionTarget characterises one of the types of open metadata element supported by a specific
 * governance service or governance action.
 * This enables the capability of a governance service/action to be correctly supplied with the
 * resources and elements that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedActionTarget extends ActionTargetType
{
    /**
     * Default constructor
     */
    public SupportedActionTarget()
    {
        super();
        super.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_ACTION_TARGET);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedActionTarget(ActionTargetType template)
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
        return "SupportedActionTarget{" +
                "} " + super.toString();
    }
}
