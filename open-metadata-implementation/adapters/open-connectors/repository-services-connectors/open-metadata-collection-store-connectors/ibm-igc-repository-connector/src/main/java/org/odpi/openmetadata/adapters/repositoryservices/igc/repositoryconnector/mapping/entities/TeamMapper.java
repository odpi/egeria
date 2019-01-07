/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.ContactThroughMapper_Team;

public class TeamMapper extends ReferenceableMapper {

    public TeamMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "group",
                "Group",
                "Team",
                userId,
                null,
                false
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("principal_id", "name");
        addSimplePropertyMapping("group_name", "description");

        // The classes to use for mapping any relationships
        addRelationshipMapper(ContactThroughMapper_Team.getInstance());

    }

}
