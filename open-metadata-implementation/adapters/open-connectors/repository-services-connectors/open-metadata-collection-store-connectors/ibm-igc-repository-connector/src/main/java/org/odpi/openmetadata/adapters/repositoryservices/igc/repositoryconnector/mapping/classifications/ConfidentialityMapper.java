/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Identity;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.attributes.ConfidentialityLevelMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Singleton defining the mapping to the OMRS "Confidentiality" classification.
 */
public class ConfidentialityMapper extends ClassificationMapping {

    private static final Logger log = LoggerFactory.getLogger(ConfidentialityMapper.class);

    private static class Singleton {
        private static final ConfidentialityMapper INSTANCE = new ConfidentialityMapper();
    }
    public static ConfidentialityMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private ConfidentialityMapper() {
        super(
                IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE,
                "assigned_to_terms",
                "Confidentiality"
        );
        addMappedOmrsProperty("level");
    }

    /**
     * Implements the "Confidentiality" classification for IGC objects (by default we only apply to terms, but could
     * apply to any IGC asset type). We use the 'assigned_to_term' relationship from one term to any term
     * within a "Confidentiality" parent category to represent the "Confidentiality" classification in OMRS.
     * Therefore, any 'assigned_to_term' relationship on a term, where the assigned term is within a "Confidentiality"
     * parent category in IGC, will be mapped to a "Confidentiality" classification in OMRS.
     *
     * @param igcomrsRepositoryConnector
     * @param classifications
     * @param fromIgcObject
     * @param userId
     */
    @Override
    public void addMappedOMRSClassifications(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                             List<Classification> classifications,
                                             Reference fromIgcObject,
                                             String userId) {

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // Retrieve all assigned_to_terms relationships from this IGC object
        ReferenceList assignedToTerms = (ReferenceList) fromIgcObject.getPropertyByName("assigned_to_terms");
        assignedToTerms.getAllPages(igcRestClient);

        // For each such relationship:
        for (Reference assignedTerm : assignedToTerms.getItems()) {

            // Retrieve the identity characteristics (ie. the parent category) of the related term
            Identity termIdentity = assignedTerm.getIdentity(igcRestClient);
            Identity catIdentity = termIdentity.getParentIdentity();

            // Only do something with the assigned term if its immediate parent category is named
            // "Confidentiality"
            if (catIdentity.toString().endsWith("Confidentiality")) {

                InstanceProperties classificationProperties = new InstanceProperties();
                ConfidentialityLevelMapper confidentialityLevelMapper = ConfidentialityLevelMapper.getInstance();
                EnumPropertyValue level = confidentialityLevelMapper.getEnumMappingByIgcValue(assignedTerm.getName());
                classificationProperties.setProperty("level", level);

                try {
                    Classification classification = getMappedClassification(
                            igcomrsRepositoryConnector,
                            "Confidentiality",
                            "Referenceable",
                            classificationProperties,
                            fromIgcObject,
                            userId
                    );
                    classifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to map Confidentiality classification.", e);
                }

            }

        }

    }

    /**
     * Search for Confidentiality by looking for a term assignment where the assigned term both sits under a
     * Confidentiality parent category and has a name matching the confidentiality level. (Note that only the
     * 'level' classification property is used from matchClassificationProperties, as no other properties are
     * implemented in IGC.)
     *
     * @param matchClassificationProperties the criteria to use when searching for the classification
     * @return IGCSearchConditionSet - the IGC search criteria to find entities based on this classification
     */
    @Override
    public IGCSearchConditionSet getIGCSearchCriteria(InstanceProperties matchClassificationProperties) {

        IGCSearchCondition igcSearchCondition = new IGCSearchCondition(
                "assigned_to_terms.parent_category.name",
                "=",
                "Confidentiality"
        );
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);

        // We can only search by level, so we will ignore all other properties
        InstancePropertyValue value = matchClassificationProperties.getPropertyValue("level");
        if (value instanceof EnumPropertyValue) {
            EnumPropertyValue level = (EnumPropertyValue) value;
            ConfidentialityLevelMapper confidentialityLevelMapper = ConfidentialityLevelMapper.getInstance();
            String igcTermName = confidentialityLevelMapper.getIgcValueForOrdinal(level.getOrdinal());
            IGCSearchCondition propertyCondition = new IGCSearchCondition(
                    "assigned_to_terms.name",
                    "=",
                    igcTermName
            );
            igcSearchConditionSet.addCondition(propertyCondition);
        }

        igcSearchConditionSet.setMatchAnyCondition(false);

        return igcSearchConditionSet;

    }

}
