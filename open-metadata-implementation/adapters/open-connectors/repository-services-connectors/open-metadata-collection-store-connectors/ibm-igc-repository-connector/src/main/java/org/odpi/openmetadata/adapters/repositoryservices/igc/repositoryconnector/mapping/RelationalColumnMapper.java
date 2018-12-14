/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelationalColumnMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(RelationalColumnMapper.class);

    /**
     * Sets the basic criteria to use for mapping between an IGC 'database_column' object and an OMRS 'RelationalColumn' object.
     *
     * @param dbColumn the IGC 'database_column' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public RelationalColumnMapper(Reference dbColumn, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dbColumn,
                "database_column",
                "RelationalColumn",
                igcomrsRepositoryConnector,
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
        addSimpleRelationshipMapping(
                "database_table_or_view",
                "AttributeForSchema",
                "attributes",
                "parentSchemas",
                null,
                RelationalTableTypeMapper.IGC_RID_PREFIX
        );
        addSimpleRelationshipMapping(
                RelationshipMappingSet.SELF_REFERENCE_SENTINEL,
                "SchemaAttributeType",
                "usedInSchemas",
                "type",
                null,
                RelationalColumnTypeMapper.IGC_RID_PREFIX
        );
        addSimpleRelationshipMapping(
                "defined_foreign_key_references",
                "ForeignKey",
                "foreignKey",
                "primaryKey"
        );
        addSimpleRelationshipMapping(
                "defined_foreign_key_referenced",
                "ForeignKey",
                "primaryKey",
                "foreignKey"
        );
        addSimpleRelationshipMapping(
                "selected_foreign_key_references",
                "ForeignKey",
                "foreignKey",
                "primaryKey"
        );
        addSimpleRelationshipMapping(
                "selected_foreign_key_referenced",
                "ForeignKey",
                "primaryKey",
                "foreignKey"
        );

        // Finally list any properties that will be used to map Classifications
        // (to do the actual mapping, implement the 'getMappedClassifications' function -- example below)
        addComplexIgcClassification("defined_primary_key");
        addComplexIgcClassification("selected_primary_key");
        addComplexOmrsClassification("PrimaryKey");

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
        Boolean bSelectedPK = (Boolean) me.getPropertyByName("selected_primary_key");
        ReferenceList definedPK = (ReferenceList) me.getPropertyByName("defined_primary_key");

        // If there are no defined PKs, setup a classification only if the user has selected
        // this column as a primary key
        if (definedPK.getItems().isEmpty()) {
            if (bSelectedPK) {
                try {
                    InstanceProperties classificationProperties = new InstanceProperties();
                    classificationProperties.setProperty("name", ReferenceMapper.getPrimitivePropertyValue(me.getName()));
                    Classification classification = getMappedClassification(
                            "PrimaryKey",
                            "RelationalColumn",
                            classificationProperties
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
                    classificationProperties.setProperty("name", ReferenceMapper.getPrimitivePropertyValue(candidateKey.getName()));
                    Classification classification = getMappedClassification(
                            "PrimaryKey",
                            "RelationalColumn",
                            classificationProperties
                    );
                    classifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to create classification.", e);
                }

            }

        }

    }

}
