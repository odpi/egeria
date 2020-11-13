/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A governance zone defines a group of assets.  The governance zone management defines
 * how the assets in the zone should be managed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GovernanceZoneInAction.class, name = "GovernanceZoneInAction")
})
public class GovernanceZoneDefinition extends GovernanceZoneProperties
{
    private static final long    serialVersionUID = 1L;

    private List<GovernanceDefinitionProperties> associatedGovernanceDefinitions = null;


    /**
     * Default Constructor
     */
    public GovernanceZoneDefinition()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZoneDefinition(GovernanceZoneDefinition template)
    {
        super(template);
    }


    /**
     * List the governance definitions that control assets in this zone.
     *
     * @return list of definitions
     */
    public List<GovernanceDefinitionProperties> getAssociatedGovernanceDefinitions()
    {
        return associatedGovernanceDefinitions;
    }

    public void setAssociatedGovernanceDefinitions(List<GovernanceDefinitionProperties> associatedGovernanceDefinitions)
    {
        this.associatedGovernanceDefinitions = associatedGovernanceDefinitions;
    }



    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceZoneDefinition{" +
                "associatedGovernanceDefinitions=" + associatedGovernanceDefinitions +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", criteria='" + getCriteria() + '\'' +
                ", scope='" + getScope() + '\'' +
                ", domainIdentifier=" + getDomainIdentifier() +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}
