/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.ConfidentialityMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.SpineObjectMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.*;

/**
 * Defines the mapping to the OMRS "GlossaryTerm" entity.
 */
public class GlossaryTermMapper extends ReferenceableMapper {

    public GlossaryTermMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "term",
                "Term",
                "GlossaryTerm",
                userId,
                null
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");
        addSimplePropertyMapping("short_description", "summary");
        addSimplePropertyMapping("long_description", "description");
        addSimplePropertyMapping("example", "examples");
        addSimplePropertyMapping("abbreviation", "abbreviation");
        addSimplePropertyMapping("usage", "usage");

        // The classes to use for mapping any relationships
        addRelationshipMapper(TermCategorizationMapper.getInstance());
        addRelationshipMapper(SynonymMapper.getInstance());
        addRelationshipMapper(RelatedTermMapper.getInstance());
        addRelationshipMapper(ReplacementTermMapper.getInstance());
        addRelationshipMapper(TranslationMapper.getInstance());
        addRelationshipMapper(TermHASARelationshipMapper.getInstance());
        addRelationshipMapper(TermISATypeOFRelationshipMapper.getInstance());

        // The classes to use for mapping any classifications
        addClassificationMapper(ConfidentialityMapper.getInstance());
        addClassificationMapper(SpineObjectMapper.getInstance());

    }

}
