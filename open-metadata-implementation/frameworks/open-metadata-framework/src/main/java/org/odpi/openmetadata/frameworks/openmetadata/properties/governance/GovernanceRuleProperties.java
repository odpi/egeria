/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceRuleProperties describes rule that the governance program wishes to enforce.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = NamingStandardRuleProperties.class, name = "NamingStandardRuleProperties"),
        })
public class GovernanceRuleProperties extends GovernanceControlProperties
{

    /**
     * Default constructor
     */
    public GovernanceRuleProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_RULE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceRuleProperties(GovernanceRuleProperties template)
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
        return "GovernanceRuleProperties{" +
                "} " + super.toString();
    }
}
