/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestConstants;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCVersionEnum;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.DataClassMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton to map the OMRS "DataClassAssignment" relationship for IGC "data_class" assets, including both
 * detected and selected classifications, and the additional details on IGC "classification" assets.
 */
public class DataClassAssignmentMapper extends RelationshipMapping {

    private static final Logger log = LoggerFactory.getLogger(DataClassMapper.class);

    private static final String R_DATA_CLASS_ASSIGNMENT = "DataClassAssignment";
    private static final String P_THRESHOLD = "threshold";

    private static class Singleton {
        private static final DataClassAssignmentMapper INSTANCE = new DataClassAssignmentMapper();
    }
    public static DataClassAssignmentMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private DataClassAssignmentMapper() {
        super(
                IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE,
                "data_class",
                "detected_classifications",
                "classified_assets_detected",
                "DataClassAssignment",
                "elementsAssignedToDataClass",
                "dataClassesAssignedToElement"
        );
        setOptimalStart(OptimalStart.CUSTOM);
        addAlternativePropertyFromOne("selected_classification");
        addAlternativePropertyFromTwo("classifications_selected");
        setRelationshipLevelIgcAsset("classification");
    }

    /**
     * Retrieve the main_object asset expected from a classification asset.
     *
     * @param relationshipAsset the classification asset to translate into a main_object asset
     * @param igcRestClient REST connectivity to the IGC environment
     * @return Reference - the main_object asset
     */
    @Override
    public List<Reference> getProxyOneAssetFromAsset(Reference relationshipAsset, IGCRestClient igcRestClient) {
        String otherAssetType = relationshipAsset.getType();
        ArrayList<Reference> asList = new ArrayList<>();
        if (otherAssetType.equals("classification")) {
            Reference withDataClass = relationshipAsset.getAssetWithSubsetOfProperties(igcRestClient,
                    new String[]{ "data_class", "classifies_asset" });
            Reference classifiedObj = (Reference) withDataClass.getPropertyByName("classifies_asset");
            asList.add(classifiedObj);
        } else {
            log.debug("Not a classification asset, just returning as-is: {}", relationshipAsset);
            asList.add(relationshipAsset);
        }
        return asList;
    }

    /**
     * Retrieve the data_class asset expected from a classification asset.
     *
     * @param relationshipAsset the classification asset to translate into a data_class asset
     * @param igcRestClient REST connectivity to the IGC environment
     * @return Reference - the data_class asset
     */
    @Override
    public List<Reference> getProxyTwoAssetFromAsset(Reference relationshipAsset, IGCRestClient igcRestClient) {
        String otherAssetType = relationshipAsset.getType();
        ArrayList<Reference> asList = new ArrayList<>();
        if (otherAssetType.equals("classification")) {
            Reference withAsset = relationshipAsset.getAssetWithSubsetOfProperties(igcRestClient,
                    new String[]{ "data_class", "classifies_asset" });
            Reference dataClass = (Reference) withAsset.getPropertyByName("data_class");
            asList.add(dataClass);
        } else {
            log.debug("Not a classification asset, just returning as-is: {}", relationshipAsset);
            asList.add(relationshipAsset);
        }
        return asList;
    }

    /**
     * Custom implementation of the relationship between an a DataClass (data_class) and a Referenceable (main_object).
     * This is one of the few relationships in IGC that has relationship-specific properties handled by a separate
     * 'classification' object, so it must be handled using custom logic.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject
     * @param userId
     */
    @Override
    public void addMappedOMRSRelationships(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                           List<Relationship> relationships,
                                           Reference fromIgcObject,
                                           String userId) {

        String assetType = Reference.getAssetTypeForSearch(fromIgcObject.getType());

        if (assetType.equals("data_class")) {
            mapDetectedClassifications_fromDataClass(
                    igcomrsRepositoryConnector,
                    relationships,
                    fromIgcObject,
                    userId
            );
            mapSelectedClassifications_fromDataClass(
                    igcomrsRepositoryConnector,
                    relationships,
                    fromIgcObject,
                    userId
            );
        } else {
            mapDetectedClassifications_toDataClass(
                    igcomrsRepositoryConnector,
                    relationships,
                    fromIgcObject,
                    userId
            );
            mapSelectedClassifications_toDataClass(
                    igcomrsRepositoryConnector,
                    relationships,
                    fromIgcObject,
                    userId
            );
        }

    }

