/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttributeForSchemaMapper_TableColumn;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.SchemaAttributeTypeMapper_DatabaseColumn;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelationalColumnMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.RelationalColumnMapper.class);

    private static final String T_RELATIONAL_COLUMN = "RelationalColumn";
    private static final String C_PRIMARY_KEY = "PrimaryKey";

    public RelationalColumnMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "database_column",
                "Database Column",
                T_RELATIONAL_COLUMN,
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("position", "position");
        addSimplePropertyMapping("minimum_length", "minimumLength");
        addSimplePropertyMapping("length", "length");
        addSimplePropertyMapping("fraction", "fraction");
        addSimplePropertyMapping("allows_null_values", "isNullable");
        addSimplePropertyMapping("unique", "isUnique");

        // The list of relationships that should be mapped
        addRelationshipMapper(AttributeForSchemaMapper_TableColumn.getInstance());
/*        addSimpleRelationshipMapping(
                "database_table_or_view",
                "AttributeForSchema",
                "attributes",
                "parentSchemas",
                null,
                RelationalTableTypeMapper.IGC_RID_PREFIX
        );*/
        addRelationshipMapper(SchemaAttributeTypeMapper_DatabaseColumn.getInstance());
/*        addSimpleRelationshipMapping(
                RelationshipMappingSet.SELF_REFERENCE_SENTINEL,
                "SchemaAttributeType",
                "usedInSchemas",
                "type",
                null,
                RelationalColumnTypeMapper.IGC_RID_PREFIX
        ); */
/*        addSimpleRelationshipMapping(
                "defined_foreign_key_references",
                R_FOREIGN_KEY,
                P_FOREIGN_KEY,
                P_PRIMARY_KEY
        );
        addSimpleRelationshipMapping(
                "defined_foreign_key_referenced",
                R_FOREIGN_KEY,
                P_PRIMARY_KEY,
                P_FOREIGN_KEY
        );
        addSimpleRelationshipMapping(
                "selected_foreign_key_references",
                R_FOREIGN_KEY,
                P_FOREIGN_KEY,
                P_PRIMARY_KEY
        );
        addSimpleRelationshipMapping(
                "selected_foreign_key_referenced",
                R_FOREIGN_KEY,
                P_PRIMARY_KEY,
                P_FOREIGN_KEY
        );*/

        // Finally list any properties that will be used to map Classifications
        // (to do the actual mapping, implement the 'getMappedClassifications' function -- example below)
        addComplexIgcClassification("defined_primary_key");
        addComplexIgcClassification("selected_primary_key");
        addComplexOmrsClassification(C_PRIMARY_KEY);

    }

    /**
     * We implement this method to apply any classifications -- since IGC itself doesn't have a "Classification"
     * asset type, we need to apply our own translation between how we're using other IGC asset types and the
     * Classification(s) we want them to represent in OMRS.
     * <br><br>
     * Here we use the 'defined_primary_key' relationship on an IGC 'database_column' to provide "PrimaryKey"
     * classifications in OMRS.
     */
    @Override
    protected void getMappedClassifications() {

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // Retrieve all assigned_to_terms relationships from this IGC object
        Boolean bSelectedPK = (Boolean) igcEntity.getPropertyByName("selected_primary_key");
        ReferenceList definedPK = (ReferenceList) igcEntity.getPropertyByName("defined_primary_key");

        // If there are no defined PKs, setup a classification only if the user has selected
        // this column as a primary key
        if (definedPK.getItems().isEmpty()) {
            if (bSelectedPK) {
                try {
                    InstanceProperties classificationProperties = new InstanceProperties();
                    classificationProperties.setProperty("name", EntityMapping.getPrimitivePropertyValue(igcEntity.getName()));
                    Classification classification = getMappedClassification(
                            C_PRIMARY_KEY,
                            T_RELATIONAL_COLUMN,
                            classificationProperties
                    );
                    omrsClassifications.add(classification);
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
                            C_PRIMARY_KEY,
                            T_RELATIONAL_COLUMN,
                            classificationProperties
                    );
                    omrsClassifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to create classification.", e);
                }

            }

        }

    }

}
