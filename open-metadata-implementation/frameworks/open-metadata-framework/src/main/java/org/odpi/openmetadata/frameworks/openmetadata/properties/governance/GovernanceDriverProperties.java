/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Governance drivers define the motivations behind the governance program. Often they show both the business
 * opportunities and the constraints that must guide the organization's activity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceStrategyProperties.class, name = "GovernanceStrategyProperties"),
                @JsonSubTypes.Type(value = BusinessImperativeProperties.class, name = "BusinessImperativeProperties"),
                @JsonSubTypes.Type(value = RegulationProperties.class, name = "RegulationProperties"),
                @JsonSubTypes.Type(value = RegulationArticleProperties.class, name = "RegulationArticleProperties"),
                @JsonSubTypes.Type(value = ThreatProperties.class, name = "ThreatProperties"),
        })
public class GovernanceDriverProperties extends GovernanceDefinitionProperties
{
    /**
     * Default Constructor
     */
    public GovernanceDriverProperties()
    {
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public GovernanceDriverProperties(GovernanceDriverProperties template)
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
        return "GovernanceDriverProperties{" +
                "} " + super.toString();
    }
}
