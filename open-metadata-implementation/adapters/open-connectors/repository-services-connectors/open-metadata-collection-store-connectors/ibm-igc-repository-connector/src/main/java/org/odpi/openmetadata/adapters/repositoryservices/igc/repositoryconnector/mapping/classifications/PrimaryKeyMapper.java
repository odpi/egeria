/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Singleton defining the mapping to the OMRS "PrimaryKey" classification.
 */
public class PrimaryKeyMapper extends ClassificationMapping {

    private static final Logger log = LoggerFactory.getLogger(ConfidentialityMapper.class);

    private static final String C_PRIMARY_KEY = "PrimaryKey";
    private static final String T_RELATIONAL_COLUMN = "RelationalColumn";

    private static class Singleton {
        private static final PrimaryKeyMapper INSTANCE = new PrimaryKeyMapper();
    }
    public static PrimaryKeyMapper getInstance() {
        return PrimaryKeyMapper.Singleton.INSTANCE;
    }

    private PrimaryKeyMapper() {
        super(
                "database_column",
                "defined_primary_key",
                C_PRIMARY_KEY
        );
        addIgcRelationshipProperty("selected_primary_key");
        addMappedOmrsProperty("name");
    }

    /**
     * Implements the "PrimaryKey" OMRS classification for IGC database_column assets.
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

        final String methodName = "addMappedOMRSClassifications";
        OMRSRepositoryHelper repositoryHelper = igcomrsRepositoryConnector.getRepositoryHelper();
        String repositoryName = igcomrsRepositoryConnector.getRepositoryName();

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // Retrieve all assigned_to_terms relationships from this IGC object
        Boolean bSelectedPK = (Boolean) fromIgcObject.getPropertyByName("selected_primary_key");
        ReferenceList definedPK = (ReferenceList) fromIgcObject.getPropertyByName("defined_primary_key");

        // If there are no defined PKs, setup a classification only if the user has selected
        // this column as a primary key
        if (definedPK.getItems().isEmpty()) {
            if (bSelectedPK) {
                try {
                    InstanceProperties classificationProperties = repositoryHelper.addStringPropertyToInstance(
                            repositoryName,
                            null,
                            "name",
                            fromIgcObject.getName(),
                            methodName
                    );
                    Classification classification = getMappedClassification(
                            igcomrsRepositoryConnector,
                            C_PRIMARY_KEY,
                            T_RELATIONAL_COLUMN,
                            classificationProperties,
                            fromIgcObject,
                            userId
                    );
                    classifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to create classification.", e);
                }
            }
        } else {

            // Otherwise setup primary key classifications for each defined candidate key
            definedPK.getAllPages(igcRestClient);
            for (Reference candidateKey : definedPK.getItems()) {

                try {
                    InstanceProperties classificationProperties = repositoryHelper.addStringPropertyToInstance(
                            repositoryName,
                            null,
                            "name",
                            candidateKey.getName(),
                            methodName
                    );
                    Classification classification = getMappedClassification(
                            igcomrsRepositoryConnector,
                            C_PRIMARY_KEY,
                            T_RELATIONAL_COLUMN,
                            classificationProperties,
                            fromIgcObject,
                            userId
                    );
                    classifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to create classification.", e);
                }

            }

        }

    }

    /**
     * Search for PrimaryKey by looking for either a defined or selected primary key in IGC.
     *
     * @param matchClassificationProperties the criteria to use when searching for the classification
     * @return IGCSearchConditionSet - the IGC search criteria to find entities based on this classification
     */
    @Override
    public IGCSearchConditionSet getIGCSearchCriteria(InstanceProperties matchClassificationProperties) {

        IGCSearchCondition igcSearchCondition = new IGCSearchCondition(
                "selected_primary_key",
                "=",
                "true"
        );
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);

        // We can only search by name, so we will ignore all other properties
        if (matchClassificationProperties != null) {
            Map<String, InstancePropertyValue> properties = matchClassificationProperties.getInstanceProperties();
            if (properties.containsKey("name")) {
                PrimitivePropertyValue name = (PrimitivePropertyValue) properties.get("name");
                String keyName = (String) name.getPrimitiveValue();
                IGCSearchCondition propertyCondition = new IGCSearchCondition(
                        "defined_primary_key.name",
                        "=",
                        keyName
                );
                igcSearchConditionSet.addCondition(propertyCondition);
                igcSearchConditionSet.setMatchAnyCondition(true);
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
                                                    String userId) throws RepositoryErrorException {

        final String methodName = "addClassificationToIGCAsset";

        log.error("PrimaryKey classification cannot be changed through IGC REST API.");
        IGCOMRSErrorCode errorCode = IGCOMRSErrorCode.CLASSIFICATION_NOT_EDITABLE;
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

    }

}
