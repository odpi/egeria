/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.sustainability;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Define links between governance definitions
 */
public enum GovernanceDefinitionLink
{
    HANDLE_THREAT(GovernanceDefinition.SUSTAINABILITY_TREAT,
                  OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName,
                  GovernanceDefinition.SUSTAINABILITY_STRATEGY),

    SUPPORT_REGULATION(GovernanceDefinition.CSRD_REGULATION,
                       OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName,
                       GovernanceDefinition.SUSTAINABILITY_STRATEGY),

    STRATEGY_RESPONSE_1(GovernanceDefinition.SUSTAINABILITY_STRATEGY,
                        OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                        GovernanceDefinition.AVOID_HARMFUL_MATERIALS),

    STRATEGY_RESPONSE_2(GovernanceDefinition.SUSTAINABILITY_STRATEGY,
                        OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                        GovernanceDefinition.NEW_SUSTAINABILITY_DOMAIN),

    STRATEGY_RESPONSE_3(GovernanceDefinition.SUSTAINABILITY_STRATEGY,
                        OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                        GovernanceDefinition.GREENHOUSE_GASES_APPROACH),

    IMPL_1(GovernanceDefinition.AVOID_HARMFUL_MATERIALS,
           OpenMetadataType.GOVERNANCE_MECHANISM_RELATIONSHIP.typeName,
           GovernanceDefinition.REMOVE_BAD_COOLING_UNITS),

    IMPL_2(GovernanceDefinition.NEW_SUSTAINABILITY_DOMAIN,
           OpenMetadataType.GOVERNANCE_MECHANISM_RELATIONSHIP.typeName,
           GovernanceDefinition.SUSTAINABILITY_LEADER_RESPONSIBILITY),

    IMPL_3(GovernanceDefinition.NEW_SUSTAINABILITY_DOMAIN,
           OpenMetadataType.GOVERNANCE_MECHANISM_RELATIONSHIP.typeName,
           GovernanceDefinition.SUSTAINABILITY_CHAMPION_RESPONSIBILITY),

    IMPL_4(GovernanceDefinition.NEW_SUSTAINABILITY_DOMAIN,
           OpenMetadataType.GOVERNANCE_MECHANISM_RELATIONSHIP.typeName,
           GovernanceDefinition.SUSTAINABILITY_TECHNOLOGY_LEAD_RESPONSIBILITY),

    IMPL_5(GovernanceDefinition.GREENHOUSE_GASES_APPROACH,
           OpenMetadataType.GOVERNANCE_MECHANISM_RELATIONSHIP.typeName,
           GovernanceDefinition.SUSTAINABILITY_TECHNOLOGY_LEAD_RESPONSIBILITY),

    ;

    private final GovernanceDefinition parentDefinition;
    private final String               relationshipType;
    private final GovernanceDefinition childDefinition;

    GovernanceDefinitionLink(GovernanceDefinition parentDefinition,
                             String               relationshipType,
                             GovernanceDefinition childDefinition)
    {
        this.parentDefinition = parentDefinition;
        this.relationshipType = relationshipType;
        this.childDefinition  = childDefinition;
    }


    public GovernanceDefinition getParentDefinition()
    {
        return parentDefinition;
    }

    public String getRelationshipType()
    {
        return relationshipType;
    }

    public GovernanceDefinition getChildDefinition()
    {
        return childDefinition;
    }

    @Override
    public String toString()
    {
        return "GovernanceDefinitionLink{" +
                "parentDefinition=" + parentDefinition +
                ", relationshipType='" + relationshipType + '\'' +
                ", childDefinition=" + childDefinition +
                "}";
    }
}
