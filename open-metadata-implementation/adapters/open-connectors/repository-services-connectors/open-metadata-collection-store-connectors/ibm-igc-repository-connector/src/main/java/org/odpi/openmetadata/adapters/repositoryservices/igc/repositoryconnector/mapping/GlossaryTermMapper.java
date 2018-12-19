/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Identity;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles mapping between an IGC 'term' object and an OMRS 'GlossaryTerm' object.
 */
public class GlossaryTermMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(GlossaryTermMapper.class);

    private static final String P_SYNONYMS = "synonyms";
    private static final String P_TRANSLATIONS = "translations";
    private static final String C_CONFIDENTIALITY = "Confidentiality";

    /**
     * Sets the basic criteria to use for mapping between an IGC 'term' object and an OMRS 'GlossaryTerm' object.
     *
     * @param term the IGC 'term' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public GlossaryTermMapper(Reference term, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                term,
                "term",
                "GlossaryTerm",
                igcomrsRepositoryConnector,
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");
        addSimplePropertyMapping("short_description", "summary");
        addSimplePropertyMapping("long_description", "description");
        addSimplePropertyMapping("example", "examples");
        addSimplePropertyMapping("abbreviation", "abbreviation");
        addSimplePropertyMapping("usage", "usage");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                "parent_category",
                "TermCategorization",
                "terms",
                "categories"
        );
        addSimpleRelationshipMapping(
                "referencing_categories",
                "TermCategorization",
                "terms",
                "categories"
        );
        addSimpleRelationshipMapping(
                P_SYNONYMS,
                "Synonym",
                P_SYNONYMS,
                P_SYNONYMS
        );
        addSimpleRelationshipMapping(
                "related_terms",
                "RelatedTerm",
                "seeAlso",
                "seeAlso"
        );
        addSimpleRelationshipMapping(
                "replaces",
                "ReplacementTerm",
                "replacementTerms",
                "replacedTerms"
        );
        addSimpleRelationshipMapping(
                "replaced_by",
                "ReplacementTerm",
                "replacedTerms",
                "replacementTerms"
        );
        addSimpleRelationshipMapping(
                P_TRANSLATIONS,
                "Translation",
                P_TRANSLATIONS,
                P_TRANSLATIONS
        );
        addSimpleRelationshipMapping(
                "assigned_assets",
                "SemanticAssignment",
                "meaning",
                "assignedElements"
        );
        addSimpleRelationshipMapping(
                "has_a",
                "TermHASARelationship",
                "objects",
                "attributes"
        );
        addSimpleRelationshipMapping(
                "is_of",
                "TermHASARelationship",
                "attributes",
                "objects"
        );
        addSimpleRelationshipMapping(
                "is_a_type_of",
                "TermISATypeOFRelationship",
                "subtypes",
                "supertypes"
        );
        addSimpleRelationshipMapping(
                "has_types",
                "TermISATypeOFRelationship",
                "supertypes",
                "subtypes"
        );

        // Finally list any properties that will be used to map Classifications
        // (to do the actual mapping, implement the 'getMappedClassifications' function -- example below)
        addComplexIgcClassification("assigned_to_terms");
        addComplexOmrsClassification(C_CONFIDENTIALITY);

    }

    /**
     * We implement this method to apply any classifications -- since IGC itself doesn't have a "Classification"
     * asset type, we need to apply our own translation between how we're using other IGC asset types and the
     * Classification(s) we want them to represent in OMRS.
     * <br><br>
     * In this example of IGC 'term's, we've used the 'assigned_to_term' relationship from one term to any term
     * within a "Confidentiality" parent category to represent the "Confidentiality" classification in OMRS.
     * Therefore, any 'assigned_to_term' relationship on a term, where the assigned term is within a "Confidentiality"
     * parent category in IGC, will be mapped to a "Confidentiality" classification in OMRS.
     */
    @Override
    protected void getMappedClassifications() {

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // Retrieve all assigned_to_terms relationships from this IGC object
        ReferenceList assignedToTerms = (ReferenceList) me.getPropertyByName("assigned_to_terms");
        assignedToTerms.getAllPages(igcRestClient);

        // For each such relationship:
        for (Reference assignedTerm : assignedToTerms.getItems()) {

            // Retrieve the identity characteristics (ie. the parent category) of the related term
            Identity termIdentity = assignedTerm.getIdentity(igcRestClient);
            Identity catIdentity = termIdentity.getParentIdentity();

            // Only do something with the assigned term if its immediate parent category is named
            // "Confidentiality"
            if (catIdentity.toString().endsWith(C_CONFIDENTIALITY)) {

                InstanceProperties classificationProperties = new InstanceProperties();

                // TODO: setup dynamically from matching ordinal value in IGC?
                EnumPropertyValue level = new EnumPropertyValue();
                String confidentialityName = assignedTerm.getName();
                switch(confidentialityName) {
                    case "Unclassified":
                        level.setOrdinal(0);
                        break;
                    case "Internal":
                        level.setOrdinal(1);
                        break;
                    case "Confidential":
                        level.setOrdinal(2);
                        break;
                    case "Sensitive":
                        level.setOrdinal(3);
                        break;
                    case "Restricted":
                        level.setOrdinal(4);
                        break;
                    default:
                        level.setOrdinal(99);
                        break;
                }
                level.setSymbolicName(confidentialityName);
                classificationProperties.setProperty("level", level);

                try {
                    Classification classification = getMappedClassification(
                            C_CONFIDENTIALITY,
                            "Referenceable",
                            classificationProperties
                    );
                    classifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to map classification.", e);
                }

            }

        }

    }

}
