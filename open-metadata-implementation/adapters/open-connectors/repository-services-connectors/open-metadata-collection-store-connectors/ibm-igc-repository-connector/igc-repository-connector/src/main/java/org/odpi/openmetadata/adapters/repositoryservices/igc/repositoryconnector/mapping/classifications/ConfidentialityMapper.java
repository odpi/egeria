/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Identity;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.update.IGCUpdate;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.attributes.ConfidentialityLevelMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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
        // Exclude IGC types that do not have 'assigned_to_terms'
        addExcludedIgcAssetType("connector");
        addExcludedIgcAssetType("data_connection");
        addExcludedIgcAssetType("group");
        addExcludedIgcAssetType("information_governance_policy");
        addExcludedIgcAssetType("label");
        addExcludedIgcAssetType("user");
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
        if (matchClassificationProperties != null) {
            Map<String, InstancePropertyValue> properties = matchClassificationProperties.getInstanceProperties();
            if (properties.containsKey("level")) {
                EnumPropertyValue level = (EnumPropertyValue) properties.get("level");
                ConfidentialityLevelMapper confidentialityLevelMapper = ConfidentialityLevelMapper.getInstance();
                String igcTermName = confidentialityLevelMapper.getIgcValueForOrdinal(level.getOrdinal());
                IGCSearchCondition propertyCondition = new IGCSearchCondition(
                        "assigned_to_terms.name",
                        "=",
                        igcTermName
                );
                igcSearchConditionSet.addCondition(propertyCondition);
                igcSearchConditionSet.setMatchAnyCondition(false);
            }
        }

        return igcSearchConditionSet;

    }

    /**
     * Implement this method to define how to add an OMRS classification to an existing IGC asset. (Since IGC has no
     * actual concept of classification, this is left as a method to-be-implemented depending on how the implementation
     * desires the classification to be represented within IGC.)
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository via OMRS connector
     * @param igcEntity the IGC object to which to add the OMRS classification
     * @param entityGUID the GUID of the OMRS entity (ie. including any prefix)
     * @param initialProperties the set of classification-specific properties to add to the classification
     * @param userId the user requesting the classification to be added (currently unused)
     * @return EntityDetail the updated entity with the OMRS classification added
     * @throws RepositoryErrorException
     */
    @Override
    public EntityDetail addClassificationToIGCAsset(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                    Reference igcEntity,
                                                    String entityGUID,
                                                    InstanceProperties initialProperties,
                                                    String userId) throws RepositoryErrorException, EntityNotKnownException {

        final String methodName = "addClassificationToIGCAsset";

        Map<String, InstancePropertyValue> classificationProperties = null;
        if (initialProperties != null) {
            classificationProperties = initialProperties.getInstanceProperties();
        }

        if (classificationProperties == null || classificationProperties.isEmpty()) {

            log.error("Confidentiality classification requires the 'level' property for IGC.");
            IGCOMRSErrorCode errorCode = IGCOMRSErrorCode.CLASSIFICATION_INSUFFICIENT_PROPERTIES;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    getOmrsClassificationType(),
                    entityGUID
            );
            throw new RepositoryErrorException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );

        } else if (classificationProperties.size() == 1 && classificationProperties.containsKey("level")) {

            // Goldilocks scenario - we have a 'level' and only a 'level'
            EnumPropertyValue level = (EnumPropertyValue) classificationProperties.get("level");
            ConfidentialityLevelMapper confidentialityLevelMapper = ConfidentialityLevelMapper.getInstance();
            String igcTermName = confidentialityLevelMapper.getIgcValueForOrdinal(level.getOrdinal());

            IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

            // TODO: do we first need to find any other Confidentiality classification and remove that?
            //  (ie. they should probably be mutually-exclusive?)

            IGCSearchCondition findTerm = new IGCSearchCondition(
                    "name",
                    "=",
                    igcTermName
            );
            IGCSearchCondition inConfidentiality = new IGCSearchCondition(
                    "parent_category.name",
                    "=",
                    "Confidentiality"
            );
            IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(findTerm);
            igcSearchConditionSet.addCondition(inConfidentiality);
            igcSearchConditionSet.setMatchAnyCondition(false);

            IGCSearch igcSearch = new IGCSearch("term", igcSearchConditionSet);
            ReferenceList results = igcRestClient.search(igcSearch);
            if (results == null || results.getPaging().getNumTotal() < 1) {
                log.error("No Confidentiality found with level: {}", igcTermName);
                IGCOMRSErrorCode errorCode = IGCOMRSErrorCode.CLASSIFICATION_NOT_FOUND;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        getOmrsClassificationType(),
                        entityGUID
                );
                throw new RepositoryErrorException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction()
                );
            } else if (results.getPaging().getNumTotal() > 1) {
                log.warn("Found multiple Confidentiality terms matching {}, taking the first.", igcTermName);
            }
            String confidentialityTermRid = results.getItems().get(0).getId();
            IGCUpdate igcUpdate = new IGCUpdate(igcEntity.getId());
            igcUpdate.addRelationship("assigned_to_terms", confidentialityTermRid);
            igcUpdate.setRelationshipUpdateMode(IGCUpdate.UpdateMode.APPEND);
            if (!igcRestClient.update(igcUpdate)) {
                log.error("Unable to update entity {} to add classification {}.", entityGUID, getOmrsClassificationType());
            }

        } else {

            log.error("More than only a 'level' property was provided for the Confidentiality classification.");
            IGCOMRSErrorCode errorCode = IGCOMRSErrorCode.CLASSIFICATION_EXCEEDS_REPOSITORY;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    getOmrsClassificationType(),
                    getIgcAssetType()
            );
            throw new RepositoryErrorException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );

        }

        IGCOMRSMetadataCollection collection = (IGCOMRSMetadataCollection) igcomrsRepositoryConnector.getMetadataCollection();
        return collection.getEntityDetail(userId, entityGUID, igcEntity);

    }

}
