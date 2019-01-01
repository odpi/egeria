/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.SubjectAreaMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.CategoryHierarchyLinkMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.TermCategorizationMapper;

public class GlossaryCategoryMapper extends ReferenceableMapper {

    public GlossaryCategoryMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "category",
                "Category",
                "GlossaryCategory",
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");
        addSimplePropertyMapping("short_description", "description");

        // The classes to use for mapping any relationships
        addRelationshipMapper(CategoryHierarchyLinkMapper.getInstance());
        addRelationshipMapper(TermCategorizationMapper.getInstance());

        // The classes to use for mapping any classifications
        addClassificationMapper(SubjectAreaMapper.getInstance());

    }

}
