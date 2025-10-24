/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PeerDefinitionProperties provides a details of how two peer governance definitions are related.
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceDriverLinkProperties.class, name = "GovernanceDriverLinkProperties"),
                @JsonSubTypes.Type(value = GovernancePolicyLinkProperties.class, name = "GovernancePolicyLinkProperties"),
                @JsonSubTypes.Type(value = GovernanceControlLinkProperties.class, name = "GovernanceControlLinkProperties"),
        })
public class PeerDefinitionProperties extends LabeledRelationshipProperties
{
    /**
     * Default constructor
     */
    public PeerDefinitionProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PeerDefinitionProperties(PeerDefinitionProperties template)
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
        return "PeerDefinitionProperties{" +
                "} " + super.toString();
    }
}
