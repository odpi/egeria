/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_ASSET_OWNERSHIP;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_CONFIDENTIALITY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_INCOMPLETE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_PRIMARY_CATEGORY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_SUBJECT_AREA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.COMPLEX_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_TO_ASSET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_CONTENT_FOR_DATA_SET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FLOW;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FILE_FOLDER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FOLDER_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FLOW;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS_PORT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SCHEMA_TYPE_OPTION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_CATEGORIZATION;

/**
 * Constants for Open Metadata Types names used to build lineage functionality
 */
public class AssetLineageTypesValidator {

    private final OMRSRepositoryHelper repositoryHelper;
    private final HashSet<String> lineageClassificationTypes = new HashSet<>();
    private final HashSet<String> lineageRelationshipTypes = new HashSet<>();

    /**
     * Set up the validator for this server
     *
     * @param repositoryHelper     helper used by the converters
     * @param accessServiceOptions access service options
     */
    public AssetLineageTypesValidator(OMRSRepositoryHelper repositoryHelper, Map<String, Object> accessServiceOptions) {
        this.repositoryHelper = repositoryHelper;

        final Set<String> defaultLineageClassifications =
                Set.of(CLASSIFICATION_NAME_CONFIDENTIALITY, CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP, CLASSIFICATION_NAME_SUBJECT_AREA,
                        CLASSIFICATION_NAME_ASSET_OWNERSHIP, CLASSIFICATION_NAME_PRIMARY_CATEGORY, CLASSIFICATION_NAME_INCOMPLETE);
        final Set<String> defaultDataFileRelationships =
                Set.of(ATTRIBUTE_FOR_SCHEMA, ASSET_SCHEMA_TYPE, NESTED_FILE, FOLDER_HIERARCHY, CONNECTION_TO_ASSET, CONNECTION_ENDPOINT);
        final Set<String> defaultRelationalTableRelationships =
                Set.of(NESTED_SCHEMA_ATTRIBUTE, ATTRIBUTE_FOR_SCHEMA, ASSET_SCHEMA_TYPE, DATA_CONTENT_FOR_DATA_SET, CONNECTION_TO_ASSET,
                        CONNECTION_ENDPOINT);
        final Set<String> defaultTopicRelationships =
                Set.of(ATTRIBUTE_FOR_SCHEMA, SCHEMA_TYPE_OPTION, ASSET_SCHEMA_TYPE);
        final Set<String> defaultProcessRelationships =
                Set.of(ATTRIBUTE_FOR_SCHEMA, ASSET_SCHEMA_TYPE, PORT_SCHEMA, PROCESS_PORT, PROCESS_HIERARCHY, DATA_FLOW, LINEAGE_MAPPING);
        final Set<String> defaultGlossaryTermRelationships =
                Set.of(SEMANTIC_ASSIGNMENT, TERM_CATEGORIZATION);

        if (accessServiceOptions != null) {
            Object lineageClassificationTypesProperty = accessServiceOptions.get(AssetLineageConstants.LINEAGE_CLASSIFICATION_TYPES_KEY);
            if (lineageClassificationTypesProperty != null) {
                lineageClassificationTypes.addAll((Collection<? extends String>) lineageClassificationTypesProperty);
            }
        }
        lineageClassificationTypes.addAll(defaultLineageClassifications);

        lineageRelationshipTypes.addAll(defaultDataFileRelationships);
        lineageRelationshipTypes.addAll(defaultRelationalTableRelationships);
        lineageRelationshipTypes.addAll(defaultTopicRelationships);
        lineageRelationshipTypes.addAll(defaultProcessRelationships);
        lineageRelationshipTypes.addAll(defaultGlossaryTermRelationships);
    }

    /**
     * Checks if the entity classification list contains lineage classifications
     *
     * @param entityDetail the entity object
     *
     * @return true if the entity contains lineage classifications
     */
    public boolean hasValidClassificationTypes(EntityDetail entityDetail) {
        if (CollectionUtils.isEmpty(entityDetail.getClassifications())) {
            return false;
        }

        List<String> classificationNames = entityDetail.getClassifications().stream()
                .map(classification -> classification.getType().getTypeDefName())
                .toList();
        return !Collections.disjoint(lineageClassificationTypes, classificationNames);
    }

    /**
     * Determines if the given relationship is a lineage relationship
     *
     * @param relationship the relationship object
     *
     * @return true if the it is a lineage relationship
     */
    public boolean isValidLineageRelationshipType(Relationship relationship) {
        if (isRelationshipValid(relationship)) {
            return lineageRelationshipTypes.contains(relationship.getType().getTypeDefName());
        }
        return false;
    }

    /**
     * Determines if the given entity is a valid lineage entity
     *
     * @param entityDetail the entity object
     *
     * @return true if the it is a lineage entity of valid type
     */
    public boolean isValidLineageEntityType(EntityDetail entityDetail, String serverName) {
        String typeDefName = entityDetail.getType().getTypeDefName();
        return repositoryHelper.isTypeOf(serverName, typeDefName, ASSET) // this includes Processes, Topics, DataFiles and subtypes
                || repositoryHelper.isTypeOf(serverName, typeDefName, RELATIONAL_TABLE)
                // this includes TabularColumns, TabularFilesColumns, RelationalColumns, EventSchemaAttributes
                || repositoryHelper.isTypeOf(serverName, typeDefName, SCHEMA_ATTRIBUTE)
                || repositoryHelper.isTypeOf(serverName, typeDefName, GLOSSARY_TERM)
                || repositoryHelper.isTypeOf(serverName, typeDefName, PORT)
                // this includes TabularSchemaTypes, RelationalDBSchemaTypes, EventTypes, EventTypeLists
                || repositoryHelper.isTypeOf(serverName, typeDefName, COMPLEX_SCHEMA_TYPE)
                || repositoryHelper.isTypeOf(serverName, typeDefName, CONNECTION)
                || repositoryHelper.isTypeOf(serverName, typeDefName, ENDPOINT)
                || repositoryHelper.isTypeOf(serverName, typeDefName, FILE_FOLDER);
    }


    /**
     * Extract the lineage classifications from the list of classifications assigned
     *
     * @param classifications the list of available classifications
     *
     * @return a list of lineage classifications
     */
    public List<Classification> filterLineageClassifications(List<Classification> classifications) {
        if (CollectionUtils.isNotEmpty(classifications)) {
            return classifications.stream()
                    .filter(classification -> classification.getType() != null)
                    .filter(classification -> lineageClassificationTypes.contains(classification.getType().getTypeDefName()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Sanity checks on the relationship object
     *
     * @param relationship the relationship object
     *
     * @return true if the relationship type is available and if the both ends of the relationship are available
     */
    private boolean isRelationshipValid(Relationship relationship) {
        return relationship.getType() != null
                && relationship.getType().getTypeDefName() != null
                && relationship.getEntityOneProxy() != null
                && relationship.getEntityOneProxy().getType() != null
                && relationship.getEntityOneProxy().getType().getTypeDefName() != null
                && relationship.getEntityTwoProxy() != null
                && relationship.getEntityTwoProxy().getType() != null
                && relationship.getEntityTwoProxy().getType().getTypeDefName() != null;
    }
}