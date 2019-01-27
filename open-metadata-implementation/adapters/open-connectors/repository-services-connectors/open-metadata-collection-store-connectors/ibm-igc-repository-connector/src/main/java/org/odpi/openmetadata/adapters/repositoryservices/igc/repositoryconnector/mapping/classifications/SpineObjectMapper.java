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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Singleton defining the mapping to the OMRS "SpineObject" classification.
 */
public class SpineObjectMapper extends ClassificationMapping {

    private static final Logger log = LoggerFactory.getLogger(SpineObjectMapper.class);

    private static class Singleton {
        private static final SpineObjectMapper INSTANCE = new SpineObjectMapper();
    }
    public static SpineObjectMapper getInstance() {
        return SpineObjectMapper.Singleton.INSTANCE;
    }

    private SpineObjectMapper() {
        super(
                "term",
                "category_path",
                "SpineObject"
        );
    }

    /**
     * Implements the SpineObject classification for IGC 'term' assets. Any term with a "Spine Objects" ancestor in
     * its category_path will be considered to be a Spine Object (and therefore be given a SpineObject classification).
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

        Identity termIdentity = fromIgcObject.getIdentity(igcomrsRepositoryConnector.getIGCRestClient());
        Identity catIdentity = termIdentity.getParentIdentity();

        if (catIdentity.toString().endsWith("Spine Objects")) {

            try {
                Classification classification = getMappedClassification(
                        igcomrsRepositoryConnector,
                        "SpineObject",
                        "GlossaryTerm",
                        null,
                        fromIgcObject,
                        userId
                );
                classifications.add(classification);
            } catch (RepositoryErrorException e) {
                log.error("Unable to map SpineObject classification.", e);
            }

        }

    }

    /**
     * Search for SpineObject by looking at parent category of the term being under a "Spine Objects" category.
     * (There are no properties on the SpineObject classification, so no need to even check the provided
     * matchClassificationProperties.)
     *
     * @param matchClassificationProperties the criteria to use when searching for the classification
     * @return IGCSearchConditionSet - the IGC search criteria to find entities based on this classification
     */
    @Override
    public IGCSearchConditionSet getIGCSearchCriteria(InstanceProperties matchClassificationProperties) {

        IGCSearchCondition igcSearchCondition = new IGCSearchCondition(
                "parent_category.name",
                "=",
                "Spine Objects"
        );
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        igcSearchConditionSet.setMatchAnyCondition(false);
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

        if (classificationProperties != null || !classificationProperties.isEmpty()) {

            log.error("SpineObject classification has no properties, yet properties were included: {}", initialProperties);
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

        } else {

            IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

            IGCSearchCondition findCategory = new IGCSearchCondition(
                    "name",
                    "=",
                    "Spine Objects"
            );
            IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(findCategory);

            IGCSearch igcSearch = new IGCSearch("category", igcSearchConditionSet);
            ReferenceList results = igcRestClient.search(igcSearch);
            if (results == null || results.getPaging().getNumTotal() < 1) {
                log.error("No Spine Objects category found -- cannot continue.");
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
                log.warn("Found multiple Spine Objects categories, taking the first.");
            }
            String spineObjectCatRid = results.getItems().get(0).getId();
            IGCUpdate igcUpdate = new IGCUpdate(igcEntity.getId());
            igcUpdate.addExclusiveRelationship("parent_category", spineObjectCatRid);
            if (!igcRestClient.update(igcUpdate)) {
                log.error("Unable to update entity {} to add classification {}.", entityGUID, getOmrsClassificationType());
            }

        }

        IGCOMRSMetadataCollection collection = (IGCOMRSMetadataCollection) igcomrsRepositoryConnector.getMetadataCollection();
        return collection.getEntityDetail(userId, entityGUID, igcEntity);

    }

}
