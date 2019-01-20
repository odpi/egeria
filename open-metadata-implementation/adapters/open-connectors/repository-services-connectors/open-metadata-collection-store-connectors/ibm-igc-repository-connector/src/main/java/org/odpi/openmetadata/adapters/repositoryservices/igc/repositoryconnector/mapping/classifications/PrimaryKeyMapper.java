/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // Retrieve all assigned_to_terms relationships from this IGC object
        Boolean bSelectedPK = (Boolean) fromIgcObject.getPropertyByName("selected_primary_key");
        ReferenceList definedPK = (ReferenceList) fromIgcObject.getPropertyByName("defined_primary_key");

        // If there are no defined PKs, setup a classification only if the user has selected
        // this column as a primary key
        if (definedPK.getItems().isEmpty()) {
            if (bSelectedPK) {
                try {
                    InstanceProperties classificationProperties = new InstanceProperties();
                    classificationProperties.setProperty("name", EntityMapping.getPrimitivePropertyValue(fromIgcObject.getName()));
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
                    InstanceProperties classificationProperties = new InstanceProperties();
                    classificationProperties.setProperty("name", EntityMapping.getPrimitivePropertyValue(candidateKey.getName()));
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
        InstancePropertyValue value = matchClassificationProperties.getPropertyValue("name");
        if (value instanceof PrimitivePropertyValue) {
            PrimitivePropertyValue name = (PrimitivePropertyValue) value;
            String keyName = (String) name.getPrimitiveValue();
            IGCSearchCondition propertyCondition = new IGCSearchCondition(
                    "defined_primary_key.name",
                    "=",
                    keyName
            );
            igcSearchConditionSet.addCondition(propertyCondition);
        }

        igcSearchConditionSet.setMatchAnyCondition(true);

        return igcSearchConditionSet;

    }

}