    /**
     * Map the detected classifications for objects classified by the provided data_class object.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject the data_class object
     * @param userId
     */
    private void mapDetectedClassifications_fromDataClass(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                   List<Relationship> relationships,
                                                   Reference fromIgcObject,
                                                   String userId) {

        // One of the few relationships in IGC that actually has properties of its own!
        // So we need to retrieve this relationship linking object (IGC type 'classification')
        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("data_class", "=", fromIgcObject.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        String[] classificationProperties = new String[]{
                "classifies_asset",
                "confidencePercent",
                P_THRESHOLD
        };
        IGCSearch igcSearch = new IGCSearch("classification", classificationProperties, igcSearchConditionSet);
        IGCVersionEnum igcVersion = igcomrsRepositoryConnector.getIGCVersion();
        if (igcVersion.isEqualTo(IGCVersionEnum.V11702) || igcVersion.isHigherThan(IGCVersionEnum.V11702)) {
            igcSearch.addProperty("value_frequency");
        }
        ReferenceList detectedClassifications = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);

        detectedClassifications.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        // For each of the detected classifications, create a new DataClassAssignment relationship
        for (Reference detectedClassification : detectedClassifications.getItems()) {

            Reference classifiedObj = (Reference) detectedClassification.getPropertyByName("classifies_asset");
            InstanceProperties relationshipProperties = new InstanceProperties();

            /* Only proceed with the classified object if it is not a 'main_object' asset
             * (in this scenario, 'main_object' represents ColumnAnalysisMaster objects that are not accessible
             *  and will throw bad request (400) REST API errors) */
            if (classifiedObj != null && !classifiedObj.getType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                try {

                    // Use 'classification' object to put RID of classification on the 'detected classification' relationships
                    Relationship relationship = getMappedRelationship(
                            igcomrsRepositoryConnector,
                            DataClassAssignmentMapper.getInstance(),
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                                    igcomrsRepositoryConnector.getRepositoryName(),
                                    R_DATA_CLASS_ASSIGNMENT),
                            classifiedObj,
                            fromIgcObject,
                            "detected_classifications",
                            userId,
                            detectedClassification.getId()
                    );

                    Number confidence = (Number) detectedClassification.getPropertyByName("confidencePercent");

                    /* Before adding to the overall set of relationships, setup the relationship properties
                     * we have in IGC from the 'classification' object. */
                    relationshipProperties.setProperty(
                            "confidence",
                            EntityMapping.getPrimitivePropertyValue(confidence.intValue())
                    );
                    relationshipProperties.setProperty(
                            P_THRESHOLD,
                            EntityMapping.getPrimitivePropertyValue(detectedClassification.getPropertyByName(P_THRESHOLD))
                    );
                    relationshipProperties.setProperty(
                            "partialMatch",
                            EntityMapping.getPrimitivePropertyValue((confidence.intValue() < 100))
                    );
                    if (igcVersion.isEqualTo(IGCVersionEnum.V11702) || igcVersion.isHigherThan(IGCVersionEnum.V11702)) {
                        relationshipProperties.setProperty(
                                "valueFrequency",
                                EntityMapping.getPrimitivePropertyValue(detectedClassification.getPropertyByName("value_frequency"))
                        );
                    }
                    EnumPropertyValue status = new EnumPropertyValue();
                    status.setSymbolicName("Discovered");
                    status.setOrdinal(0);
                    relationshipProperties.setProperty(
                            "status",
                            status
                    );

                    relationship.setProperties(relationshipProperties);
                    log.debug("mapDetectedClassifications_fromDataClass - adding relationship: {}", relationship);
                    relationships.add(relationship);

                } catch (RepositoryErrorException e) {
                    log.error("Unable to map relationship.", e);
                }
            }
        }

    }

    /**
     * Map the selected classifications for objects classified by the provided data_class object.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject the data_class object
     * @param userId
     */
    private void mapSelectedClassifications_fromDataClass(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                   List<Relationship> relationships,
                                                   Reference fromIgcObject,
                                                   String userId) {

        // (Note that in IGC these can only be retrieved by looking up all assets for which this data_class is selected,
        // they cannot be looked up as a relationship from the data_class object...  Therefore, start by searching
        // for any assets that list this data_class as their selected_classification
        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("selected_classification", "=", fromIgcObject.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        IGCSearch igcSearch = new IGCSearch("amazon_s3_data_file_field", igcSearchConditionSet);
        igcSearch.addType("data_file_field");
        igcSearch.addType("database_column");
        igcSearch.addProperty("selected_classification");
        igcSearch.addProperties(IGCRestConstants.getInstance().MODIFICATION_DETAILS);
        ReferenceList assetsWithSelected = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);

        assetsWithSelected.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        for (Reference assetWithSelected : assetsWithSelected.getItems()) {

            try {

                InstanceProperties relationshipProperties = new InstanceProperties();

                // Use 'data_class' object to put RID of data_class itself on the 'selected classification' relationships
                Relationship relationship = getMappedRelationship(
                        igcomrsRepositoryConnector,
                        DataClassAssignmentMapper.getInstance(),
                        (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                                igcomrsRepositoryConnector.getRepositoryName(),
                                R_DATA_CLASS_ASSIGNMENT),
                        assetWithSelected,
                        fromIgcObject,
                        "selected_classification",
                        userId
                );

                EnumPropertyValue status = new EnumPropertyValue();
                status.setSymbolicName("Proposed");
                status.setOrdinal(1);
                relationshipProperties.setProperty(
                        "status",
                        status
                );

                relationship.setProperties(relationshipProperties);
                log.debug("mapSelectedClassifications_fromDataClass - adding relationship: {}", relationship);
                relationships.add(relationship);

            } catch (RepositoryErrorException e) {
                log.error("Unable to map relationship.", e);
            }

        }

    }

    /**
     * Map the provided main_object object to its detected data classes.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject the main_object object
     * @param userId
     */
    private void mapDetectedClassifications_toDataClass(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                          List<Relationship> relationships,
                                                          Reference fromIgcObject,
                                                          String userId) {

        // One of the few relationships in IGC that actually has properties of its own!
        // So we need to retrieve this relationship linking object (IGC type 'classification')
        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("classifies_asset", "=", fromIgcObject.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        String[] classificationProperties = new String[]{
                "data_class",
                "confidencePercent",
                P_THRESHOLD
        };
        IGCSearch igcSearch = new IGCSearch("classification", classificationProperties, igcSearchConditionSet);
        IGCVersionEnum igcVersion = igcomrsRepositoryConnector.getIGCVersion();
        if (igcVersion.isEqualTo(IGCVersionEnum.V11702) || igcVersion.isHigherThan(IGCVersionEnum.V11702)) {
            igcSearch.addProperty("value_frequency");
        }
        ReferenceList detectedClassifications = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);

        detectedClassifications.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        // For each of the detected classifications, create a new DataClassAssignment relationship
        for (Reference detectedClassification : detectedClassifications.getItems()) {

            Reference dataClassObj = (Reference) detectedClassification.getPropertyByName("data_class");
            InstanceProperties relationshipProperties = new InstanceProperties();

            /* Only proceed with the classified object if it is not a 'main_object' asset
             * (in this scenario, 'main_object' represents ColumnAnalysisMaster objects that are not accessible
             *  and will throw bad request (400) REST API errors) */
            if (dataClassObj != null && !dataClassObj.getType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                try {

                    // Use 'classification' object to put RID of classification on the 'detected classification' relationships
                    Relationship relationship = getMappedRelationship(
                            igcomrsRepositoryConnector,
                            DataClassAssignmentMapper.getInstance(),
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                                    igcomrsRepositoryConnector.getRepositoryName(),
                                    R_DATA_CLASS_ASSIGNMENT),
                            fromIgcObject,
                            dataClassObj,
                            "detected_classifications",
                            userId,
                            detectedClassification.getId()
                    );

                    Number confidence = (Number) detectedClassification.getPropertyByName("confidencePercent");

                    /* Before adding to the overall set of relationships, setup the relationship properties
                     * we have in IGC from the 'classification' object. */
                    relationshipProperties.setProperty(
                            "confidence",
                            EntityMapping.getPrimitivePropertyValue(confidence.intValue())
                    );
                    relationshipProperties.setProperty(
                            P_THRESHOLD,
                            EntityMapping.getPrimitivePropertyValue(detectedClassification.getPropertyByName(P_THRESHOLD))
                    );
                    relationshipProperties.setProperty(
                            "partialMatch",
                            EntityMapping.getPrimitivePropertyValue((confidence.intValue() < 100))
                    );
                    if (igcVersion.isEqualTo(IGCVersionEnum.V11702) || igcVersion.isHigherThan(IGCVersionEnum.V11702)) {
                        relationshipProperties.setProperty(
                                "valueFrequency",
                                EntityMapping.getPrimitivePropertyValue(detectedClassification.getPropertyByName("value_frequency"))
                        );
                    }
                    EnumPropertyValue status = new EnumPropertyValue();
                    status.setSymbolicName("Discovered");
                    status.setOrdinal(0);
                    relationshipProperties.setProperty(
                            "status",
                            status
                    );

                    relationship.setProperties(relationshipProperties);
                    log.debug("mapDetectedClassifications_toDataClass - adding relationship: {}", relationship);
                    relationships.add(relationship);

                } catch (RepositoryErrorException e) {
                    log.error("Unable to map relationship.", e);
                }
            }
        }

    }

    /**
     * Map the provided main_object object to its selected classification.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject the main_object object
     * @param userId
     */
    private void mapSelectedClassifications_toDataClass(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                          List<Relationship> relationships,
                                                          Reference fromIgcObject,
                                                          String userId) {

        if (fromIgcObject.hasProperty("selected_classification")) {

            Reference withSelectedClassification = fromIgcObject.getAssetWithSubsetOfProperties(
                    igcomrsRepositoryConnector.getIGCRestClient(),
                    new String[]{ "selected_classification" });

            Reference selectedClassification = (Reference)withSelectedClassification.getPropertyByName("selected_classification");

            // If the reference itself (or its type) are null the relationship does not exist
            if (selectedClassification != null && selectedClassification.getType() != null) {
                try {

                    InstanceProperties relationshipProperties = new InstanceProperties();

                    Relationship relationship = getMappedRelationship(
                            igcomrsRepositoryConnector,
                            DataClassAssignmentMapper.getInstance(),
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                                    igcomrsRepositoryConnector.getRepositoryName(),
                                    R_DATA_CLASS_ASSIGNMENT),
                            fromIgcObject,
                            (Reference) withSelectedClassification.getPropertyByName("selected_classification"),
                            "selected_classification",
                            userId
                    );

                    EnumPropertyValue status = new EnumPropertyValue();
                    status.setSymbolicName("Proposed");
                    status.setOrdinal(1);
                    relationshipProperties.setProperty(
                            "status",
                            status
                    );

                    relationship.setProperties(relationshipProperties);
                    log.debug("mapSelectedClassifications_toDataClass - adding relationship: {}", relationship);
                    relationships.add(relationship);

                } catch (RepositoryErrorException e) {
                    log.error("Unable to map relationship.", e);
                }
            } else {
                log.debug("No selected_classification set for asset -- skipping.");
            }

        } else {
            log.info("Provided asset has no selected_classification property: {}", fromIgcObject.getId());
        }

    }

}
