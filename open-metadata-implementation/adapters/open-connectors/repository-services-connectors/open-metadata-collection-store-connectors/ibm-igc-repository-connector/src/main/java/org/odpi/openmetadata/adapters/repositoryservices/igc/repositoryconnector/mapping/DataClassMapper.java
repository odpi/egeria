/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class DataClassMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(DataClassMapper.class);

    private static final String R_DATA_CLASS_ASSIGNMENT = "DataClassAssignment";
    private static final String P_THRESHOLD = "threshold";

    /**
     * Sets the basic criteria to use for mapping between an IGC 'data_class' object and an OMRS 'DataClass' object.
     *
     * @param dataClass the IGC 'data_class' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public DataClassMapper(Reference dataClass, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dataClass,
                "data_class",
                "DataClass",
                igcomrsRepositoryConnector, userId
        );

        // IGC 'data_class' is one of the few objects with a relationship-specific asset type associated,
        // so we need to ensure that is also added to the assets to be handled by this mapper
        addOtherIGCAssetType("classification");

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");
        addSimplePropertyMapping("class_code", "classCode");
        addSimplePropertyMapping("default_threshold", "defaultThreshold");
        addSimplePropertyMapping("example", "example");

        // These properties need complex mappings, handled by 'complexPropertyMappings' method below
        addComplexIgcProperty("data_type_filter_elements_enum");
        addComplexIgcProperty("data_class_type_single");
        addComplexIgcProperty("java_class_name_single");
        addComplexIgcProperty("regular_expression_single");
        addComplexIgcProperty("valid_value_strings");
        addComplexIgcProperty("validValueReferenceFile");
        addComplexOmrsProperty("dataType");
        addComplexOmrsProperty("specificationDetails");
        addComplexOmrsProperty("specification");

        // Further expand the complex properties if we're on v11.7 (and these are then available)
        if (igcomrsRepositoryConnector.getIGCVersion().equals(IGCRestClient.VERSION_117)) {
            addComplexIgcProperty("expression");
            addComplexIgcProperty("script");
            addComplexIgcProperty("provider");
            addComplexIgcProperty("filters");
            addComplexOmrsProperty("userDefined");
        }

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                "parent_data_class",
                "DataClassHierarchy",
                "subDataClasses",
                "superDataClass"
        );
        addSimpleRelationshipMapping(
                "contains_data_classes",
                "DataClassHierarchy",
                "superDataClass",
                "subDataClasses"
        );

        // These relationships need complex mappings, handled by 'complexRelationshipMappings' method below
        addComplexIgcRelationship("classified_assets_detected");
        addComplexIgcRelationship("classifications_selected");
        addComplexOmrsRelationship(R_DATA_CLASS_ASSIGNMENT);

    }

    /**
     * No classifications implemented for DataClasses.
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do
    }

    /**
     * Implement any complex property mappings that cannot be simply mapped one-to-one.
     */
    @Override
    protected void complexPropertyMappings(InstanceProperties instanceProperties, Method getPropertyByName) {

        try {

            /*
             * setup the OMRS 'dataType' property
             */
            // There can be multiple data types defined on an IGC data class...
            ArrayList<String> dataTypes = (ArrayList<String>) getPropertyByName.invoke(me, "data_type_filter_elements_enum");
            String dataType = null;
            for (String type : dataTypes) {
                // We'll take the first dataType we find to start with...
                if (dataType == null) {
                    dataType = type;
                } else if (type.equals("string") || !type.equals(dataType)) {
                    // But if we find any others, or we find string, we can safely set to "string"
                    // as a catch-all and then short-circuit
                    dataType = "string";
                    break;
                }
            }
            instanceProperties.setProperty(
                    "dataType",
                    ReferenceMapper.getPrimitivePropertyValue(dataType)
            );

            /*
             * setup the OMRS 'specificationDetails' property
             */
            String dataClassType = (String) getPropertyByName.invoke(me, "data_class_type_single");
            instanceProperties.setProperty(
                    "specificationDetails",
                    ReferenceMapper.getPrimitivePropertyValue(dataClassType)
            );

            /*
             * setup the OMRS 'specification' property
             */
            // There are many different flavours of IGC data classes, so the expression used can vary widely...
            String dataClassDetails = "";
            switch(dataClassType) {
                case "Regex":
                    dataClassDetails = (String) getPropertyByName.invoke(me, "regular_expression_single");
                    break;
                case "ValidValues":
                    dataClassDetails = (String) getPropertyByName.invoke(me, "valid_value_strings");
                    if (dataClassDetails == null || dataClassDetails.equals("null") || dataClassDetails.equals("")) {
                        dataClassDetails = (String) getPropertyByName.invoke(me, "validValueReferenceFile");
                    }
                    break;
                case "Script":
                    dataClassDetails = (String) getPropertyByName.invoke(me, "script");
                    break;
                case "ColumnSimilarity":
                    dataClassDetails = (String) getPropertyByName.invoke(me, "expression");
                    break;
                case "UnstructuredFilter":
                    ReferenceList filters = (ReferenceList) getPropertyByName.invoke(me, "filters");
                    if (!filters.getItems().isEmpty()) {
                        filters.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());
                        ArrayList<String> filterNames = new ArrayList<>();
                        for (Reference filter : filters.getItems()) {
                            filterNames.add(filter.getName());
                        }
                        dataClassDetails = String.join(", ", filterNames);
                    }
                    break;
                default:
                    dataClassDetails = (String) getPropertyByName.invoke(me, "java_class_name_single");
                    break;
            }
            instanceProperties.setProperty(
                    "specification",
                    ReferenceMapper.getPrimitivePropertyValue(dataClassDetails)
            );

            /*
             * setup the OMRS 'userDefined' property
             * Provider = 'IBM' is only present in v11.7+ to be able to make this determination
             */
            if (igcomrsRepositoryConnector.getIGCVersion().equals("v117")) {
                String provider = (String) getPropertyByName.invoke(me, "provider");
                instanceProperties.setProperty(
                        "userDefined",
                        ReferenceMapper.getPrimitivePropertyValue( (provider == null || !provider.equals("IBM")) )
                );
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to map complex property.", e);
        }

    }

    @Override
    protected void complexRelationshipMappings() {

        /*
         * Add detected relationships to the set of relationships
         */
        complexMapDetectedClassifications();

        /*
         * Add selected relationships to the set of relationships
         */
        complexMapSelectedClassifications();

    }

    private void complexMapDetectedClassifications() {

        // One of the few relationships in IGC that actually has properties of its own!
        // So we need to retrieve this relationship linking object (IGC type 'classification')
        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("data_class", "=", me.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        String[] classificationProperties = new String[]{
                "classifies_asset",
                "confidencePercent",
                P_THRESHOLD
        };
        IGCSearch igcSearch = new IGCSearch("classification", classificationProperties, igcSearchConditionSet);
        if (!igcomrsRepositoryConnector.getIGCVersion().equals("v115")) {
            igcSearch.addProperty("value_frequency");
        }
        ReferenceList detectedClassifications = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);

        detectedClassifications.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        String classificationClassName = ReferenceMapper.IGC_REST_GENERATED_MODEL_PKG + "." + igcomrsRepositoryConnector.getIGCVersion() + ".Classification";
        ClassLoader classLoader = this.getClass().getClassLoader();

        try {

            Class classification = classLoader.loadClass(classificationClassName);
            Method classificationGetPropertyByName = classification.getMethod("getPropertyByName", String.class);

            // For each of the detected classifications, create a new DataClassAssignment relationship
            for (Reference detectedClassification : detectedClassifications.getItems()) {

                Reference classifiedObj = (Reference) classificationGetPropertyByName.invoke(detectedClassification, "classifies_asset");
                InstanceProperties relationshipProperties = new InstanceProperties();

                /* Only proceed with the classified object if it is not a 'main_object' asset
                 * (in this scenario, 'main_object' represents ColumnAnalysisMaster objects that are not accessible
                 *  and will throw bad request (400) REST API errors) */
                if (classifiedObj != null && !classifiedObj.getType().equals("main_object")) {
                    try {

                        Relationship omrsRelationship = getMappedRelationship(
                                "classified_assets_detected",
                                (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(SOURCE_NAME,
                                        R_DATA_CLASS_ASSIGNMENT),
                                "dataClassesAssignedToElement",
                                "elementsAssignedToDataClass",
                                classifiedObj);

                        Number confidence = (Number) classificationGetPropertyByName.invoke(detectedClassification, "confidencePercent");

                        /* Before adding to the overall set of relationships, setup the relationship properties
                         * we have in IGC from the 'classification' object. */
                        relationshipProperties.setProperty(
                                "confidence",
                                ReferenceMapper.getPrimitivePropertyValue(confidence.intValue())
                        );
                        relationshipProperties.setProperty(
                                P_THRESHOLD,
                                ReferenceMapper.getPrimitivePropertyValue(classificationGetPropertyByName.invoke(detectedClassification, P_THRESHOLD))
                        );
                        relationshipProperties.setProperty(
                                "partialMatch",
                                ReferenceMapper.getPrimitivePropertyValue((confidence.intValue() < 100))
                        );
                        if (!igcomrsRepositoryConnector.getIGCVersion().equals("v115")) {
                            relationshipProperties.setProperty(
                                    "valueFrequency",
                                    ReferenceMapper.getPrimitivePropertyValue(classificationGetPropertyByName.invoke(detectedClassification, "value_frequency"))
                            );
                        }
                        EnumPropertyValue status = new EnumPropertyValue();
                        status.setSymbolicName("Discovered");
                        status.setOrdinal(0);
                        relationshipProperties.setProperty(
                                "status",
                                status
                        );

                        omrsRelationship.setProperties(relationshipProperties);
                        relationships.add(omrsRelationship);

                    } catch (RepositoryErrorException e) {
                        log.error("Unable to map relationship.", e);
                    }
                }
            }

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to make use of IGC Classification class.", e);
        }

    }

    private void complexMapSelectedClassifications() {

        // (Note that in IGC these can only be retrieved by looking up all assets for which this data_class is selected,
        // they cannot be looked up as a relationship from the data_class object...  Therefore, start by searching
        // for any assets that list this data_class as their selected_classification
        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("selected_classification", "=", me.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        IGCSearch igcSearch = new IGCSearch("amazon_s3_data_file_field", igcSearchConditionSet);
        igcSearch.addType("data_file_field");
        igcSearch.addType("database_column");
        igcSearch.addProperty("selected_classification");
        igcSearch.addProperties(ReferenceMapper.MODIFICATION_DETAILS);
        ReferenceList assetsWithSelected = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);

        assetsWithSelected.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        for (Reference assetWithSelected : assetsWithSelected.getItems()) {

            try {

                InstanceProperties relationshipProperties = new InstanceProperties();

                Relationship omrsRelationship = getMappedRelationship(
                        "classifications_selected",
                        (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(SOURCE_NAME,
                                R_DATA_CLASS_ASSIGNMENT),
                        "dataClassesAssignedToElement",
                        "elementsAssignedToDataClass",
                        assetWithSelected);

                EnumPropertyValue status = new EnumPropertyValue();
                status.setSymbolicName("Proposed");
                status.setOrdinal(1);
                relationshipProperties.setProperty(
                        "status",
                        status
                );

                omrsRelationship.setProperties(relationshipProperties);

                relationships.add(omrsRelationship);

            } catch (RepositoryErrorException e) {
                log.error("Unable to map relationship.", e);
            }

        }

    }

}
