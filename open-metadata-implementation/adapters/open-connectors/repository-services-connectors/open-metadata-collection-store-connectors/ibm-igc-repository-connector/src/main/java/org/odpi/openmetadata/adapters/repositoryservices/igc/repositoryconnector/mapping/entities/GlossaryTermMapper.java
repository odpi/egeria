/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Identity;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlossaryTermMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(GlossaryTermMapper.class);

    private static final String T_GLOSSARY_TERM = "GlossaryTerm";
    private static final String C_CONFIDENTIALITY = "Confidentiality";
    private static final String C_SPINE_OBJECT = "SpineObject";

    public GlossaryTermMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "term",
                "Term",
                T_GLOSSARY_TERM,
                userId
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
        
        // Finally list any properties that will be used to map Classifications
        // (to do the actual mapping, implement the 'getMappedClassifications' function -- example below)
        addComplexIgcClassification("assigned_to_terms");
        addComplexOmrsClassification(C_CONFIDENTIALITY);
        addComplexOmrsClassification(C_SPINE_OBJECT);

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
     * <br><br>
     * Additionally, any term whose parent category is "Spine Objects" will be classified as an OMRS "SpineObject".
     */
    @Override
    protected void getMappedClassifications() {

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        addConfidentialityClassification(igcRestClient);
        addSpineObjectClassification(igcRestClient);

    }

    /**
     * Implements the Confidentiality classification.
     *
     * @param igcRestClient REST connectivity to the IGC environment
     */
    private void addConfidentialityClassification(IGCRestClient igcRestClient) {

        // Retrieve all assigned_to_terms relationships from this IGC object
        ReferenceList assignedToTerms = (ReferenceList) igcEntity.getPropertyByName("assigned_to_terms");
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
                    omrsClassifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to map Confidentiality classification.", e);
                }

            }

        }

    }

    /**
     * Implements the SpineObject classification.
     *
     * @param igcRestClient REST connectivity to the IGC environment
     */
    private void addSpineObjectClassification(IGCRestClient igcRestClient) {

        Identity termIdentity = igcEntity.getIdentity(igcRestClient);
        Identity catIdentity = termIdentity.getParentIdentity();

        if (catIdentity.toString().endsWith("Spine Objects")) {

            try {
                Classification classification = getMappedClassification(
                        C_SPINE_OBJECT,
                        T_GLOSSARY_TERM,
                        null
                );
                omrsClassifications.add(classification);
            } catch (RepositoryErrorException e) {
                log.error("Unable to map SpineObject classification.", e);
            }

        }

    }

}
