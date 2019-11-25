/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.Arrays;
import java.util.List;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.LINEAGE_MAPPING;

/**
 * The validator is used for validating whether the entity or relationship types are relevant for asset lineage context.
 */
public class Validator {


    /**
     * Is valid lineage entity event boolean.
     *
     * @param typeDefName the type def name
     * @return the boolean
     */
    public boolean isValidLineageEntityEvent(String typeDefName) {
        final List<String> types = Arrays.asList(GLOSSARY_TERM, TABULAR_SCHEMA_TYPE, TABULAR_COLUMN, RELATIONAL_COLUMN, RELATIONAL_TABLE, DATA_FILE);
        return types.contains(typeDefName);
    }


    /**
     * Is valid lineage relationship event boolean.
     *
     * @param relationship the relationship
     * @return the boolean
     */
    public boolean isValidLineageRelationshipEvent(Relationship relationship) {
        String entityProxyOneType = relationship.getEntityOneProxy().getType().getTypeDefName();
        if (
                relationship.getType().getTypeDefName().equals(SEMANTIC_ASSIGNMENT)
                        &&
                        (
                                entityProxyOneType.equals(RELATIONAL_COLUMN) || entityProxyOneType.equals(RELATIONAL_TABLE)
                                        || entityProxyOneType.equals(TABULAR_COLUMN)
                        )
        )
            return true;

        final List<String> types = Arrays.asList(PROCESS_PORT, PORT_DELEGATION, PORT_SCHEMA, SCHEMA_TYPE, ATTRIBUTE_FOR_SCHEMA, LINEAGE_MAPPING);

        return types.contains(relationship.getType().getTypeDefName());
    }
}
