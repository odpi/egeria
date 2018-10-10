/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A governance process is an automated multi-step process that coordinates people and other processing
 * in order to implement all or part of a policy.
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
public class GovernanceZone extends GovernanceZoneDefinition
{
    private List<GovernanceDefinition>  associatedGovernanceDefinitions = null;


    /**
     * Default Constructor
     */
    public GovernanceZone()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZone(GovernanceZone template)
    {
        super(template);
    }


    /**
     * List the governance definitions that control assets in this zone.
     *
     * @return list of definitions
     */
    public List<GovernanceDefinition> getAssociatedGovernanceDefinitions()
    {
        return associatedGovernanceDefinitions;
    }

    public void setAssociatedGovernanceDefinitions(List<GovernanceDefinition> associatedGovernanceDefinitions)
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
        return "GovernanceZone{" +
                "associatedGovernanceDefinitions=" + associatedGovernanceDefinitions +
                ", GUID='" + getGUID() + '\'' +
                ", classifications=" + getClassifications() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", criteria='" + getCriteria() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                '}';
    }
}
