/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.GovernancePolicyLinkMapper;

/**
 * Defines the mapping to the OMRS "GlossaryPolicy" entity.
 */
public class GovernancePolicyMapper extends ReferenceableMapper {

    public GovernancePolicyMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "information_governance_policy",
                "Information Governance Policy",
                "GovernancePolicy",
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "title");
        addSimplePropertyMapping("short_description", "summary");
        addSimplePropertyMapping("long_description", "description");

        // The list of relationships that should be mapped
        addRelationshipMapper(GovernancePolicyLinkMapper.getInstance());

    }

}
