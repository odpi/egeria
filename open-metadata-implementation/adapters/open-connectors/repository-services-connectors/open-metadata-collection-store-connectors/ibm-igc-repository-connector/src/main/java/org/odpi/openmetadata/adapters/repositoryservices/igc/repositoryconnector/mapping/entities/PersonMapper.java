/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.ContactThroughMapper_Person;

public class PersonMapper extends ReferenceableMapper {

    public PersonMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "user",
                "User",
                "Person",
                userId,
                null,
                false
        );
        addOtherIGCAssetType("steward_user");
        addOtherIGCAssetType("non_steward_user");

        // The list of properties that should be mapped
        addSimplePropertyMapping("principal_id", "name");
        addSimplePropertyMapping("full_name", "fullName");
        addSimplePropertyMapping("job_title", "jobTitle");

        // The classes to use for mapping any relationships
        addRelationshipMapper(ContactThroughMapper_Person.getInstance());

    }

}
