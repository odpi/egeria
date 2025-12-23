/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Governance policies outline how a governance domain will provide support to the governance drivers.
 * They are said to be the "responses to the challenges proposed by the governance drivers".
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernancePrincipleProperties.class, name = "GovernancePrincipleProperties"),
                @JsonSubTypes.Type(value = GovernanceObligationProperties.class, name = "GovernanceObligationProperties"),
                @JsonSubTypes.Type(value = GovernanceApproachProperties.class, name = "GovernanceApproachProperties"),
        })
public class GovernancePolicyProperties extends GovernanceDefinitionProperties
{
    /**
     * Default Constructor
     */
    public GovernancePolicyProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_POLICY.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public GovernancePolicyProperties(GovernancePolicyProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernancePolicyProperties{" +
                "} " + super.toString();
    }
}
