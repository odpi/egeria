/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttachedTagMapper;

/**
 * Defines the mapping to the OMRS "InformalTag" entity.
 */
public class InformalTagMapper extends ReferenceableMapper {

    public InformalTagMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "label",
                "Label",
                "InformalTag",
                userId,
                null,
                false
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "tagName");
        addSimplePropertyMapping("description", "tagDescription");

        // The list of relationships that should be mapped
        addRelationshipMapper(AttachedTagMapper.getInstance());

    }

}
