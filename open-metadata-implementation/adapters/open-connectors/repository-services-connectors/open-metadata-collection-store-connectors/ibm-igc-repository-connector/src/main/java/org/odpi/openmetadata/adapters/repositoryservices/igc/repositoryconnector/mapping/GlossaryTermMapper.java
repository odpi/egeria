/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Identity;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

/**
 * Handles mapping between an IGC 'term' object and an OMRS 'GlossaryTerm' object.
 */
public class GlossaryTermMapper extends ReferenceableMapper {

    /**
     * Sets the basic criteria to use for mapping between an IGC 'term' object and an OMRS 'GlossaryTerm' object.
     *
     * @param term the IGC 'term' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public GlossaryTermMapper(MainObject term, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                term,
                "term",
                "GlossaryTerm",
                igcomrsRepositoryConnector,
                userId
        );

        // The list of properties that should be mapped
        PROPERTIES.put("name", "displayName");
        PROPERTIES.put("short_description", "summary");
        PROPERTIES.put("long_description", "description");
        PROPERTIES.put("example", "examples");
        PROPERTIES.put("abbreviation", "abbreviation");
        PROPERTIES.put("usage", "usage");

        // The list of relationships that should be mapped
        RELATIONSHIPS.put(
                "parent_category",
                "TermCategorization",
                "terms",
                "categories"
        );
        RELATIONSHIPS.put(
                "referencing_categories",
                "TermCategorization",
                "terms",
                "categories"
        );
        RELATIONSHIPS.put(
                "synonyms",
                "Synonym",
                "synonyms",
                "synonyms"
        );
        RELATIONSHIPS.put(
                "related_terms",
                "RelatedTerm",
                "seeAlso",
                "seeAlso"
        );
        RELATIONSHIPS.put(
                "replaces",
                "ReplacementTerm",
                "replacementTerms",
                "replacedTerms"
        );
        RELATIONSHIPS.put(
                "replaced_by",
                "ReplacementTerm",
                "replacedTerms",
                "replacementTerms"
        );
        RELATIONSHIPS.put(
                "translations",
                "Translation",
                "translations",
                "translations"
        );
        RELATIONSHIPS.put(
                "assigned_assets",
                "SemanticAssignment",
                "meaning",
                "assignedElements"
        );
        RELATIONSHIPS.put(
                "has_a",
                "TermHASARelationship",
                "objects",
                "attributes"
        );
        RELATIONSHIPS.put(
                "is_of",
                "TermHASARelationship",
                "attributes",
                "objects"
        );
        RELATIONSHIPS.put(
                "is_a_type_of",
                "TermISATypeOFRelationship",
                "subtypes",
                "supertypes"
        );
        RELATIONSHIPS.put(
                "has_types",
                "TermISATypeOFRelationship",
                "supertypes",
                "subtypes"
        );

        // Finally list any properties that will be used to map Classifications
        // (to do the actual mapping, implement the 'getMappedClassifications' function -- example below)
        CLASSIFICATION_PROPERTIES.add("assigned_to_terms");

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
    protected void getMappedClassifications() {

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // Retrieve all assigned_to_terms relationships from this IGC object
        ReferenceList assignedToTerms = ((MainObject) me).getAssignedToTerms();
        assignedToTerms.getAllPages(igcRestClient);

        // For each such relationship:
        for (Reference reference : assignedToTerms.getItems()) {

            // Retrieve the identity characteristics (ie. the parent category) of the related term
            MainObject assignedTerm = (MainObject) reference;
            Identity termIdentity = assignedTerm.getIdentity(igcRestClient);
            Identity catIdentity = termIdentity.getParentIdentity();

            // Only do something with the assigned term if its immediate parent category is named
            // "Confidentiality"
            if (catIdentity.toString().endsWith("Confidentiality")) {

                InstanceProperties classificationProperties = new InstanceProperties();

                // TODO: add ordinal (ie. and to sample data in IGC)
                EnumPropertyValue level = new EnumPropertyValue();
                level.setSymbolicName(assignedTerm.getName());
                classificationProperties.setProperty("level", level);

                try {
                    // In such cases, create a new OMRS "Confidentiality" classification,
                    // and add this to the list of classifications for this mapping
                    Classification classification = igcomrsRepositoryConnector.getRepositoryHelper().getNewClassification(
                            SOURCE_NAME,
                            userId,
                            "Confidentiality",
                            "Referenceable",
                            ClassificationOrigin.ASSIGNED,
                            null,
                            classificationProperties
                    );
                    classifications.add(classification);
                } catch (TypeErrorException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
